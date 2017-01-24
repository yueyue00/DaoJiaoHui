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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.AudienceDetailActivity;
import com.gj.gaojiaohui.activity.AudienceInteractionActivity;
import com.gj.gaojiaohui.adapter.FollowMyListAdapter;
import com.gj.gaojiaohui.bean.FollowMyBean;
import com.gj.gaojiaohui.bean.FollowMyListBean;
import com.gj.gaojiaohui.bean.VipRegisterBean;
import com.gj.gaojiaohui.port.OnChangeFollowMyListener;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众互动 - 关注我的 add wb
 */
public class FragmentFollowMy extends Fragment implements OnItemClickListener, OnChangeFollowMyListener {
	private View view;
	private FollowMyListAdapter adapter;
	private ListView follow_lv;
	private List<FollowMyBean> list;
	private Context mContext;
	/** 同步锁 为true代表正在请求数据 */
	private boolean lock;
	/** 请求回的数据进行解析 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				parseData(msg);
				break;
			case 2:
				parseResult(msg);
				break;
			}
		}
	};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_follow_my, null);
		mContext = getActivity();

		getData();
		initView();
		return view;
	}

	private void initView() {
		list = new ArrayList<FollowMyBean>();
		adapter = new FollowMyListAdapter(mContext, list, R.layout.item_follow_my);
		adapter.setOnShopAppRefreshListener(this);
		follow_lv = (ListView) view.findViewById(R.id.follow_lv);
		follow_lv.setAdapter(adapter);
		follow_lv.setOnItemClickListener(this);
	}

	private void getData() {
		lock = true;
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.AUDIENCE_FOLLOW_MY, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1);
	}

	private void parseData(Message msg) {
		lock = false;
		String json = msg.obj.toString();
		FollowMyListBean data = CommonUtil.gson.fromJson(json, FollowMyListBean.class);
		if (data.resultCode == 200) {
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			CustomToast.showToast(mContext, "请求关注列表失败!");
		}
	}

	private void parseResult(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		VipRegisterBean data = CommonUtil.gson.fromJson(json, VipRegisterBean.class);
		if (data.resultCode == 200) {
			getData();
			// 刷新隔壁的[我的关注]数据
			((AudienceInteractionActivity) getActivity()).Changed();
		} else {
			CustomToast.showToast(mContext, "失败!");
		}
	}

	/** 关注/取消关注 */
	private void cancelFollow(String userId, String exhibitorId, boolean state) {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url;
		if (state) {
			url = String.format(GloableConfig.AUDIENCE_FOLLOW, GloableConfig.userid, exhibitorId, 1);
		} else {
			url = String.format(GloableConfig.AUDIENCE_CANCEL_FOLLOW, GloableConfig.userid, exhibitorId);
		}
		volleyUtil.stringRequest(handler, url, map, 2);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 在这里执行跳转到用户详情的流程
		String userId = list.get(position).id;
		Intent intent = new Intent(mContext, AudienceDetailActivity.class);
		intent.putExtra("userId", userId);
		mContext.startActivity(intent);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!lock) {
			getData();
		}
		super.onResume();
	}

	@Override
	public void OnRefresh(String exhibitorId, boolean state) {
		// 在这里执行取消关注该用户的方法 事后记得要重新请求数据
		cancelFollow(GloableConfig.userid, exhibitorId, state);
	}

}
