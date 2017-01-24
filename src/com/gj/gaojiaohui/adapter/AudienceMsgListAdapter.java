package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.AudienceMsgBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众详情 - 留言列表 的适配器
 */
public class AudienceMsgListAdapter extends CommonAdapter<AudienceMsgBean> {
	private List<AudienceMsgBean> items = new ArrayList<AudienceMsgBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public AudienceMsgListAdapter(Context context, List<AudienceMsgBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, AudienceMsgBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.msg_value_tv, bean.value);
		viewHolder.setText(R.id.msg_date_tv, bean.date);
	}

}
