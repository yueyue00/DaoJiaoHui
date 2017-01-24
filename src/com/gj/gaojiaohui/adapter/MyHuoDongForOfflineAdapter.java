package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;

public class MyHuoDongForOfflineAdapter extends CommonAdapter<MeetingChildBeanForOffline> {
	private List<MeetingChildBeanForOffline> items = new ArrayList<MeetingChildBeanForOffline>();
	private DisplayImageOptions options;

	private Context mContext;

	public MyHuoDongForOfflineAdapter(Context context, List<MeetingChildBeanForOffline> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.mContext = context;
		this.items=items;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, MeetingChildBeanForOffline bean) {
		int position = viewHolder.getPosition();
		switch (GloableConfig.LANGUAGE_TYPE) {
		case "1":
			viewHolder.setText(R.id.three_huodong_title, bean.title);
			viewHolder.setText(R.id.three_huodong_neirong, bean.value);
			viewHolder.setText(R.id.huodong_time, bean.start_date);
			break;
		case "2":
			viewHolder.setText(R.id.three_huodong_title, bean.title_en);
			viewHolder.setText(R.id.three_huodong_neirong, bean.value_en);
			viewHolder.setText(R.id.huodong_time, bean.start_date);
			break;
		}
		
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
