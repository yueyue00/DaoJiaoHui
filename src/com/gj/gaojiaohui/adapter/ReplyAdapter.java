package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.ReplyBean;
import com.smartdot.wenbo.huiyi.R;

/**
 * 点评回复的adapter
 * 
 * @author zhangt
 * 
 */
public class ReplyAdapter extends CommonAdapter<ReplyBean> {

	public ReplyAdapter(Context context, List<ReplyBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
	}

	@Override
	public void convert(ViewHolder viewHolder, ReplyBean bean) {
		viewHolder.setText(R.id.reply_name_tv, bean.name);
		viewHolder.setText(R.id.reply_reply_tv, bean.value);
	}

}
