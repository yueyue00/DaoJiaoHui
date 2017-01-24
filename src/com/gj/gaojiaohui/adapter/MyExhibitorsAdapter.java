package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.bean.ActivityListChildBean;
import com.gj.gaojiaohui.bean.HuiZhanDetailsChildBean;
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

public class MyExhibitorsAdapter extends CommonAdapter<HuiZhanDetailsChildBean> {
	private List<HuiZhanDetailsChildBean> items = new ArrayList<HuiZhanDetailsChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public MyExhibitorsAdapter(Context context, List<HuiZhanDetailsChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptionsForCirclePic(R.drawable.dianping_img_default_nor, 0);
	}

	@Override
	public void convert(ViewHolder viewHolder, HuiZhanDetailsChildBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.exhibitors_name, bean.name);
		ImageView imageView = (ImageView) viewHolder.getView(R.id.exhibitors_logo);
		ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
	}
}
