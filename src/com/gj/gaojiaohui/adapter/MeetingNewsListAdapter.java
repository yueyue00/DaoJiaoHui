package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.MeetingNewsBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会咨询列表的适配器
 */
public class MeetingNewsListAdapter extends CommonAdapter<MeetingNewsBean> {

	private DisplayImageOptions options;

	private Context mContext;

	public MeetingNewsListAdapter(Context context, List<MeetingNewsBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, MeetingNewsBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.item_home_list_title, bean.title);
		viewHolder.setText(R.id.item_home_list_contain, bean.value);
		ImageView imageView = (ImageView) viewHolder.getView(R.id.item_home_list_img);
		ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
	}

}
