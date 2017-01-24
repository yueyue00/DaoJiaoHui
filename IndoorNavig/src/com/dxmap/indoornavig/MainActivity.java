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
		//�������ߵ�ͼ��·��������null��ΪĬ��·��   SD��/dxmap/
		v.initMap(null);
		//�������  ������չ��num
		v.setStartPos("1");
		//�����յ� ������չ��num
	//	v.setEndPos("3D82");
		String fid = getIntent().getStringExtra("fid");
		//����ģʽ�� ���������յ�ӿ� ������fid
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
				//���ٴ��ڲ���
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
	 * activity��ʼ���û�����ʱ����
	 * ������������������������һ������÷������Ǳ����õģ���
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
