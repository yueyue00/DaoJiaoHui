package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.VipRegisterAdapter;
import com.gj.gaojiaohui.bean.VipRegisterBean;
import com.gj.gaojiaohui.bean.VipRegisterListBean;
import com.gj.gaojiaohui.port.OnChangeVipRegisterListener;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.SideBar;
import com.smartdot.wenbo.huiyi.R;

/**
 * 嘉宾签到 界面 add wb
 */
public class VipRegisterActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnChangeVipRegisterListener {

	private VipRegisterAdapter adapter;
	private ListView register_lv;
	private List<VipRegisterListBean> list;
	private Context mContext;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	/** 请求回的数据进行解析 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 2:
				parseResult(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_register);
		mContext = this;

		getData();
		initView();
		initIndexView();
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String param = GloableConfig.VIP_REGISTER_LIST;
		String url = String.format(param, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1);
	}

	/** 嘉宾签到 */
	private void vipSign(String vipId, String btnId) {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String param = GloableConfig.VIP_REGISTER;
		String url = String.format(param, GloableConfig.userid, vipId, btnId);
		volleyUtil.stringRequest(handler, url, map, 2);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		VipRegisterBean data = CommonUtil.gson.fromJson(json, VipRegisterBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			list.clear();
			list.addAll(data.list);
			adapter.changeData(list, data.permission);
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, "请求签到列表失败!");
		}
	}

	/** 解析签到返回的结果 */
	private void parseResult(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		VipRegisterBean data = CommonUtil.gson.fromJson(json, VipRegisterBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			getData();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, "签到失败!");
		}
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.guest_sign));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		list = new ArrayList<VipRegisterListBean>();
		adapter = new VipRegisterAdapter(mContext, list, R.layout.item_vip_register);
		adapter.setOnShopAppRefreshListener(this);
		register_lv = (ListView) findViewById(R.id.register_lv);
		register_lv.setAdapter(adapter);
	}

	/** 初始化A-Z索引view */
	@SuppressLint("InflateParams")
	private void initIndexView() {
		indexBar = (SideBar) findViewById(R.id.sideBar);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		// 根据a-z进行排序
		indexBar.setListView(register_lv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		}

	}

	@Override
	public void OnRegisterData(final String userId, final String btnId) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.sign_in).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				vipSign(userId, btnId);
				// CustomToast.showToast(mContext, "当前点击的按钮id是" + btnId + "/n" +  "当前嘉宾id是" + userId);
				dialog.dismiss();
			}

		}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});

		builder.create();
		builder.show();
	}
}
