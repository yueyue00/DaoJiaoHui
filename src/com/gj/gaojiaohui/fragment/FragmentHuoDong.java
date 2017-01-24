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
import com.gj.gaojiaohui.activity.HuoDongParticularsActivity;
import com.gj.gaojiaohui.adapter.MyHuoDongAdapter;
import com.gj.gaojiaohui.bean.ActivityListBean;
import com.gj.gaojiaohui.bean.ActivityListChildBean;
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

/**
 * 活动列表
 * 
 * @author Administrator
 * 
 */
public class FragmentHuoDong extends Fragment implements com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>,
		OnItemClickListener, OnClickListener {

	private View view;
	private PullToRefreshListView pulltorefresh_huodong;
	private LinearLayout netless_linear;
	private LinearLayout netless_refresh_ll;
	// private ImageView netless_offline_img;

	private List<ActivityListChildBean> list;
	private Context mContext;
	private MyHuoDongAdapter adapter;

	/** 加载更所数据 有数据/true-没数据/false */
	private boolean isMore = true;
	/** 首页 */
	private int FIRST_PAGE = 1;
	/** 请求回的数据进行解析 */
	public int pageSize = 1;
	/** 判断数据请求是否成功了 成功了不可以再次请求 */
	private boolean isNetComplete;

	FragmentExhibit fragmentExhibit;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 第一页数据
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 1:
				// 上拉加载数据
				parseDataMore(msg);
				break;
			}
		}
	};

	public FragmentHuoDong(FragmentExhibit fragmentExhibit) {
		this.fragmentExhibit = fragmentExhibit;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_huodong, container, false);
		mContext = getActivity();
		initView();
		isNetWork();
		return view;
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

	private void initView() {
		pulltorefresh_huodong = (PullToRefreshListView) view.findViewById(R.id.three_list_huodong);
		netless_linear = (LinearLayout) view.findViewById(R.id.netless_linear);
		netless_refresh_ll = (LinearLayout) view.findViewById(R.id.netless_refresh_ll);
		netless_refresh_ll.setOnClickListener(this);

		list = new ArrayList<ActivityListChildBean>();
		adapter = new MyHuoDongAdapter(mContext, list, R.layout.three_huodong_item);
		pulltorefresh_huodong.setAdapter(adapter);
		pulltorefresh_huodong.setOnItemClickListener(this);

		pulltorefresh_huodong.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		pulltorefresh_huodong.setOnRefreshListener(this);
	}

	/** 无网、有网界面显示 */
	public void isNetWork() {
		if (CommonUtils.isNetworkConnected(mContext)) {// 有网
			setVisibilityForView(View.VISIBLE, view.GONE);
			getData();
			pageSize = 1;
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
		pulltorefresh_huodong.setVisibility(visibility);
		netless_linear.setVisibility(visibilityNetless);
		netless_refresh_ll.setVisibility(visibilityNetless);
		// netless_offline_img.setVisibility(visibilityNetless);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		isNetComplete = false;
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		ActivityListBean data = CommonUtil.gson.fromJson(json, ActivityListBean.class);// 这段崩溃有3种原因//
		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
			pageSize++;

		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 解析json数据 */
	private void parseDataMore(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		ActivityListBean data = CommonUtil.gson.fromJson(json, ActivityListBean.class);// 这段崩溃有3种原因
																						// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			if (data.list.size() != 0) {// 有数据
				isMore = true;
				list.addAll(data.list);
				adapter.notifyDataSetChanged();
				pageSize++;
				isNetComplete = false;
			} else {// 没有数据
				isMore = false;
			}
			if (pulltorefresh_huodong.isRefreshing()) {
				pulltorefresh_huodong.onRefreshComplete();
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// isNetWork();
			// pageSize = 1;
			// getData();
			// netJudge();
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

	/**
	 * 网络判断 无网时加载无网界面 有网是加载有网络的界面并请求数据
	 */
	// public void netJudge() {
	// ProgressUtil.showPregressDialog(getActivity(), R.layout.custom_progress);
	// if (NetUtils.isConnected(getActivity())) {
	// ProgressUtil.dismissProgressDialog();
	// pulltorefresh_huodong.setVisibility(View.VISIBLE);
	// netless_linear.setVisibility(View.GONE);
	// netless_refresh_ll.setVisibility(View.GONE);
	// getData();
	// } else {
	// ProgressUtil.dismissProgressDialog();
	// pulltorefresh_huodong.setVisibility(View.GONE);
	// netless_linear.setVisibility(View.VISIBLE);
	// netless_refresh_ll.setVisibility(View.VISIBLE);
	// CustomToast.showToast(getActivity(),
	// getResources().getString(R.string.netless_toast));
	// }
	// }

	public void getData() {
		isNetComplete = true;
		String url = String.format(GloableConfig.HUODONG_LIST_URL, FIRST_PAGE, GloableConfig.LANGUAGE_TYPE);
		L.v("lixm", "活动列表 的url:" + url);
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, 0);
	}

	/** 上拉加载数据 */
	private void moreGetData() {
		String url = String.format(GloableConfig.HUODONG_LIST_URL, pageSize, GloableConfig.LANGUAGE_TYPE);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, 1);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载
		if (isMore) {
			pulltorefresh_huodong.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
			pulltorefresh_huodong.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
			pulltorefresh_huodong.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		} else {
			pulltorefresh_huodong.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.nothing));
			pulltorefresh_huodong.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.nothing));
			pulltorefresh_huodong.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.nothing));
		}
		moreGetData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int userId = list.get(position - 1).id;
		Intent intent = new Intent(getActivity(), HuoDongParticularsActivity.class);
		intent.putExtra("userId", userId);
		mContext.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.netless_refresh_ll:
			isNetWork();
			// pageSize = 1;
			// getData();
			// netJudge();
			break;

		default:
			break;
		}
	}
}
