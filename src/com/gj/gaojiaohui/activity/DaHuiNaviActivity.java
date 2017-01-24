package com.gj.gaojiaohui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dxmap.indoornavig.BaseStack;
import com.dxmap.indoornavig.BaseView;
import com.gj.gaojiaohui.fragment.FragmentDaHuiNavi;
import com.gj.gaojiaohui.fragment.FragmentZhanGuanNavi;
import com.gj.gaojiaohui.qrcode.activity.CaptureActivity;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.view.NoScrollViewPager;
import com.gj.gaojiaohui.view.PinchImageViewPager;
import com.gj.gaojiaohui.view.PinchImageViewPager.OnPageChangeListener;
import com.smartdot.wenbo.huiyi.R;

//大会导航界面---zyj
public class DaHuiNaviActivity extends GaoJiaoHuiBaseActivity {

	NoScrollViewPager dahuinavi_vp;
	RadioGroup dahuinavi_rg;
	ImageView dahuinavi_fanhui;
	FragmentDaHuiNavi dahuinavi_fm;
	FragmentZhanGuanNavi zhanguannavi_fm;
	ArrayList<Fragment> mfragments = new ArrayList<Fragment>();
	DaHuiNaviFmAdapter dahuinavi_adapter;
	private ImageView dahuinavi_erweima;
	Context mContext;
	String endfid = ""; // 设置终点 fid
	String startid = ""; // 起点 展位号
	String endid = ""; // 终点展位号
	String mapType = "outdoor";
	public static DaHuiNaviActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dahuinavi);
		mContext = DaHuiNaviActivity.this;
		instance = this;
		Intent intent = getIntent();
		try {
			mapType = intent.getStringExtra("mapType");
			endfid = intent.getStringExtra("endfid");
			startid = intent.getStringExtra("startid");
			endid = intent.getStringExtra("endid");
		} catch (NullPointerException e) {
		}
		dahuinavi_fm = new FragmentDaHuiNavi();
		zhanguannavi_fm = new FragmentZhanGuanNavi(startid, endid, endfid);
		initView();
		setAllClick();

		if (!StringUtils.isNull(mapType)) {
			if (mapType.equals("outdoor")) {
				dahuinavi_vp.setCurrentItem(0);
			} else {
				dahuinavi_vp.setCurrentItem(1);
			}
		}

	}

	private void initView() {
		dahuinavi_vp = (NoScrollViewPager) findViewById(R.id.dahuinavi_vp);
		dahuinavi_rg = (RadioGroup) findViewById(R.id.dahuinavi_rg);
		dahuinavi_fanhui = (ImageView) findViewById(R.id.dahuinavi_fanhui);
		dahuinavi_erweima = (ImageView) findViewById(R.id.dahuinavi_erweima);
		mfragments.add(dahuinavi_fm);
		mfragments.add(zhanguannavi_fm);
		dahuinavi_adapter = new DaHuiNaviFmAdapter(getSupportFragmentManager(), mfragments);
		dahuinavi_vp.setAdapter(dahuinavi_adapter);

		dahuinavi_erweima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CaptureActivity.class);
				intent.putExtra("isToMap", true);
				startActivityForResult(intent, 2001);
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void setAllClick() {
		dahuinavi_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				if (dahuinavi_rg.getChildAt(position) != null) {
					RadioButton rb = (RadioButton) dahuinavi_rg.getChildAt(position);
					rb.setChecked(true);
				} else {
					Toast.makeText(mContext, "没有这个child", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		dahuinavi_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radiobutton_dahuinavi:
					dahuinavi_vp.setCurrentItem(0);
					// switchFragment(DAHUINAVI);
					break;
				case R.id.radiobutton_zhanguannavi:
					dahuinavi_vp.setCurrentItem(1);
					// switchFragment(ZHANGUANNAVI);
					break;
				}
			}
		});
		dahuinavi_fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	class DaHuiNaviFmAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> fragments;
		FragmentManager fm;

		public DaHuiNaviFmAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
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
				// 释放界面栈
				BaseStack.UnInitBaseStack();
				// 销毁窗口操作
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, keyevent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 2001) {
				CustomToast.showToast(mContext, data.getStringExtra("qr_result"));
			}
		}
		if (requestCode != 2001) {
			for (int i = 0; i < BaseStack.GetStackSize(); i++) {
				BaseView baseview = BaseStack.GetStack(i);
				if (baseview != null) {
					boolean b = baseview.onActivityResult(requestCode, resultCode, data);
					if (b == true)
						break;
				}
			}
		}

	}

}
