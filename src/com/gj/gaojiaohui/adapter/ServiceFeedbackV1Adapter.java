package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.a.a.be;

import com.gj.gaojiaohui.bean.FollowBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class ServiceFeedbackV1Adapter extends
		CommonAdapter<ServiceFeedbackChildBean> {

	private List<ServiceFeedbackChildBean> items = new ArrayList<ServiceFeedbackChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public ServiceFeedbackV1Adapter(Context context,
			List<ServiceFeedbackChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, ServiceFeedbackChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder.setText(R.id.service_feedback_item_name_tv, bean.name);
		TextView new_liuyan_tv = (TextView) viewHolder
				.getView(R.id.service_feedback_item_new_liuyan_tv);
		if (!bean.state) {
			new_liuyan_tv.setVisibility(View.VISIBLE);
		} else {
			new_liuyan_tv.setVisibility(View.GONE);
		}
		ImageView imageView = (ImageView) viewHolder
				.getView(R.id.service_feedback_item_icon_img);
		// ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
	}

}
