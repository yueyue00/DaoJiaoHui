package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.FollowBean;
import com.gj.gaojiaohui.port.OnChangeFollowListener;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的关注列表的适配器
 */
public class LineStepsAdapter extends CommonAdapter<FollowBean> {
	private List<FollowBean> items = new ArrayList<FollowBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public LineStepsAdapter(Context context, List<FollowBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = datas;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, FollowBean bean) {
		final int position = viewHolder.getPosition();
//		viewHolder.setText(R.id.follow_name_tv, bean.name);
//		viewHolder.setText(R.id.follow_value_tv, bean.value);
//		ImageView imageView = (ImageView) viewHolder.getView(R.id.follow_img);
//		imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO 这部分做取消关注的操作
//				changeListener.OnRefresh(bean.id);
//			}
//		});
	}

	OnChangeFollowListener changeListener;

	/** 取消关注后的回调监听,让外围界面刷新 */
	public void setOnShopAppRefreshListener(OnChangeFollowListener changeListener) {
		this.changeListener = changeListener;
	}
}
