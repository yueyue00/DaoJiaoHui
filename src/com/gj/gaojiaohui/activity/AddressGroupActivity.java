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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.AddressGroupAdapter;
import com.gj.gaojiaohui.bean.AddressBean;
import com.gj.gaojiaohui.bean.AddressListBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 通讯录 界面 add wb
 */
public class AddressGroupActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private AddressGroupAdapter adapter;
	private ListView address_lv;
	private List<AddressBean> list;
	private Context mContext;
	private String deptId = "";

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
		setContentView(R.layout.activity_address);
		mContext = this;

		getData();
		initView();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.address_group));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		ImageView serach = (ImageView) findViewById(R.id.serach_img);
		serach.setVisibility(View.VISIBLE);
		serach.setOnClickListener(this);

		list = new ArrayList<AddressBean>();
		adapter = new AddressGroupAdapter(mContext, list, R.layout.item_address_group);
		address_lv = (ListView) findViewById(R.id.address_lv);
		address_lv.setAdapter(adapter);
		address_lv.setOnItemClickListener(this);
	}

	private void getData() {
		String id = "" + getIntent().getStringExtra("id");
		if (!StringUtils.isAsNull(id)) {
			deptId = id;
		}

		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.ADDRESS_BOOK_URL, GloableConfig.userid, deptId);
		volleyUtil1.stringRequest(handler, url, map, 1001);
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
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 如果点击的是部门,就再打开一次,并将id传过去
		if (list.get(position).is_dept) {
			Intent intent = new Intent(mContext, AddressGroupActivity.class);
			intent.putExtra("id", list.get(position).dept_id);
			mContext.startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.serach_img:
			// TODO 在这里执行跳转到搜索界面的流程
			mContext.startActivity(new Intent(mContext, AddressSearchActivity.class));
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
