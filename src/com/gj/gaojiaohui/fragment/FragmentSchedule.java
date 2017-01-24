package com.gj.gaojiaohui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.AddScheduleActivity;
import com.gj.gaojiaohui.activity.ExhibitorsActivity;
import com.gj.gaojiaohui.activity.GuestActivity;
import com.gj.gaojiaohui.activity.HuoDongParticularsActivity;
import com.gj.gaojiaohui.activity.ScheduleSearchActivity;
import com.gj.gaojiaohui.adapter.MyExpandableListViewAdapter;
import com.gj.gaojiaohui.adapter.MyRecyclerAdapter;
import com.gj.gaojiaohui.bean.MeetingChildBean;
import com.gj.gaojiaohui.bean.MeetingGroupBean;
import com.gj.gaojiaohui.bean.MeetingScheduleBean;
import com.gj.gaojiaohui.bean.MeetingScheduleListBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 日程列表
 */
public class FragmentSchedule extends Fragment implements OnClickListener {
	private Context mContext;
	private View view;
	private ImageView search;
	private RecyclerView recyclerView;
	/** 顶部时间滑块集合 */
	private List<MeetingScheduleBean> scheduleDatas;
	private MyRecyclerAdapter recycleAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	private ExpandableListView expandableListView;
	private MyExpandableListViewAdapter adapter;
	/** 父布局标题集合 */
	private List<MeetingGroupBean> group_list;
	/** 子布局内容集合 */
	private List<List<MeetingChildBean>> item_list;
	private List<MeetingChildBean> data_list;

	private LinearLayout all_schedule_layout;
	private LinearLayout sample_schedule_layout;
	private LinearLayout data_layout;
	private RelativeLayout net_layout;

	/** 当前点击的类型 0/全部日程 1/个人日程 */
	private int type = 0;

	/** [今天]的数据是第多少个 用于刷新选中状态 */
	private int currentPosition = 0;

	/** 请求回的数据进行解析 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_schedule, null);
		mContext = getActivity();

		getData("", type);
		initSwitchLayout();
		initExpandableListView();
		initRecyclerView();
		initNetworkView();

		return view;
	}

	/** 无网络界面判断 */
	private void initNetworkView() {
		if (CommonUtils.isNetworkConnected(getActivity())) {
			// 有网
			data_layout.setVisibility(View.VISIBLE);
			net_layout.setVisibility(View.GONE);
		} else {
			// 无网
			data_layout.setVisibility(View.GONE);
			net_layout.setVisibility(View.VISIBLE);
		}
	}

