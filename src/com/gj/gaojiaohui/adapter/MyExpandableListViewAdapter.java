package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.MeetingChildBean;
import com.gj.gaojiaohui.bean.MeetingGroupBean;
import com.smartdot.wenbo.huiyi.R;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private List<MeetingGroupBean> group_list;
	private List<List<MeetingChildBean>> item_list;

	public MyExpandableListViewAdapter(Context context, List<MeetingGroupBean> group_list, List<List<MeetingChildBean>> item_list) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.group_list = group_list;
		this.item_list = item_list;
	}

	@Override
	public int getGroupCount() {
		return group_list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return item_list.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group_list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return item_list.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public void changData(List<MeetingGroupBean> group_list, List<List<MeetingChildBean>> item_list) {
		if (group_list != null && item_list != null) {
			this.group_list = group_list;
			this.item_list = item_list;
			notifyDataSetChanged();
		}
	}

	/** 标题 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_schedule_group, null);

			groupHolder = new GroupHolder();

			groupHolder.date_tv = (TextView) convertView.findViewById(R.id.richeng_group_date);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		if (group_list.get(groupPosition).forenoon) {
			groupHolder.date_tv.setTextColor(Color.parseColor("#80B1FC"));
		} else {
			groupHolder.date_tv.setTextColor(Color.parseColor("#FB982E"));
		}
		
		groupHolder.date_tv.setText(group_list.get(groupPosition).date);
		return convertView;
	}

	/** 子布局 */
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ItemHolder itemHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_schedule_child, null);
			itemHolder = new ItemHolder();
			itemHolder.richeng_meeting_name_tv = (TextView) convertView.findViewById(R.id.richeng_meeting_name_tv);
			itemHolder.richeng_meeting_date_tv = (TextView) convertView.findViewById(R.id.richeng_meeting_date_tv);
			itemHolder.richeng_meeting_compere_tv = (TextView) convertView.findViewById(R.id.richeng_meeting_compere_tv);
			convertView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder) convertView.getTag();
		}

		itemHolder.richeng_meeting_name_tv.setText(item_list.get(groupPosition).get(childPosition).meeting_title.trim());
		itemHolder.richeng_meeting_date_tv.setText(item_list.get(groupPosition).get(childPosition).meeting_site.trim());
		itemHolder.richeng_meeting_compere_tv.setText(item_list.get(groupPosition).get(childPosition).meeting_compere.trim());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupHolder {
		public TextView date_tv;
	}

	class ItemHolder {
		public TextView richeng_meeting_name_tv;
		public TextView richeng_meeting_date_tv;
		public TextView richeng_meeting_compere_tv;
	}
}
