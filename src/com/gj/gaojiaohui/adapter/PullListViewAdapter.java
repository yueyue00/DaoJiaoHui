package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.HomePageNewsListBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * 首页新闻
 * @author zhangt
 *
 */
public class PullListViewAdapter extends CommonAdapter<HomePageNewsListBean> {
	
	private DisplayImageOptions options;

	public PullListViewAdapter(Context context, List<HomePageNewsListBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, HomePageNewsListBean bean) {
		viewHolder.setText(R.id.item_home_list_title, bean.title.toString());
		viewHolder.setText(R.id.item_home_list_contain, bean.value.toString());
		ImageView imageView = (ImageView) viewHolder.getView(R.id.item_home_list_img);
		ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
	}

}
