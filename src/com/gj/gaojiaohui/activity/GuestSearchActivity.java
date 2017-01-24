package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

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

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.GuestSearchAdapter;
import com.gj.gaojiaohui.bean.GuestBean;
import com.gj.gaojiaohui.bean.GuestInfoBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.smartdot.wenbo.huiyi.R;

/**
 * 嘉宾搜索 界面 add zyj
 */
public class GuestSearchActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private GuestSearchAdapter adapter;
	private ListView guestsearch_lv;
	private List<GuestInfoBean> listData = new ArrayList<>();
	private Context mContext;
	private EditText search_et;
	private String content;
	/** 请求回的数据进行解析 */
	Handler guestlist_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "嘉宾搜索数据："+json);
		if (json != null && !json.equals("")) {
			// 通过gson将json映射成实体类
			GuestBean data = CommonUtil.gson.fromJson(json, GuestBean.class);// 这段崩溃有3种原因
			switch (data.resultCode) {
			case 200:
				listData.clear();
				listData.addAll(data.list);
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}

	String muser_id = "";
	String language = "";
	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guestsearch);
		SharePreferenceUtils.getAppConfig(this);
		mContext = this;
		initView();
		process();
		addListener();
	}

	private void process() {
		try {// 获取用户id
			muser_id = Constant.decode(Constant.key, (String) SharePreferenceUtils.getParam("user_id", ""));
			language = (String) SharePreferenceUtils.getParam("language", "");
			if (language.equals("en")) {
				language = "2";
			} else {
				language = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getIntent().getStringExtra("type") != null) {
			switch (getIntent().getStringExtra("type")) {// 获取type类型
			case "liebiao":
				type = "0";
				break;
			case "jiaoliu":
				type = "1";
				break;
			default:
				break;
			}
		}
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.cancel_search_tv);
		title.setOnClickListener(this);
		search_et = (EditText) findViewById(R.id.search_et);
		adapter = new GuestSearchAdapter(mContext, listData, R.layout.guestlist_item);
		guestsearch_lv = (ListView) findViewById(R.id.guestsearch_lv);
		guestsearch_lv.setAdapter(adapter);
		guestsearch_lv.setOnItemClickListener(this);
	}

	private void addListener() {
		search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 搜索方法
					getSearchResult();
					return true;
				}
				return false;
			}

		});
	}

	/**
	 * 搜索方法
	 */
	public void getSearchResult() {
		content = search_et.getText().toString();
		String murl = String.format(GloableConfig.GUESTS_SEARCH_URL, muser_id, language, type, content);
		L.v("lixm", "嘉宾交流搜索:"+murl);
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		Constant.getDataByVolley(mContext, murl, guestlist_handler, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 在这里执行跳转到展商详情的流程
		// CommonUtils.hideKeyboard(FollowSearchActivity.this);
		Intent intent = new Intent(mContext, GuestInfoActivity.class);
		if (getIntent().getStringExtra("type") != null)
			intent.putExtra("type", getIntent().getStringExtra("type"));
		intent.putExtra("userid", listData.get(position).id);
		intent.putExtra("tag", listData.get(position).tag);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.cancel_search_tv:
			getSearchResult();
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
