package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.RatingBar;

import com.gj.gaojiaohui.bean.MyFollowBean;
import com.gj.gaojiaohui.port.OnChangeMyFollowListener;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我关注的 列表的适配器
 */
public class MyFollowListAdapter extends CommonAdapter<MyFollowBean> {
	private List<MyFollowBean> items = new ArrayList<MyFollowBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public MyFollowListAdapter(Context context, List<MyFollowBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, MyFollowBean bean) {
		int position = viewHolder.getPosition();
		 viewHolder.setText(R.id.follow_name_tv, bean.name);
		 addRatingBarListener(viewHolder, bean, position);
	}

	private void addRatingBarListener(ViewHolder viewHolder, MyFollowBean bean, final int position) {
		RatingBar ratingBar = (RatingBar) viewHolder.getView(R.id.ratingBar);
		ratingBar.setRating(bean.star);
	}

	OnChangeMyFollowListener changeListener;

	/** 取消关注/关注后的回调监听,让外围界面刷新 传的id是观众的id */
	public void setOnShopAppRefreshListener(OnChangeMyFollowListener changeListener) {
		this.changeListener = changeListener;
	}
}
