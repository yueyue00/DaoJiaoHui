package com.dxmap.indoornavig;

import com.fengmap.android.FMMapSDK;

import android.app.Application;

public class MainApplication extends Application {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FMMapSDK.init(this);   //≥ı ºªØSDK 
		super.onCreate();
	}

}
