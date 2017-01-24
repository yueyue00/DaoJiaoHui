package com.gj.gaojiaohui.activity;

import com.smartdot.wenbo.huiyi.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 搜索页面
 * @author Administrator
 *
 */
public class SouSuoActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	TextView three_quxiao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sou_suo);
		
		three_quxiao = (TextView) findViewById(R.id.three_quxiao);
		
		three_quxiao.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.three_quxiao:
			finish();
			break;
		default:
			break;
		}
	}
}
