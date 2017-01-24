package com.gj.gaojiaohui.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.gj.gaojiaohui.adapter.ServiceFeedbackAdapter;
import com.smartdot.wenbo.huiyi.R;

/**
 * 服务反馈
 * @author Administrator
 *
 */
public class ServiceFeedbackActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private ImageView service_feedback_fanhui;
	private ListView service_feedback_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_feedback);
		
		initView();
	}

	private void initView() {
		service_feedback_fanhui = (ImageView) findViewById(R.id.service_feedback_fanhui);
		service_feedback_listview = (ListView) findViewById(R.id.service_feedback_listview);
		service_feedback_listview.setAdapter(new ServiceFeedbackAdapter(this));
		service_feedback_fanhui.setOnClickListener(this);
		service_feedback_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(ServiceFeedbackActivity.this,ServiceBackMessageActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.service_feedback_fanhui:
			finish();
			break;

		default:
			break;
		}
	}
}
