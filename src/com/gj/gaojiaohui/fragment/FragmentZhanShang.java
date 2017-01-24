package com.gj.gaojiaohui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.GaojiaoMainActivity;
import com.gj.gaojiaohui.activity.ZhanShangParticularsActivity;
import com.gj.gaojiaohui.adapter.MyZhanShangListAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsListBean;
import com.gj.gaojiaohui.bean.ExhibitorsListChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smartdot.wenbo.huiyi.R;

/***
 * 展商列表
 * 
 * @author Administrator
 * 
 */
public class FragmentZhanShang extends Fragment implements OnItemClickListener,
		com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>, OnClickListener {

	private View view;
	private PullToRefreshListView pulltorefresh_zhanshang;
	private LinearLayout netless_ll;
	private LinearLayout netless_refresh_ll;
	// private ImageView netless_offline_img;

	private List<ExhibitorsListChildBean> list;
	private MyZhanShangListAdapter adapter;
	private Context mContext;
	private ExhibitorsListChildBean exhibitBean;
	/** 首页 */
	private int FIRST_PAGE = 1;
	/** 数据请求页码 */
	public int toPage = 1;
	/** 加载更所数据 有数据/true-没数据/false */
	private boolean isMore = true;
	/** 判断数据请求是否成功了 成功了不可以再次请求 */
	private boolean isNetComplete;
	
	FragmentExhibit fragmentExhibit;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				// 获取第一页数据
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 1002:
				// 获取分页数据
				pageLoad(msg);
				break;
			}
		}
	};

	public FragmentZhanShang(FragmentExhibit fragmentExhibit) {
		// TODO Auto-generated constructor stub
		this.fragmentExhibit = fragmentExhibit;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_zhanshang, container, false);
		mContext = getActivity();
		initView();
		isNetWork();
		return view;
	}

	/** 无网、有网界面显示 */
	public void isNetWork() {
		if (CommonUtils.isNetworkConnected(mContext)) {// 有网
			setVisibilityForView(View.VISIBLE, view.GONE);
			getData();
			toPage = 1;
		} else {// 无网
			setVisibilityForView(View.GONE, view.VISIBLE);
			CustomToast.showToast(mContext, getResources().getString(R.string.net_error));
		}
	}

	/**
	 * 设置有网和无网界面的显示与隐藏
	 * 
	 * @param visibility
	 *            设置有网界面控件的显示
	 * @param visibilityNetless
	 *            设置无网界面的显示
	 */
	public void setVisibilityForView(int visibility, int visibilityNetless) {
		pulltorefresh_zhanshang.setVisibility(visibility);
		netless_ll.setVisibility(visibilityNetless);
		netless_refresh_ll.setVisibility(visibilityNetless);
	}

	@Override
	public void onResume() {
		GaojiaoMainActivity a = (GaojiaoMainActivity) getActivity();
		boolean b = a.getHidden();
		boolean c = this.isHidden();
		// 当前Fragment处于显示状态并且没有请求数据时
		if(!c){
			if (!b && !isNetComplete) {
				isNetWork();
			}
		}

		super.onResume();
	}

	/** 解析json数据 */
	public void parseData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "展商列表:"+json);
		// 通过gson将json映射成实体类
		ExhibitorsListBean data = CommonUtil.gson.fromJson(json, ExhibitorsListBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
			toPage++;
			isNetComplete = false;
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 分页加载json数据 */
	private void pageLoad(Message msg) {
		isNetComplete = false;
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		ExhibitorsListBean data = CommonUtil.gson.fromJson(json, ExhibitorsListBean.class);// 这段崩溃有3种原因
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
			if (pulltorefresh_zhanshang.isRefreshing()) {
				pulltorefresh_zhanshang.onRefreshComplete();
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	public void getData() {
		isNetComplete = true;
		ProgressUtil.showPregressDialog(getActivity(), R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.ZHANSHANG_LIST_URL, FIRST_PAGE, GloableConfig.LANGUAGE_TYPE, GloableConfig.userid);
		L.v("lixm", "展商列表 的接口:"+url);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what

	}

	private void initView() {
		pulltorefresh_zhanshang = (PullToRefreshListView) view.findViewById(R.id.three_list_zhanshang);
		netless_ll = (LinearLayout) view.findViewById(R.id.netless_linear);
		netless_refresh_ll = (LinearLayout) view.findViewById(R.id.netless_refresh_ll);
		netless_refresh_ll.setOnClickListener(this);
		// netless_offline_img = (ImageView)
		// view.findViewById(R.id.netless_offline_img);

		list = new ArrayList<ExhibitorsListChildBean>();
		adapter = new MyZhanShangListAdapter(getActivity(), list, R.layout.three_zhanshang_item);
		pulltorefresh_zhanshang.setAdapter(adapter);
		pulltorefresh_zhanshang.setOnItemClickListener(this);

		pulltorefresh_zhanshang.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		pulltorefresh_zhanshang.setOnRefreshListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String exhibitorId = list.get(position - 1).id;
		Intent intent = new Intent(mContext, ZhanShangParticularsActivity.class);
		intent.putExtra("exhibitorId", exhibitorId);
		mContext.startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载
		if (isMore) {
			pulltorefresh_zhanshang.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
			pulltorefresh_zhanshang.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
			pulltorefresh_zhanshang.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		} else {
			pulltorefresh_zhanshang.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.nothing));
			pulltorefresh_zhanshang.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.nothing));
			pulltorefresh_zhanshang.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.nothing));
		}
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.ZHANSHANG_LIST_URL, toPage, GloableConfig.LANGUAGE_TYPE, GloableConfig.userid);
		volleyUtil1.stringRequest(handler, url, map, 1002);// 1002是msg.what
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if (!hidden) {
			// 当前fragment重新show出来的时候再次加载数据
			// isNetWork();
			// toPage = 1;
			// getData();
			// 不请求数据只刷新状态
			if (CommonUtils.isNetworkConnected(mContext)) {// 有网
				setVisibilityForView(View.VISIBLE, view.GONE);
			} else {// 无网
				setVisibilityForView(View.GONE, view.VISIBLE);
				CustomToast.showToast(mContext, getResources().getString(R.string.net_error));
			}
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.netless_refresh_ll:// 无网界面点击刷新
			isNetWork();
			// toPage = 1;
			// getData();
			break;
		}
	}
}
