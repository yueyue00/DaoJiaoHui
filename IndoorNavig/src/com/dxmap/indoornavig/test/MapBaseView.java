package com.dxmap.indoornavig.test;

import com.dxmap.indoornavig.BaseView;
import com.dxmap.indoornavig.test.UiMap.OnMapGroupSwitchListener;
import com.dxmap.indoornavig.test.UiMap.OnMapLoadSuccessListener;

import android.view.View;

public class MapBaseView extends BaseView implements OnMapLoadSuccessListener,OnMapGroupSwitchListener{
	
	@Override
	public boolean onCreateView(View view) {
		// TODO Auto-generated method stub
		super.onCreateView(view);
		return true;
	}
	
	

	@Override
	public void onMapLoadSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapGroupSwitch(boolean isSingle) {
		// TODO Auto-generated method stub
		
	}

}
