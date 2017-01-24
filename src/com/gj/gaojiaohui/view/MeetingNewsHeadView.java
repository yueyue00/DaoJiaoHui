package com.gj.gaojiaohui.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gj.gaojiaohui.adapter.MeetingNewsHeadAdapter;
import com.gj.gaojiaohui.bean.MeetingNewsBean;
import com.smartdot.wenbo.huiyi.R;

/** 大会资讯 - 轮播图 */
@SuppressLint("HandlerLeak")
public class MeetingNewsHeadView {
	View headView;
	Context context;
	public ViewPager mPager;
	CirclePageIndicator mIndicator;
	MeetingNewsHeadAdapter adapter;
	List<MeetingNewsBean> data = new ArrayList<>();
	boolean isStop = false;

	@SuppressLint("InflateParams")
	public MeetingNewsHeadView(Context context) {
		headView = LayoutInflater.from(context).inflate(R.layout.scroll_head_view, null);

		mPager = (ViewPager) headView.findViewById(R.id.scroll_pager);
		adapter = new MeetingNewsHeadAdapter(context, data, mPager);
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
		// 注掉轮播
		//startAlwaysCircle(6000);
	}

	/**
	 * 刷新数据
	 */
	public void refreshData(List<MeetingNewsBean> data) {
		this.data.clear();
		if (data != null)
			this.data.addAll(data);
		adapter.refreshData();
	}

	/** 获取数据集合 */
	public List<MeetingNewsBean> getDatas() {
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
