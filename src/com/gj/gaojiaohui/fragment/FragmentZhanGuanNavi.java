package com.gj.gaojiaohui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dxmap.indoornavig.BaseStack;
import com.dxmap.indoornavig.BaseView;
import com.dxmap.indoornavig.IndoorHomeView;
import com.dxmap.indoornavig.IndoorNaviView;
import com.gj.gaojiaohui.utils.StringUtils;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.string;

//展馆导航模块
public class FragmentZhanGuanNavi extends Fragment {

	/** 室内导航的view */
	private FrameLayout mMainView;
	IndoorNaviView v;
	private String stratid;
	private String endid;
	private String endFid;

	public FragmentZhanGuanNavi(String startid, String endid, String endfid) {
		this.stratid = startid;
		this.endid = endid;
		this.endFid = endfid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_zhanguannavi, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {

		mMainView = (FrameLayout) view.findViewById(R.id.mMainView);
		// 在需要显示地图的页面加一个mMainView （必须FrameLayout）用来显示地图。
		BaseStack.InitBaseStack(mMainView, getActivity());
		BaseView.InitBaseView(mMainView, getActivity());

		v = (IndoorNaviView) BaseStack.CreateViewAndAddView(IndoorNaviView.class, R.layout.view_indoorplan);
		// 设置离线地图的路径，若传null则为默认路径 SD卡/dxmap/
		v.initMap(null);
		// v.setEndPos("3D82");
		if (!StringUtils.isNull(endFid)) {
			v.setHigthlightEndPos(endFid);
		} else if (!StringUtils.isNull(stratid)) {
			v.setStartPos(stratid);
		} else if (!StringUtils.isNull(endid)) {
			v.setEndPos(endid);
		}
		BaseStack.SetContentView(v);

	}

}
