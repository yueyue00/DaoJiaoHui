package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.bean.ActivityListChildBean;
import com.gj.gaojiaohui.bean.AudienceMsgBean;
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

public class MyHuoDongAdapter extends CommonAdapter<ActivityListChildBean> {

	private DisplayImageOptions options;

	private Context mContext;

	public MyHuoDongAdapter(Context context, List<ActivityListChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, ActivityListChildBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.three_huodong_title, bean.title);
		viewHolder.setText(R.id.three_huodong_neirong, bean.value);
		viewHolder.setText(R.id.huodong_time, bean.date);
		// ImageView imageView = (ImageView)
		// viewHolder.getView(R.id.three_huodong_image);
		// if(bean.pic.equals("")){
		// imageView.setVisibility(View.GONE);
		// }else{
		// imageView.setVisibility(View.VISIBLE);
		// ImageLoader.getInstance().displayImage(bean.pic, imageView, options);
		// }
	}
}
