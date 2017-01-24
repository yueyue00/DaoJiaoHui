package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.adapter.TongZhiCenterAdapter;
import com.gj.gaojiaohui.bean.TongZhiCenterBean;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 大会通知 列表界面
 * 
 * @author zhangt
 */
public class TongZhiCenterActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private TextView titleTextView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mListView;
	private TongZhiCenterAdapter mAdapter;
	private List<TongZhiCenterBean> tongZhiCenterBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tong_zhi_more);
		getData();
		initView();
	}

	private void initView() {
		mContext = this;
		backImageView = (ImageView) findViewById(R.id.custom_title_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_title_tv);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.tongzhi_swiperefreshlayout);
		mListView = (ListView) findViewById(R.id.tongzhi_lv);

		titleTextView.setText(getResources().getString(R.string.tongzhizhongxin));

		// tongZhiBeans = new ArrayList<>();
		mAdapter = new TongZhiCenterAdapter(mContext, tongZhiCenterBeans, R.layout.item_tongzhi_center_layout);
		mListView.setAdapter(mAdapter);

		backImageView.setOnClickListener(this);
		mSwipeRefreshLayout.setEnabled(false);
	}

	/**
	 * 模拟请求数据
	 */
	private void getData() {
		tongZhiCenterBeans = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			TongZhiCenterBean bean = new TongZhiCenterBean();
			bean.title = "大会紧急通知";
			bean.time = "2016.9.12 15:32:22";
			tongZhiCenterBeans.add(bean);
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
