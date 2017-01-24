package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.gj.gaojiaohui.adapter.AddressGroupAdapter;
import com.gj.gaojiaohui.bean.AddressBean;
import com.gj.gaojiaohui.bean.AddressListBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 通讯录-搜索 界面 add wb
 */
public class AddressSearchActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private AddressGroupAdapter adapter;
	private ListView meeting_news_lv;
	private List<AddressBean> list;
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
		setContentView(R.layout.activity_follow_search);
		mContext = this;

		initView();
		addListener();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.cancel_search_tv);
		title.setOnClickListener(this);
		search_et = (EditText) findViewById(R.id.search_et);
		list = new ArrayList<AddressBean>();
		adapter = new AddressGroupAdapter(mContext, list, R.layout.item_address_child);
		meeting_news_lv = (ListView) findViewById(R.id.follow_lv);
		meeting_news_lv.setAdapter(adapter);
		meeting_news_lv.setOnItemClickListener(this);
	}

	private void getData(String value) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.ADDRESS_BOOK_SEARCH_URL, GloableConfig.userid, value);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		AddressListBean data = CommonUtil.gson.fromJson(json, AddressListBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, "无搜索结果");
		}
	}
	
	private void search() {
		String value = search_et.getText().toString();
		if (!StringUtils.isNull(value)) {
			getData(value);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO
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
