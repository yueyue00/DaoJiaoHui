package com.gj.gaojiaohui.activity;

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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.AudienceSearchAdapter;
import com.gj.gaojiaohui.bean.FollowMyBean;
import com.gj.gaojiaohui.bean.FollowMyListBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 媒体互动 界面 add wb
 */
public class MediaInteractionActivity extends FragmentActivity implements OnClickListener, OnItemClickListener {

	private AudienceSearchAdapter adapter;
	private ListView follow_lv;
	private List<FollowMyBean> list;
	private Context mContext;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_interaction);

		mContext = this;
		getData();
		initView();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.mdia_interaction));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);

		list = new ArrayList<FollowMyBean>();
		adapter = new AudienceSearchAdapter(mContext, list, R.layout.item_follow_search);
		follow_lv = (ListView) findViewById(R.id.follow_lv);
		follow_lv.setAdapter(adapter);
		follow_lv.setOnItemClickListener(this);
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.MEDIAINTER_ACTION, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1);
	}

	private void parseData(Message msg) {
		ProgressUtil.dismissProgressDialog();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 在这里执行跳转到用户详情的流程
		String userId = list.get(position).id;
		Intent intent = new Intent(mContext, MediaInterDetailActivity.class);
		intent.putExtra("userId", userId);
		mContext.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		}

	}

}
