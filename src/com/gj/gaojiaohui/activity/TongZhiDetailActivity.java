package com.gj.gaojiaohui.activity;

import com.gj.gaojiaohui.utils.CustomToast;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通知详情
 * 
 * @author zhangt
 * 
 */
public class TongZhiDetailActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView tongzhi_detail_title;
	private TextView tongzhi_detail_time;
	private TextView tongzhi_detail_contain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tong_zhi_detail);
		initView();
	}

	private void initView() {
		mContext = this;
		backImageView = (ImageView) findViewById(R.id.custom_title_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_title_tv);
		tongzhi_detail_title = (TextView) findViewById(R.id.tongzhi_detail_title);
		tongzhi_detail_time = (TextView) findViewById(R.id.tongzhi_detail_time);
		tongzhi_detail_contain = (TextView) findViewById(R.id.tongzhi_detail_contain);

		titleTextView.setText("");

		backImageView.setOnClickListener(this);
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
