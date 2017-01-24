package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.InfoBean;
import com.gj.gaojiaohui.bean.TongZhiCenterBean;
import com.smartdot.wenbo.huiyi.R;

public class TongZhiCenterAdapter extends CommonAdapter<TongZhiCenterBean> {

	public TongZhiCenterAdapter(Context context, List<TongZhiCenterBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
	}

	@Override
	public void convert(ViewHolder viewHolder, TongZhiCenterBean bean) {
		viewHolder.setText(R.id.item_tongzhi_title_tv, bean.title);
		viewHolder.setText(R.id.item_tongzhi_time_tv, bean.time);
	}

}
