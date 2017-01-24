package com.gj.gaojiaohui.activity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 大会紧急通知-通知中心界面跳转使用
 * 
 * @author lixiaoming
 * 
 */
public class DaHuiEmergencyNoticeActivity extends Activity implements OnClickListener {
	private ImageView back_img;
	private TextView title_tv;
	private TextView notice_title_tv;
	private TextView notice_date_tv;
	private TextView notice_content_tv;
	private Context mContext;
	private String notifiContent = "";
	private String notifiTitle = "";
	private String notifiDate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_da_hui_emergency_notice);
		mContext = this;
		Intent intent = getIntent();
		notifiContent = intent.getStringExtra("notifiContent");
		notifiTitle = intent.getStringExtra("notifiTitle");
		notifiDate = intent.getStringExtra("notifiDate");
		if (TextUtils.isEmpty(notifiContent)) {
			notifiContent = "";
		}
		if (TextUtils.isEmpty(notifiTitle)) {
			notifiTitle = "";
		}
		if (TextUtils.isEmpty(notifiDate)) {
			notifiDate = "";
		}
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText("");
		notice_title_tv = (TextView) findViewById(R.id.dahui_notice_title_tv);
		notice_title_tv.setText(notifiTitle);
		notice_date_tv = (TextView) findViewById(R.id.dahui_notice_date_tv);
		notice_date_tv.setText(notifiDate);
		notice_content_tv = (TextView) findViewById(R.id.dahui_notice_content_tv);
		notice_content_tv.setText(notifiContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		}
	}
}
