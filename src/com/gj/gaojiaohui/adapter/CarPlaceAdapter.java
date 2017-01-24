package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gj.gaojiaohui.activity.DaHuiNaviActivity;
import com.gj.gaojiaohui.bean.AssemblyVehicleChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

public class CarPlaceAdapter extends CommonAdapter<AssemblyVehicleChildBean> {

	private List<AssemblyVehicleChildBean> items = new ArrayList<AssemblyVehicleChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public CarPlaceAdapter(Context context, List<AssemblyVehicleChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, final AssemblyVehicleChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder.setText(R.id.car_num, bean.name);
		RelativeLayout place_rl = (RelativeLayout) viewHolder.getView(R.id.place_rl);
		int last = bean.approach.size() - 1;
		if (bean.approach.size() == 0) {
			place_rl.setVisibility(View.GONE);
		} else {
			place_rl.setVisibility(View.VISIBLE);
			viewHolder.setText(R.id.qidian, bean.approach.get(0));
			viewHolder.setText(R.id.zhongdian, bean.approach.get(last));
		}
		ImageView imageView = (ImageView) viewHolder.getView(R.id.weizhi_image);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DaHuiNaviActivity.class);
				intent.putExtra("latitude", bean.latitude);
				intent.putExtra("longitude", bean.longitude);
				intent.putExtra("address", bean.name);
				mContext.startActivity(intent);	
			}
		});
	}

}
