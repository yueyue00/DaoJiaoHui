package com.dxmap.indoornavig.test;

import java.util.ArrayList;
import java.util.List;

import com.dxmap.indoornavig.BaseStack;
import com.dxmap.indoornavig.BaseView;
import com.dxmap.indoornavig.MainActivity;
import com.dxmap.indoornavig.NaviSearchActivity;
import com.dxmap.indoornavig.R;
import com.dxmap.indoornavig.test.ViewMapPos.OnMapLoadFinishListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class TestActivity extends FragmentActivity implements OnPageChangeListener,OnMapLoadFinishListener{
	
	private ViewPager mViewPager;
	private List<Fragment> mFragmentList ;
    private MyFragmentPager mAdapter;
    public static UiMap mUIMap;
    private boolean is = true;
    private TestMapView mTestMap;
    private ProgressDialog mDialog;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fragment);
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TestActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Intent intent = new Intent(TestActivity.this,NaviSearchActivity.class);
				intent.putStringArrayListExtra("data", new ArrayList<String>());
				startActivity(intent);
			}
		});
		final LinearLayout map1 = (LinearLayout) findViewById(R.id.map1);
		
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * if (is) { view.setVisibility(View.GONE); is = false; }else{
				 * view.setVisibility(View.VISIBLE); is = true; }
				 */

				/*if (view.getVisibility() == View.GONE)
					view.setVisibility(View.VISIBLE);
				else
					view.setVisibility(View.GONE);*/
				
				mDialog = ProgressDialog.show(TestActivity.this, null, "ит╨С...");
				mTestMap = new TestMapView(TestActivity.this);
				final View view = mTestMap.getView();
				mTestMap.setListener(TestActivity.this);
				map1.addView(view);
				view.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						view.setVisibility(View.GONE);
					}
				});
			}
		});
		
	//	View v1 = View.inflate(this, R.layout.view_map, null);
	//	LinearLayout map1 = (LinearLayout) findViewById(R.id.map1);
	//	 map1.addView(new TestMapView(this).getView());
	//	LinearLayout map2 = (LinearLayout) findViewById(R.id.map2);
		// map2.addView(new TestMapView(this).getView());
		/*BaseStack.InitBaseStack(null, this);
		BaseView.InitBaseView(null, this);
		mUIMap = (UiMap) BaseStack.CreateView(UiMap.class, R.layout.view_map);
		mUIMap.initMap(null);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mFragmentList = new ArrayList<Fragment>();
		FragmentOne one = new FragmentOne();
		FragmentTwo two = new FragmentTwo();
		mFragmentList.add(one);
		mFragmentList.add(two);
		mAdapter = new MyFragmentPager(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(this);*/
	}
	
	class MyFragmentPager extends FragmentPagerAdapter{

		public MyFragmentPager(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mFragmentList.size();
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
	//	mViewPager.setCurrentItem(arg0);
	}

	@Override
	public void onMapLoadSuccess(String path) {
		// TODO Auto-generated method stub
		if (mDialog != null)
			mDialog.dismiss();
	}

	@Override
	public void onMapLoadFaile(String path) {
		// TODO Auto-generated method stub
		if (mDialog != null)
			mDialog.dismiss();
	}
}
