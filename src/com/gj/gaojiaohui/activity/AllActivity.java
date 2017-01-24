package com.gj.gaojiaohui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smartdot.wenbo.huiyi.R;

public class AllActivity extends Activity implements OnClickListener {

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all);

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		button6 = (Button) findViewById(R.id.button6);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 个人设置
		case R.id.button1:
			startActivity(new Intent(this, PersonalDataActivity.class));
			break;
		// 展商资料
		case R.id.button2:
			startActivity(new Intent(this, BusinessInformationActivity.class));
			break;
		// 大会统计
		case R.id.button3:
			startActivity(new Intent(this, GeneralStatisticsActivity.class));
			break;
		// 会议服务
		case R.id.button4:
			startActivity(new Intent(this, ConferenceServiceActivity.class));
			break;
		// 我的报名
		case R.id.button5:
			startActivity(new Intent(this, MyRegistrationActivity.class));
			break;
		// 服务反馈
		case R.id.button6:
			startActivity(new Intent(this, ServiceFeedbackActivity.class));
			break;
		default:
			break;
		}
	}
}
