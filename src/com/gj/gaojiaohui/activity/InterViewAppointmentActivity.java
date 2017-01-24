package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.InterViewAppointmentAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsListBean;
import com.gj.gaojiaohui.bean.InterviewAppointmentBean;
import com.gj.gaojiaohui.bean.InterviewAppointmentChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.SideBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 采访预约
 * 
 * @author lixiaoming
 * 
 */
public class InterViewAppointmentActivity extends GaoJiaoHuiBaseActivity implements OnItemClickListener, OnClickListener, OnRefreshListener2<ListView> {

	private ImageView img_back;
	private TextView title_tv;
	private ImageView search_img;
	private PullToRefreshListView listView;

	private Context mContext;
	private List<InterviewAppointmentChildBean> list;
	private InterViewAppointmentAdapter adapter;
	/** 首页 */
	private int FIRST_PAGE = 1;
	/** 数据请求页码 */
	private int toPage = 1;
	/** 加载更所数据 有数据/true-没数据/false */
	private boolean isMore = true;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 1002:
				pageLoad(msg);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inter_view_appointment);
		mContext = this;
		initView();
		getData();
	}

	/**
	 * 从网络获取数据
	 */
	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.INTERVIEW_APPOINTMENT_URL, FIRST_PAGE, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		img_back = (ImageView) findViewById(R.id.custom_back_img);
		img_back.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.appointment_title));
		search_img = (ImageView) findViewById(R.id.serach_img);
		search_img.setVisibility(View.VISIBLE);
		search_img.setOnClickListener(this);
		listView = (PullToRefreshListView) findViewById(R.id.appointment_lv);

		list = new ArrayList<InterviewAppointmentChildBean>();
		adapter = new InterViewAppointmentAdapter(mContext, list, R.layout.interview_appointment_item);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		listView.setOnRefreshListener(this);

	}

	/**
	 * 解析json数据
	 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		InterviewAppointmentBean data = CommonUtil.gson.fromJson(json, InterviewAppointmentBean.class);// 这段崩溃有3种原因
		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理，在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
			toPage++;
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 分页加载json数据 */
	private void pageLoad(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		InterviewAppointmentBean data = CommonUtil.gson.fromJson(json, InterviewAppointmentBean.class);// 这段崩溃有3种原因
		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			if (data.list.size() != 0) {// 有数据
				isMore = true;
				list.addAll(data.list);
				adapter.notifyDataSetChanged();
				toPage++;
			} else {// 没有数据
				isMore = false;
			}
			if (listView.isRefreshing()) {
				listView.onRefreshComplete();
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, AppointmentDetailActivity.class);
		intent.putExtra("id", list.get(position - 1).id);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.serach_img:
			Intent intent = new Intent(mContext, InterViewAppointmentSearchActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 下拉刷新

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载
		if (isMore) {
			listView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
			listView.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
			listView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		} else {
			listView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.nothing));
			listView.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.nothing));
			listView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.nothing));
		}
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.INTERVIEW_APPOINTMENT_URL, toPage, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1002);// 1002是msg.what
	}
}
