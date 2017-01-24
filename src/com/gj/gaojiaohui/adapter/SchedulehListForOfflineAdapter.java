package com.gj.gaojiaohui.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;
import com.smartdot.wenbo.huiyi.R;

/**
 * 离线日程列表的适配器
 */
public class SchedulehListForOfflineAdapter extends CommonAdapter<MeetingChildBeanForOffline> {
	private List<MeetingChildBeanForOffline> items = new ArrayList<MeetingChildBeanForOffline>();
	private Context mContext;

	public SchedulehListForOfflineAdapter(Context context, List<MeetingChildBeanForOffline> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
	}

	@Override
	public void convert(ViewHolder viewHolder, MeetingChildBeanForOffline bean) {
		final int position = viewHolder.getPosition();
		switch (GloableConfig.LANGUAGE_TYPE) {
		case "1":
			viewHolder.setText(R.id.richeng_meeting_name_tv, bean.title.trim());
			viewHolder.setText(R.id.richeng_meeting_date_tv, getSite(bean));
			break;
		case "2":
			viewHolder.setText(R.id.richeng_meeting_name_tv, bean.title_en.trim());
			viewHolder.setText(R.id.richeng_meeting_date_tv, getSite(bean));
			break;
		}
	}

	/** 拼出时间地点的样式 */
	private String getSite(MeetingChildBeanForOffline bean) {
		String start_date = bean.start_date.trim();
		String end_date = bean.end_date.trim();
		String value = null;
		switch (GloableConfig.LANGUAGE_TYPE) {
		case "1":
			value = start_date + " " + mContext.getResources().getString(R.string.to) + " " + end_date + "，" + bean.site;
			break;
		case "2":
			value = start_date + " " + mContext.getResources().getString(R.string.to) + " " + end_date + "，" + bean.site_en;
			break;
		}
		return value;
	}

	// /** 拼出时间地点的样式 采用美观的上下午表现方式 */
	// private String getSite(MeetingChildBeanForOffline bean) {
	// String date = bean.start_date.substring(0, 10).trim();
	// String start_date =
	// bean.start_date.substring(bean.start_date.indexOf(" "),
	// bean.start_date.length()).trim();
	// String end_date = bean.end_date.substring(bean.end_date.indexOf(" "),
	// bean.end_date.length()).trim();
	// String AM_PM = getAmOrPm(bean.start_date).trim();
	// String value = null;
	// switch (GloableConfig.LANGUAGE_TYPE) {
	// case "1":
	// value = date + " " + AM_PM + " " + start_date +
	// mContext.getResources().getString(R.string.to) + " " + end_date + "，" +
	// bean.site;
	// break;
	// case "2":
	// value = date + " " + AM_PM + " " + start_date +
	// mContext.getResources().getString(R.string.to) + " " + end_date + "，" +
	// bean.site_en;
	// break;
	// }
	// return value;
	// }

	/** 判断上午还是下午 */
	@SuppressWarnings("deprecation")
	private String getAmOrPm(String data) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date starttimeDate = sdf.parse(data);
			if (starttimeDate.getHours() < 12) {
				return mContext.getResources().getString(R.string.am);
			} else if (starttimeDate.getHours() < 24) {
				return mContext.getResources().getString(R.string.pm);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

}
