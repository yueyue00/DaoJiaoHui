package com.dxmap.indoornavig;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements OnClickListener{

	private FrameLayout mMainView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mMainView  = (FrameLayout) findViewById(R.id.main_view);
		BaseStack.InitBaseStack(mMainView, this);
		BaseView.InitBaseView(mMainView, this);
		IndoorNaviView v = (IndoorNaviView) BaseStack.CreateViewAndAddView(IndoorNaviView.class, R.layout.view_indoorplan);
		//设置离线地图的路径，若传null则为默认路径   SD卡/dxmap/
		v.initMap(null);
		//设置起点  参数：展号num
		v.setStartPos("1");
		//设置终点 参数：展号num
	//	v.setEndPos("3D82");
		String fid = getIntent().getStringExtra("fid");
		//亮点模式下 单独设置终点接口 参数：fid
		if (fid != null)
			v.setHigthlightEndPos(fid);
		BaseStack.SetContentView(v);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	//	mHomeView.onDestoryMap();
		super.onDestroy();

	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
		BaseView baseview = BaseStack.GetTopStack();
		if (baseview != null) {
			if (baseview.onKeyDown(keyCode, keyevent) == true)
				return true;
		}
		if (BaseStack.GetStackSize() <= 1) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				BaseStack.UnInitBaseStack();
				//销毁窗口操作
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, keyevent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		for (int i = 0; i < BaseStack.GetStackSize(); i++) {
			BaseView baseview = BaseStack.GetStack(i);
			if (baseview != null) {
				boolean b = baseview.onActivityResult(requestCode, resultCode, data);
				if (b == true)
					break;
			}
		}
	}
	
	/**
	 * activity开始与用户交互时调用
	 * （无论是启动还是重新启动一个活动，该方法总是被调用的）。
	 */
	@Override
	protected void onResume() {
		BaseView baseview = BaseStack.GetTopStack();
		if (baseview != null) {
			baseview.onResume();
		}
		super.onResume();
	}
}
