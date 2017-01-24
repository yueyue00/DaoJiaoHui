package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.bean.MessageDetailsChildBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackDetailChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyLiuYanAdapter extends CommonAdapter<MessageDetailsChildBean> {

	private List<MessageDetailsChildBean> items = new ArrayList<MessageDetailsChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public MyLiuYanAdapter(Context context,
			List<MessageDetailsChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, MessageDetailsChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder.setText(R.id.liuyan_title, bean.name);
		viewHolder.setText(R.id.liuyan_time, bean.date);
		viewHolder.setText(R.id.liuyan_neirong, bean.value);
	}

}
