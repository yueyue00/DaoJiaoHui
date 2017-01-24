package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gheng.exhibit.http.body.response.InitData;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MeetingDatasAdapter;
import com.gj.gaojiaohui.bean.MeetingDatasBean;
import com.gj.gaojiaohui.bean.MeetingDatasChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 大会资料
 * 
 * @author lixiaoming
 * 
 */
public class MeetingDatasActivity extends Activity implements OnClickListener {

	private ImageView img_back;
	private TextView title_tv;
	private ListView listView;

	private Context mContext;
	private List<MeetingDatasChildBean> list;
	private MeetingDatasAdapter adapter;
	private String type;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_datas);
		mContext = this;
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		initView();
		getData();
	}

	/**
	 * 请求接口数据
	 */
	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.MEETING_DATAS_URL, type, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		img_back = (ImageView) findViewById(R.id.custom_back_img);
		img_back.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		if (type.equals("1")) {// 媒体
			title_tv.setText(getResources().getString(R.string.media_datas_title));
		} else if (type.equals("2")) {// 大会
			title_tv.setText(getResources().getString(R.string.meetingdatas_title));
		}
		listView = (ListView) findViewById(R.id.meeting_datas_lv);

		list = new ArrayList<MeetingDatasChildBean>();
		adapter = new MeetingDatasAdapter(mContext, list, R.layout.meeting_datas_item);
		listView.setAdapter(adapter);
	}

	/**
	 * 解析json数据
	 * 
	 * @param msg
	 */
	private void parseData(Message msg) {
		String json = (String) msg.obj;
		MeetingDatasBean data = CommonUtil.gson.fromJson(json, MeetingDatasBean.class);
		if (data.resultCode == 200) {
			/** 请求成功 填充数据 刷新adapter */
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;

		default:
			break;
		}
	}
}
