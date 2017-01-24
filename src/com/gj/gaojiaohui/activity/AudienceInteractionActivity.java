package com.gj.gaojiaohui.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gj.gaojiaohui.adapter.MyFragmentPagerAdapter;
import com.gj.gaojiaohui.fragment.FragmentFollowMy;
import com.gj.gaojiaohui.fragment.FragmentMyFollow;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众互动 包含关注我的/我关注的/关注用户统计 界面 add wb
 */
public class AudienceInteractionActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener {

	private Context mContext;
	private LinearLayout left_schedule_layout;
	private LinearLayout center_schedule_layout;

	private ViewPager mViewPager;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	/** viewpager的adapter */
	private MyFragmentPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audience_interaction);
		mContext = this;
		initSwitchLayout();
		initView();
	}

	/** 初始化顶部tab */
	private void initSwitchLayout() {
		ImageView search = (ImageView) findViewById(R.id.serach_img);
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		search.setOnClickListener(this);
		back.setOnClickListener(this);
		left_schedule_layout = (LinearLayout) findViewById(R.id.left_schedule_layout);
		center_schedule_layout = (LinearLayout) findViewById(R.id.center_schedule_layout);
		center_schedule_layout.setOnClickListener(this);
		left_schedule_layout.setOnClickListener(this);
		initSelectState();
		left_schedule_layout.setSelected(true);
	}

	private void initView() {
		addFragment();// 添加Fragment
		adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), (ArrayList<Fragment>) fragments);// 返回创建Manager时的对象
		mViewPager = (ViewPager) findViewById(R.id.myact_ViewPager);
		mViewPager.setAdapter(adapter);
		mViewPager.addOnPageChangeListener(this);// 设置滑动viewpager的监听事件 自身受理
	}

	/** 添加Fragment */
	private void addFragment() {
		FragmentFollowMy f1 = new FragmentFollowMy();
		FragmentMyFollow f2 = new FragmentMyFollow();

		fragments.add(f1); // 关注我的
		fragments.add(f2); // 我关注的
	}

	/** 初始化顶部tab选择状态 */
	private void initSelectState() {
		left_schedule_layout.setSelected(false);
		center_schedule_layout.setSelected(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.serach_img:
			// TODO 在这里执行跳转到搜索界面的流程
			mContext.startActivity(new Intent(mContext, AudienceInteractionSearchActivity.class));
			break;
		case R.id.left_schedule_layout:
			initSelectState();
			left_schedule_layout.setSelected(true);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.center_schedule_layout:
			initSelectState();
			center_schedule_layout.setSelected(true);
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}

	}

	public void Changed() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onPageSelected(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			initSelectState();
			left_schedule_layout.setSelected(true);
			break;
		case 1:
			initSelectState();
			center_schedule_layout.setSelected(true);
			break;
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

}
