package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.bean.ActivityListChildBean;
import com.gj.gaojiaohui.bean.MettingDetailsChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGuestAdapter extends CommonAdapter<MettingDetailsChildBean> {
	private List<ActivityListChildBean> items = new ArrayList<ActivityListChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public MyGuestAdapter(Context context, List<MettingDetailsChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptionsForCirclePic(R.drawable.pic_vip, 40);
	}

	@Override
	public void convert(ViewHolder viewHolder, MettingDetailsChildBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.guest_name, bean.name);
		viewHolder.setText(R.id.guest_position, bean.position);
		ImageView imageView = (ImageView) viewHolder.getView(R.id.guest_head);
		ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
	}
}