	public void getData(String date, int type) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.SCHEDULE_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE, type, date);
		volleyUtil1.stringRequest(handler, url, map, 1001);
	}

	/** 初始化顶部tab选择状态 */
	private void initSelectState() {
		all_schedule_layout.setSelected(false);
		sample_schedule_layout.setSelected(false);
	}
	
	/** 返回当前的日程type 0/全部日程 1/个人日程 主Activity调用,用于刷新录入日程后界面 */
	public int getType() {
		return type;
	}

	/** 初始化顶部tab */
	private void initSwitchLayout() {
		search = (ImageView) view.findViewById(R.id.search_img);
		search.setOnClickListener(this);
		TextView addSchedule = (TextView) view.findViewById(R.id.add_schedule);
		addSchedule.setOnClickListener(this);
		all_schedule_layout = (LinearLayout) view.findViewById(R.id.all_schedule_layout);
		sample_schedule_layout = (LinearLayout) view.findViewById(R.id.sample_schedule_layout);

		data_layout = (LinearLayout) view.findViewById(R.id.data_layout);
		net_layout = (RelativeLayout) view.findViewById(R.id.net_layout);

		LinearLayout netless_refresh_ll = (LinearLayout) view.findViewById(R.id.netless_refresh_ll);
		netless_refresh_ll.setOnClickListener(this);

		all_schedule_layout.setOnClickListener(this);
		sample_schedule_layout.setOnClickListener(this);
		initSelectState();
		all_schedule_layout.setSelected(true);
	}

	/** 初始化顶部日程滑块列表 */
	private void initRecyclerView() {
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		scheduleDatas = new ArrayList<MeetingScheduleBean>();
		mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
		recycleAdapter = new MyRecyclerAdapter(mContext, scheduleDatas);
		recyclerView.setAdapter(recycleAdapter);
		recyclerView.setLayoutManager(mLayoutManager);
		recycleAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, String data, int position) {
				// 重新请求数据
				String date = scheduleDatas.get(position).id;
				getData(date, type);
			}
		});
		// 指定默认选中的item并刷新
		recycleAdapter.setDefaultSelected(currentPosition);
		recycleAdapter.notifyDataSetChanged();
		// 滚动到指定位置
		recyclerView.smoothScrollToPosition(currentPosition);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		MeetingScheduleListBean data = CommonUtil.gson.fromJson(json, MeetingScheduleListBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */

			group_list.clear();
			scheduleDatas.clear();
			item_list.clear();
			data_list.clear();

			// 如果请求回的数据里没有当天的,默认取最近的一天的日期id重新请求数据
			if (data.schedule_list.size() == 0 && data.date_list.size() != 0) {
				String date = data.date_list.get(0).id;
				getData(date, type);
				return;
			}

			/** 填充顶部时间滑块的数据 */
			scheduleDatas.addAll(data.date_list);
			for (int i = 0; i < scheduleDatas.size(); i++) {
				MeetingScheduleBean bean = scheduleDatas.get(i);
				if (bean.now) {
					currentPosition = i;
				}
			}
			// 指定默认选中的item并刷新
			recycleAdapter.setDefaultSelected(currentPosition);
			recycleAdapter.changData(data.date_list);
			// 滚动到指定位置
			recyclerView.smoothScrollToPosition(currentPosition);

			/** 填充底部会议列表的数据 */
			group_list.addAll(data.schedule_list);
			for (MeetingGroupBean bean : group_list) {
				item_list.add(bean.list);
			}
			adapter.changData(group_list, item_list);
			// 首次加载全部展开
			for (int i = 0; i < group_list.size(); i++) {
				expandableListView.expandGroup(i);
			}

		} else {
			/** 请求失败的处理 */
		}
	}

	/** 初始化日程列表 */
	private void initExpandableListView() {
		// // 数个分组标题
		// MeetingGroupBean a = new MeetingGroupBean();
		// a.date = "上午8：00";
		// group_list = new ArrayList<MeetingGroupBean>();
		// for (int i = 0; i < 5; i++) {
		// group_list.add(a);
		// }
		//
		// // 子布局的内容
		// MeetingChildBean b = new MeetingChildBean();
		// b.meeting_title = "会议开幕式";
		// b.meeting_site = "上午8:00至10:00";
		// b.meeting_compere = "主持人: 老虎先生";
		// data_list = new ArrayList<MeetingChildBean>();
		// for (int i = 0; i < 5; i++) {
		// data_list.add(b);
		// }
		//
		// // 数个子布局
		// item_list = new ArrayList<List<MeetingChildBean>>();
		// for (int i = 0; i < 5; i++) {
		// item_list.add(data_list);
		// }

		group_list = new ArrayList<MeetingGroupBean>();
		data_list = new ArrayList<MeetingChildBean>();
		item_list = new ArrayList<List<MeetingChildBean>>();
		adapter = new MyExpandableListViewAdapter(mContext, group_list, item_list);
		expandableListView = (ExpandableListView) view.findViewById(R.id.expendlist);
		expandableListView.setAdapter(adapter);
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// 父群组 子布局 这里执行重新刷新数据
				startActionView(groupPosition, childPosition);
				return false;
			}
		});
		// 首次加载全部展开
		for (int i = 0; i < group_list.size(); i++) {
			expandableListView.expandGroup(i);
		}
		// 不能点击收缩
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		// 去掉箭头
		expandableListView.setGroupIndicator(null);
	}

	/** 根据不同的type打开界面 */
	private void startActionView(int groupPosition, int childPosition) {
		int type = item_list.get(groupPosition).get(childPosition).type;
		String id = item_list.get(groupPosition).get(childPosition).meeting_id;
		switch (type) {
		case 0:// 会议
			Intent intent1 = new Intent(mContext, GuestActivity.class);
			intent1.putExtra("id", id);
			mContext.startActivity(intent1);
			break;
		case 1:// 会展(展商)
			Intent intent2 = new Intent(mContext, ExhibitorsActivity.class);
			intent2.putExtra("id", id);
			mContext.startActivity(intent2);
			break;
		case 2:// 活动
			Intent intent3 = new Intent(mContext, HuoDongParticularsActivity.class);
			intent3.putExtra("userId", Integer.parseInt(id));
			mContext.startActivity(intent3);
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if (!hidden) {
			initNetworkView();
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all_schedule_layout:
			search.setVisibility(View.VISIBLE);
			initSelectState();
			all_schedule_layout.setSelected(true);
			type = 0;
			getData("", type);
			break;
		case R.id.sample_schedule_layout:
			// 点击个人日程做是否登录判断
			if (!GloableConfig.userid.equals("")) {
				search.setVisibility(View.GONE);
				initSelectState();
				sample_schedule_layout.setSelected(true);
				type = 1;
				getData("", type);
			} else {
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.not_login));
			}
			// mContext.startActivity(new Intent(mContext,
			// FollowMyActivity.class));// 测试关注列表用
			// mContext.startActivity(new Intent(mContext,
			// MeetingNewsActivity.class));// 测试大会资讯用
			// mContext.startActivity(new Intent(mContext,
			// AddressGroupActivity.class));// 测试通讯录
			// mContext.startActivity(new Intent(mContext,
			// VipRegisterActivity.class));// 测试嘉宾签到用
			// mContext.startActivity(new Intent(mContext,
			// AudienceInteractionActivity.class));// 测试观众互动用;
			// mContext.startActivity(new Intent(mContext,
			// MediaInteractionActivity.class));// 测试媒体互动用;
			break;
		case R.id.search_img:
			mContext.startActivity(new Intent(mContext, ScheduleSearchActivity.class));
			break;
		case R.id.add_schedule:
			// 点击录入日程做是否登录判断
			if (!GloableConfig.userid.equals("")) {
				// mContext.startActivity(new Intent(mContext,AddScheduleActivity.class));
				getActivity().startActivityForResult(new Intent(mContext, AddScheduleActivity.class), 0);
			} else {
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.not_login));
			}
			break;
		case R.id.netless_refresh_ll:
			// 无网界面点击刷新重新请求数据
			getData("", type);
			if (CommonUtils.isNetworkConnected(getActivity())) {
				initNetworkView();
			}
			break;
		}
	}

}
