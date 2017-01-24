package com.dxmap.indoornavig.test;

import com.dxmap.indoornavig.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

public class Test2Activity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fragment);
		LinearLayout map1 = (LinearLayout) findViewById(R.id.map1);
		 map1.addView(new TestMapView(this).getView());
	}

}
