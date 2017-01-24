package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;

import com.gj.gaojiaohui.fragment.FragmentMyFollow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/** 管理成功/未成功 ViewPager的Adapter */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	// viewpager 的数据就是fragment
	ArrayList<Fragment> list;

	public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		if (list == null) {
			this.list = new ArrayList<Fragment>();
		} else {
			this.list = list;
		}
	}
	
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		// 当判断滑到我的关注界面时调用这个界面里的getData请求数据方法进行刷新
		if(object instanceof FragmentMyFollow){
			((FragmentMyFollow) object).getData();
			 return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

}
