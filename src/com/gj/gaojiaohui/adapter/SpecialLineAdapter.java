package com.gj.gaojiaohui.adapter;

import java.util.List;

import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 大会车辆的详情
 * 
 * @author Administrator
 * 
 */
public class SpecialLineAdapter extends CommonAdapter<String> {

	RelativeLayout point_start_rl;
	RelativeLayout point_middle_rl;
	RelativeLayout point_end_rl;
	private int count;

	public SpecialLineAdapter(Context context, List<String> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		count = datas.size();
	}

	@Override
	public void convert(ViewHolder viewHolder, String bean) {
		viewHolder.setText(R.id.route_tv, bean);
		point_start_rl = viewHolder.getView(R.id.point_start_rl);
		point_middle_rl = viewHolder.getView(R.id.point_middle_rl);
		point_end_rl = viewHolder.getView(R.id.point_end_rl);

		if (viewHolder.getPosition() == 0) {
			point_start_rl.setVisibility(View.VISIBLE);
			point_middle_rl.setVisibility(View.GONE);
			point_end_rl.setVisibility(View.GONE);
		} else if (viewHolder.getPosition() == count - 1) {
			point_start_rl.setVisibility(View.GONE);
			point_middle_rl.setVisibility(View.GONE);
			point_end_rl.setVisibility(View.VISIBLE);
		} else {
			point_start_rl.setVisibility(View.GONE);
			point_middle_rl.setVisibility(View.VISIBLE);
			point_end_rl.setVisibility(View.GONE);
		}

	}

}
