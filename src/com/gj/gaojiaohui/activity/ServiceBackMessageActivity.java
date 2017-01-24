package com.gj.gaojiaohui.activity;

import com.gj.gaojiaohui.adapter.ServiceBackAdapter;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 服务反馈(留言)
 * @author Administrator
 *
 */
public class ServiceBackMessageActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private ImageView service_back_fanhui;
	private ListView service_back_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_back_message);
		
		getWindow().setSoftInputMode(
			    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
			      | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
	}

	private void initView() {
		service_back_fanhui = (ImageView) findViewById(R.id.service_back_fanhui);
		service_back_listview = (ListView) findViewById(R.id.service_back_listview);
		service_back_listview.setAdapter(new ServiceBackAdapter(this));
		service_back_fanhui.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.service_back_fanhui:
			finish();
			break;

		default:
			break;
		}
	}
}
