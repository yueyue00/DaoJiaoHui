package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.TongZhiBean;
import com.smartdot.wenbo.huiyi.R;

public class TongZhiAdapter extends CommonAdapter<TongZhiBean> {

	public TongZhiAdapter(Context context, List<TongZhiBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
	}

	@Override
	public void convert(ViewHolder viewHolder, TongZhiBean bean) {
		viewHolder.setText(R.id.item_tongzhi_title_tv, bean.title);

	}

}
