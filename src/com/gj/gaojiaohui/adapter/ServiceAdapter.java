package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.bean.AssemblyVehicleChildBean;
import com.gj.gaojiaohui.bean.ConferenceServiceChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceAdapter extends CommonAdapter<ConferenceServiceChildBean> {

	private List<ConferenceServiceChildBean> items = new ArrayList<ConferenceServiceChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public ServiceAdapter(Context context,
			List<ConferenceServiceChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, ConferenceServiceChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder.setText(R.id.service_name, bean.tel_number);
		viewHolder.setText(R.id.service_number, bean.tel);
	}
}
