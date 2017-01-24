package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.a.a.m;

import com.gj.gaojiaohui.adapter.TongZhiAdapter;
import com.gj.gaojiaohui.bean.TongZhiBean;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 大会通知的list
 * 
 * @author zhangt
 * 
 */
public class TongZhiListActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private TextView titleTextView;
	private ListView mListView;
	private List<TongZhiBean> tongZhiBeans;
	private TongZhiAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tong_zhi_list);
		getData();
		initView();
	}

	private void initView() {
		mContext = this;
		backImageView = (ImageView) findViewById(R.id.custom_title_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_title_tv);
		mListView = (ListView) findViewById(R.id.tongzhi_list_lv);

		mAdapter = new TongZhiAdapter(mContext, tongZhiBeans, R.layout.item_tongzhi_layout);
		mListView.setAdapter(mAdapter);

		backImageView.setOnClickListener(this);
		titleTextView.setText("");

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, TongZhiDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	private void getData() {
		tongZhiBeans = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			TongZhiBean bean = new TongZhiBean();
			bean.title = "高交会开幕时间通知";
			tongZhiBeans.add(bean);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_title_back_img:
			finish();
			break;

		default:
			break;
		}

	}
}
