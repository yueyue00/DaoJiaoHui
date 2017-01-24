package com.gj.gaojiaohui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gheng.exhibit.http.body.response.News_LieBiao;
import com.gj.gaojiaohui.adapter.ScrollHeadAdapter;
import com.gj.gaojiaohui.bean.HomePageNewsListBean;
import com.gj.gaojiaohui.bean.HomePageNewsTopBean;
import com.smartdot.wenbo.huiyi.R;

/** 轮播图 */
public class ScrollHeadView {

	View headView;
	Context context;
	public ViewPager mPager;
	CirclePageIndicator mIndicator;
	ScrollHeadAdapter adapter;
	List<HomePageNewsTopBean> data = new ArrayList<>();
	boolean isStop = false;

	public ScrollHeadView(Context context) {
		headView = LayoutInflater.from(context).inflate(R.layout.scroll_head_view, null);

		mPager = (ViewPager) headView.findViewById(R.id.scroll_pager);
		adapter = new ScrollHeadAdapter(context, data, mPager);
		mPager.setAdapter(adapter);
		mIndicator = (CirclePageIndicator) headView.findViewById(R.id.scroll_indicator);
		mIndicator.setViewPager(mPager);
		final float density = context.getResources().getDisplayMetrics().density;
		mIndicator.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		mIndicator.setRadius(3 * density);// 点的大小
		mIndicator.setPageColor(context.getResources().getColor(R.color.gray_color));// 未选中时的显示填充颜色
		mIndicator.setFillColor(context.getResources().getColor(R.color.white));// 选中时的填充颜色
		mIndicator.setStrokeColor(context.getResources().getColor(R.color.gray_color));// 未选中时的Stroke
		mIndicator.setStrokeWidth(1);
		startAlwaysCircle(6000);

		// mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
		//
		// @Override
		// public void onPageSelected(int arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int status) {
		//
		// switch (status) {
		//
		// case 1:// 手势滑动
		// break;
		// case 2:// 界面切换
		// break;
		// case 0:// 滑动结束
		// if (mPager.getAdapter().getCount() != 1) {
		// if (mPager.getCurrentItem() == mPager.getAdapter().getCount() - 1) {
		// mPager.setCurrentItem(1, false);
		// }
		// // 当前为第一张，此时从左向右滑，则切换到最后一张
		// else if (mPager.getCurrentItem() == 0) {
		// mPager.setCurrentItem(mPager.getAdapter().getCount() - 2, false);
		// }
		// }
		// break;
		//
		// }
		// }
		// });

	}

	/**
	 * 刷新数据
	 */
	public void refreshData(List<HomePageNewsTopBean> data) {
		this.data.clear();
		if (data != null)
			this.data.addAll(data);
		adapter.refreshData();
	}

	/** 获取数据集合 */
	public List<HomePageNewsTopBean> getDatas() {
		return data;
	}

	/**
	 * 获取返回的view
	 */
	public View getView() {
		return headView;
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int currentIndex = mPager.getCurrentItem();
			currentIndex++;
			if (currentIndex >= adapter.getCount())
				currentIndex = 0;
			mPager.setCurrentItem(currentIndex);
		}
	};

	/**
	 * 开启无限循环
	 */
	private void startAlwaysCircle(final long speed) {
		new Thread() {
			public void run() {
				while (!isStop) {
					try {
						Thread.sleep(speed);
						mHandler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

	/** 获取Viewpager */
	public ViewPager getViewPager() {
		return mPager;
	}

	/** 结束循环 */
	public void setStop() {
		isStop = false;
	}
}
