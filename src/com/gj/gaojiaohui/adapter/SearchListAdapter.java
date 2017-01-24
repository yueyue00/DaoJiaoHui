package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.bean.MeetingChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 日程列表搜索的适配器
 */
public class SearchListAdapter extends CommonAdapter<MeetingChildBean> {
	private List<MeetingChildBean> items = new ArrayList<MeetingChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public SearchListAdapter(Context context, List<MeetingChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, MeetingChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder.setText(R.id.richeng_meeting_name_tv, bean.meeting_title);
		viewHolder.setText(R.id.richeng_meeting_date_tv, bean.meeting_site);
		viewHolder.setText(R.id.richeng_meeting_compere_tv, bean.meeting_compere);
	}

}
