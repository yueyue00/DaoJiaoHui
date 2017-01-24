package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.smartdot.wenbo.huiyi.R;

/**
 * 服务调度的adapter
 * 
 * @author zhangt
 * 
 */
public class DiaoDuAdapter extends CommonAdapter<DiaoDuBean>{

	public DiaoDuAdapter(Context context, List<DiaoDuBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
	}

	@Override
	public void convert(ViewHolder viewHolder, DiaoDuBean bean) {
		viewHolder.setText(R.id.diaodu_name_tv, bean.name);
	}

}
