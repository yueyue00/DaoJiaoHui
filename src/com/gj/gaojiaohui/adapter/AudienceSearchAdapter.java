package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.FollowMyBean;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众互动 搜索界面 适配器
 */
public class AudienceSearchAdapter extends CommonAdapter<FollowMyBean> {
	private List<FollowMyBean> items = new ArrayList<FollowMyBean>();

	private Context mContext;

	public AudienceSearchAdapter(Context context, List<FollowMyBean> list, int itemLayoutResId) {
		super(context, list, itemLayoutResId);
		this.mContext = context;
	}

	@Override
	public void convert(ViewHolder viewHolder, FollowMyBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.follow_name_tv, bean.name);
	}

}
