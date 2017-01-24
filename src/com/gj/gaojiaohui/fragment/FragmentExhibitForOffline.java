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

import com.gj.gaojiaohui.activity.ExhibitorSearchForOfflineActivity;
import com.smartdot.wenbo.huiyi.R;

public class FragmentExhibitForOffline extends Fragment implements OnClickListener {
	private View view;
	private ImageView zhanshang_sousuo;
	private RelativeLayout exhbit_rl;
	FragmentZhanShangForOffline fragmentZhanShangForOffline;
	FragmentHuoDongForOffline fragmentHuoDongForOffline;
	protected FragmentManager fragmentmanager;
	protected FragmentTransaction fragmenttransaction;
	public static final int ZHANSHANG = 0;
	public static final int HUODONG = 1;
	private LinearLayout linear_zhanshang;
	private LinearLayout linear_huodong;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_exhibit_for_offline, container, false);
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
	}

	/** 初始化顶部tab选择状态 */
	private void initSelectState() {
		linear_zhanshang.setSelected(false);
		linear_huodong.setSelected(false);
	}

	private void initView() {
		exhbit_rl = (RelativeLayout) view.findViewById(R.id.exhbit_offline_rl);
		zhanshang_sousuo = (ImageView) view.findViewById(R.id.three_sousuo_offline);
		linear_zhanshang = (LinearLayout) view.findViewById(R.id.three_zhanshang_offline);
		linear_huodong = (LinearLayout) view.findViewById(R.id.three_huodong_offline);
		zhanshang_sousuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ExhibitorSearchForOfflineActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initFragment() {
		// 初始化fragment
		fragmentZhanShangForOffline = new FragmentZhanShangForOffline();
		fragmentHuoDongForOffline = new FragmentHuoDongForOffline();
		// 获得管理器
		fragmentmanager = getActivity().getSupportFragmentManager();
		fragmenttransaction = fragmentmanager.beginTransaction();
		fragmenttransaction.hide(fragmentHuoDongForOffline).add(R.id.three_rl_offline, fragmentZhanShangForOffline, "first").commit();
		linear_zhanshang.setSelected(true);
		linear_huodong.setSelected(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.three_zhanshang_offline:
			setFragment(ZHANSHANG);
			zhanshang_sousuo.setVisibility(View.VISIBLE);
			break;
		case R.id.three_huodong_offline:
			setFragment(HUODONG);
			zhanshang_sousuo.setVisibility(View.GONE);
			break;
		}
	}

	private void setFragment(int statue) {
		fragmenttransaction = fragmentmanager.beginTransaction();
		switch (statue) {
		case 0:
			if (!fragmentZhanShangForOffline.isAdded()) {
				fragmentZhanShangForOffline = new FragmentZhanShangForOffline();
				fragmenttransaction.hide(fragmentHuoDongForOffline).add(R.id.three_rl_offline, fragmentZhanShangForOffline, "third").commit();
			} else {
				fragmenttransaction.hide(fragmentHuoDongForOffline).show(fragmentZhanShangForOffline).commit();
			}
			linear_zhanshang.setSelected(true);
			linear_huodong.setSelected(false);

			break;
		case 1:
			if (!fragmentHuoDongForOffline.isAdded()) {
				fragmentHuoDongForOffline = new FragmentHuoDongForOffline();
				fragmenttransaction.hide(fragmentZhanShangForOffline).add(R.id.three_rl_offline, fragmentHuoDongForOffline, "third").commit();
			} else {
				fragmenttransaction.hide(fragmentZhanShangForOffline).show(fragmentHuoDongForOffline).commit();
			}
			linear_zhanshang.setSelected(false);
			linear_huodong.setSelected(true);
			break;
		}
	}
}
