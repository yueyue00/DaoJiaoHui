package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.ServiceFeedbackChildBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackDetailChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class ServiceFeedbackDetailAdapter extends
		CommonAdapter<ServiceFeedbackDetailChildBean> {

	private List<ServiceFeedbackDetailChildBean> items = new ArrayList<ServiceFeedbackDetailChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public ServiceFeedbackDetailAdapter(Context context,
			List<ServiceFeedbackDetailChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder,
			ServiceFeedbackDetailChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder
				.setText(R.id.service_feedback_detail_item_name_tv, bean.name);
		viewHolder
				.setText(R.id.service_feedback_detail_item_date_tv, bean.date);
		viewHolder.setText(R.id.service_feedback_detail_item_liuyan_content_tv,
				bean.value);
		ImageView imageView = (ImageView) viewHolder
				.getView(R.id.service_feedback_detail_item_icon_img);
		// ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
	}

}
