package com.gj.gaojiaohui.activity;

import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 会议服务
 * 
 * @author Administrator
 * 
 */
public class ConferenceServiceActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView conference_service_fanhui;
	private RelativeLayout assembly_vehicle_rl;
	private RelativeLayout conference_rl;
	private RelativeLayout medical_rl;
	private RelativeLayout complaints_rl;
	private RelativeLayout vehicle_center_rl;
	private RelativeLayout hotel_rl;
	private RelativeLayout security_alarm_rl;
	private RelativeLayout fire_alarm_rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conference_service);

		initView();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.the_scene_service_telephone));
		conference_service_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		
		assembly_vehicle_rl = (RelativeLayout) findViewById(R.id.assembly_vehicle_rl);
		conference_rl = (RelativeLayout) findViewById(R.id.conference_rl);
		medical_rl = (RelativeLayout) findViewById(R.id.medical_rl);
		complaints_rl = (RelativeLayout) findViewById(R.id.complaints_rl);
		vehicle_center_rl = (RelativeLayout) findViewById(R.id.vehicle_center_rl);
		hotel_rl = (RelativeLayout) findViewById(R.id.hotel_rl);
		security_alarm_rl = (RelativeLayout) findViewById(R.id.security_alarm_rl);
		fire_alarm_rl = (RelativeLayout) findViewById(R.id.fire_alarm_rl);

		conference_service_fanhui.setOnClickListener(this);
		assembly_vehicle_rl.setOnClickListener(this);
		conference_rl.setOnClickListener(this);
		medical_rl.setOnClickListener(this);
		complaints_rl.setOnClickListener(this);
		vehicle_center_rl.setOnClickListener(this);
		hotel_rl.setOnClickListener(this);
		security_alarm_rl.setOnClickListener(this);
		fire_alarm_rl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		// 咨询接待
		case R.id.assembly_vehicle_rl:
			Intent intent1 = new Intent(this, ConferenceActivity.class);
			intent1.putExtra("teltype", "1");
			startActivity(intent1);
			break;
		// 票证咨询
		case R.id.conference_rl:
			Intent intent2 = new Intent(this, ConferenceActivity.class);
			intent2.putExtra("teltype", "2");
			startActivity(intent2);
			break;
		// 专业观众服务
		case R.id.medical_rl:
			Intent intent3 = new Intent(this, ConferenceActivity.class);
			intent3.putExtra("teltype", "3");
			startActivity(intent3);
			break;
		// 会展中心投诉电话
		case R.id.complaints_rl:
			Intent intent4 = new Intent(this, ConferenceActivity.class);
			intent4.putExtra("teltype", "4");
			startActivity(intent4);
			break;
		// 食品卫生投诉电话
		case R.id.vehicle_center_rl:
			Intent intent5 = new Intent(this, ConferenceActivity.class);
			intent5.putExtra("teltype", "5");
			startActivity(intent5);
			break;
		// 现场医疗救护点
		case R.id.hotel_rl:
			Intent intent6 = new Intent(this, ConferenceActivity.class);
			intent6.putExtra("teltype", "6");
			startActivity(intent6);
			break;
		// 治安报警
		case R.id.security_alarm_rl:
			Intent intent7 = new Intent(this, ConferenceActivity.class);
			intent7.putExtra("teltype", "7");
			startActivity(intent7);
			break;
		// 消防报警
		case R.id.fire_alarm_rl:
			Intent intent8 = new Intent(this, ConferenceActivity.class);
			intent8.putExtra("teltype", "8");
			startActivity(intent8);
			break;
		default:
			break;
		}
	}
}
