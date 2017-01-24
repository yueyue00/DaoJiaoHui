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
import com.gj.gaojiaohui.adapter.MyFollowListAdapter;
import com.gj.gaojiaohui.bean.MyFollowBean;
import com.gj.gaojiaohui.bean.MyFollowListBean;
import com.gj.gaojiaohui.port.OnChangeMyFollowListener;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众互动 - 我关注的 add wb
 */
public class FragmentMyFollow extends Fragment implements OnItemClickListener, OnChangeMyFollowListener {
	private View view;
	private MyFollowListAdapter adapter;
	private ListView follow_lv;
	private List<MyFollowBean> list;
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
		list = new ArrayList<MyFollowBean>();
		adapter = new MyFollowListAdapter(mContext, list, R.layout.item_my_follow);
		adapter.setOnShopAppRefreshListener(this);
		follow_lv = (ListView) view.findViewById(R.id.follow_lv);
		follow_lv.setAdapter(adapter);
		follow_lv.setOnItemClickListener(this);
	}

	public void getData() {
		lock = true;
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.AUDIENCE_MY_FOLLOW, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1);
	}

	private void parseData(Message msg) {
		lock = false;
		String json = msg.obj.toString();
		MyFollowListBean data = CommonUtil.gson.fromJson(json, MyFollowListBean.class);
		if (data.resultCode == 200) {
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			CustomToast.showToast(mContext, "请求关注列表失败!");
		}
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
	public void OnRefresh(String id, float rating) {
		// 在这里执行对该用户评星的方法 事后记得要重新请求数据
	}
}
