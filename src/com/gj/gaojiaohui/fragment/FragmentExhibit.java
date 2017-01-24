package com.gj.gaojiaohui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.activity.ZhanShangSearchActivity;
import com.smartdot.wenbo.huiyi.R;

/***
 * 展商
 * 
 * @author Administrator
 * 
 */
public class FragmentExhibit extends Fragment implements OnClickListener {

	private View view;
	private ImageView zhanshang_sousuo;
	private RelativeLayout exhbit_rl;
	private RelativeLayout three_rl;
	private ImageView back_img;
	private TextView title_tv_netless;
	private TextView offline_mode_tv;
	private LinearLayout netless_linear;
	private LinearLayout netless_refresh_ll;
	private ImageView offline_mode_img;
	FragmentZhanShang fragmentZhanShang;
	FragmentHuoDong fragmentHuoDong;
	protected FragmentManager fragmentmanager;
	protected FragmentTransaction fragmenttransaction;
	public static final int ZHANSHANG = 0;
	public static final int HUODONG = 1;
	private LinearLayout linear_zhanshang;
	private LinearLayout linear_huodong;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_exhibit, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();
		linear_zhanshang.setOnClickListener(this);
		linear_huodong.setOnClickListener(this);
		initSelectState();
		initFragment();
		// isNetWork();
	}

	/** 初始化顶部tab选择状态 */
	private void initSelectState() {
		linear_zhanshang.setSelected(false);
		linear_huodong.setSelected(false);
	}

	private void initView() {
		exhbit_rl = (RelativeLayout) view.findViewById(R.id.exhbit_rl);
		three_rl = (RelativeLayout) view.findViewById(R.id.three_rl);
		offline_mode_tv = (TextView) view.findViewById(R.id.offline_mode);
		netless_linear = (LinearLayout) view.findViewById(R.id.netless_linear);
		netless_refresh_ll = (LinearLayout) view.findViewById(R.id.netless_refresh_ll);
		offline_mode_img = (ImageView) view.findViewById(R.id.netless_offline_img);
		zhanshang_sousuo = (ImageView) view.findViewById(R.id.three_sousuo);
		linear_zhanshang = (LinearLayout) view.findViewById(R.id.three_zhanshang);
		linear_huodong = (LinearLayout) view.findViewById(R.id.three_huodong);
		linear_zhanshang.setOnClickListener(this);
		linear_huodong.setOnClickListener(this);
		zhanshang_sousuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ZhanShangSearchActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initFragment() {
		// 初始化fragment
		fragmentZhanShang = new FragmentZhanShang(this);
		fragmentHuoDong = new FragmentHuoDong(this);
		// 获得管理器
		fragmentmanager = getActivity().getSupportFragmentManager();
		fragmenttransaction = fragmentmanager.beginTransaction();
		fragmenttransaction.hide(fragmentHuoDong).add(R.id.three_rl, fragmentZhanShang, "first").commit();
		linear_zhanshang.setSelected(true);
		linear_huodong.setSelected(false);
	}

	public boolean getHidden() {
		return this.getHidden();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.three_zhanshang:
			setFragment(ZHANSHANG);
			zhanshang_sousuo.setVisibility(View.VISIBLE);
			break;
		case R.id.three_huodong:
			setFragment(HUODONG);
			zhanshang_sousuo.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			if (!fragmentZhanShang.isHidden()) {
				fragmentZhanShang.isNetWork();
			}
			if (!fragmentHuoDong.isHidden()) {
				fragmentHuoDong.isNetWork();
			}
		}
		super.onHiddenChanged(hidden);
	}

	private void setFragment(int statue) {
		fragmenttransaction = fragmentmanager.beginTransaction();
		switch (statue) {
		case 0:
			if (!fragmentZhanShang.isAdded()) {
				fragmentZhanShang = new FragmentZhanShang(this);
				fragmenttransaction.hide(fragmentHuoDong).add(R.id.three_rl, fragmentZhanShang, "third").commit();
			} else {
				fragmenttransaction.hide(fragmentHuoDong).show(fragmentZhanShang).commit();
			}
			linear_zhanshang.setSelected(true);
			linear_huodong.setSelected(false);
			break;
		case 1:
			if (!fragmentHuoDong.isAdded()) {
				fragmentHuoDong = new FragmentHuoDong(this);
				fragmenttransaction.hide(fragmentZhanShang).add(R.id.three_rl, fragmentHuoDong, "third").commit();
			} else {
				fragmenttransaction.hide(fragmentZhanShang).show(fragmentHuoDong).commit();
			}
			linear_zhanshang.setSelected(false);
			linear_huodong.setSelected(true);
			break;
		}
	}

}
