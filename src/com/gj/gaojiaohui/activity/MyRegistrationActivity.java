package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.RegistrationAdapter;
import com.gj.gaojiaohui.bean.SignUpListBean;
import com.gj.gaojiaohui.bean.SignUpListChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的报名
 * 
 * @author Administrator
 * 
 */
public class MyRegistrationActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private ListView my_registration_listview;
	private List<SignUpListChildBean> list;
	private Context mContext;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			}
		}
	};
	private RegistrationAdapter adapter;
	private TextView title_tv;
	private ImageView custom_back_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_registration);

		mContext = this;
		initView();
		getData();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getData();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.my_registration));
		custom_back_img = (ImageView) findViewById(R.id.custom_back_img);
		my_registration_listview = (ListView) findViewById(R.id.my_registration_listview);

		list = new ArrayList<SignUpListChildBean>();
		adapter = new RegistrationAdapter(mContext, list, R.layout.my_registration_item);
		my_registration_listview.setAdapter(adapter);
		my_registration_listview.setOnItemClickListener(this);
		custom_back_img.setOnClickListener(this);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		SignUpListBean data = CommonUtil.gson.fromJson(json, SignUpListBean.class);// 这段崩溃有3种原因
																					// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	public void getData() {
		String url = String.format(GloableConfig.MY_REGISTRATION_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, 0);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int userId = list.get(position).id;
		Intent intent = new Intent(MyRegistrationActivity.this, HuoDongParticularsActivity.class);
		intent.putExtra("userId", userId);
		startActivity(intent);
	}
}
