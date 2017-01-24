package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.adapter.AddressChildAdapter;
import com.gj.gaojiaohui.bean.AddressBean;
import com.gj.gaojiaohui.view.SideBar;
import com.smartdot.wenbo.huiyi.R;

/**
 * 通讯录二级 界面 add wb
 */
public class AddressChildActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private AddressChildAdapter adapter;
	private ListView address_lv;
	private List<AddressBean> list;
	private Context mContext;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		mContext = this;

		initData();
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
		adapter = new AddressChildAdapter(mContext, list, R.layout.item_address_child);
		address_lv = (ListView) findViewById(R.id.address_lv);
		address_lv.setAdapter(adapter);
		address_lv.setOnItemClickListener(this);
		initIndexView();
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
		// FirstLetterUtil.sortList(list);
		// Collections.sort(list, new PinyinAddressComparator());
		indexBar.setListView(address_lv);
		// 请求数据后要再执行一次
		// adapter.changeData(list);
	}

	private void initData() {
		list = new ArrayList<AddressBean>();

		for (int i = 0; i < 30; i++) {
			AddressBean data = new AddressBean();
			if (i < 3)
				data.name = "安全";
			else if (i < 6)
				data.name = "盼盼";
			else if (i < 8)
				data.name = "帕奇";
			else if (i < 9)
				data.name = "芳华";
			else if (i < 13)
				data.name = "爱雨";
			else if (i < 19)
				data.name = "卡鲁";
			else if (i < 23)
				data.name = "ub丹棱";
			else if (i < 26)
				data.name = "ua赞叹";
			else
				data.name = "转欧留言";
			list.add(data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 在这里执行跳转到人员详情的流程

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
