package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.LiangDianBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class LiangDianAdapter extends CommonAdapter<LiangDianBean> {

	private DisplayImageOptions options;

	public LiangDianAdapter(Context context, List<LiangDianBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, LiangDianBean bean) {
		viewHolder.setText(R.id.item_liangdian_title, bean.title);
	}

}
