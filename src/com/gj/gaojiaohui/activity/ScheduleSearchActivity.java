package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.SearchListAdapter;
import com.gj.gaojiaohui.bean.MeetingChildBean;
import com.gj.gaojiaohui.bean.MeetingSearchBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 日程列表-搜索 界面 add wb
 */
public class ScheduleSearchActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private SearchListAdapter adapter;
	private ListView meeting_news_lv;
	private List<MeetingChildBean> list;
	private Context mContext;
	private EditText search_et;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_searchs);
		mContext = this;

		initView();
		addListener();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.cancel_search_tv);
		title.setOnClickListener(this);
		search_et = (EditText) findViewById(R.id.search_et);
		list = new ArrayList<MeetingChildBean>();
		adapter = new SearchListAdapter(mContext, list, R.layout.item_schedule_child);
		meeting_news_lv = (ListView) findViewById(R.id.search_lv);
		meeting_news_lv.setAdapter(adapter);
		meeting_news_lv.setOnItemClickListener(this);
	}

	private void getData(String value) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = String.format(GloableConfig.SCHEDULE_SEARCH_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE, value);
		volleyUtil1.stringRequest(handler, url, map, 1001);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		MeetingSearchBean data = CommonUtil.gson.fromJson(json, MeetingSearchBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			list.clear();
			list.addAll(data.schedule_list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
		}
	}

	private void addListener() {
		search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 搜索方法
					search();
					return true;
				}
				return false;
			}

		});
	}

	private void search() {
		String value = search_et.getText().toString();
		if (!StringUtils.isAsNull(value)) {
			getData(value);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long Id) {
		// TODO 在这里执行跳转到日程详情的流程
		int type = list.get(position).type;
		String id = list.get(position).meeting_id;
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
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.cancel_search_tv:
			search();
			break;

		default:
			break;
		}

	}

	/** 触摸空白区域关闭软键盘 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		}
		return false;
	}
}
