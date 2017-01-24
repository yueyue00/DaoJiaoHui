package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.LiangdianExhibitBean;
import com.smartdot.wenbo.huiyi.R;

/**
 * 亮点推荐/重点线路 点进去之后 展商列表的Adapter
 * 
 * @author zhangt
 * 
 */
public class LiangdianExhibitAdapter extends CommonAdapter<LiangdianExhibitBean> {

	public LiangdianExhibitAdapter(Context context, List<LiangdianExhibitBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
	}

	@Override
	public void convert(ViewHolder viewHolder, LiangdianExhibitBean bean) {
		viewHolder.setText(R.id.exhibit_detail_detail_tv, bean.name);

	}

}
