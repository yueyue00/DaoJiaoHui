package com.dxmap.indoornavig.test;

import com.dxmap.indoornavig.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FragmentOne extends Fragment {
	
	private LinearLayout mLayout;
	private boolean is = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_one, null);
		Button button = (Button) v.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is) {
				//	mLayout.removeView(TestActivity.mUIMap.GetView());
					is = false;
				}else{
				//	mLayout.addView(TestActivity.mUIMap.GetView());
					is = true;
				}
			}
		});
		mLayout = (LinearLayout) v.findViewById(R.id.map);
		return v;
	}

}
