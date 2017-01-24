package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.GuestSchedule;
import com.smartdot.wenbo.huiyi.R;

public class GuestScheduleAdapter extends BaseAdapter {

	Context context;
	ArrayList<GuestSchedule> list;

	public GuestScheduleAdapter(Context context, ArrayList<GuestSchedule> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder holder = null;
		if (convertView == null) {
			holder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_guestschedule, null);
			holder.vertical_line = (View) convertView.findViewById(R.id.vertical_line);
			holder.xingcheng_time = (TextView) convertView.findViewById(R.id.xingcheng_time);
			holder.xingcheng_name = (TextView) convertView.findViewById(R.id.xingcheng_name);
			holder.icon_xingcheng = (ImageView) convertView.findViewById(R.id.icon_xingcheng);

			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		if (position == list.size() - 1) {
			holder.vertical_line.setVisibility(View.GONE);
		} else {
			holder.vertical_line.setVisibility(View.VISIBLE);
		}
		holder.xingcheng_name.setText(list.get(position).name);
		holder.xingcheng_time.setText(list.get(position).date);
		if (list.get(position).state.equals("true")) {
			holder.icon_xingcheng.setBackgroundResource(R.drawable.guest_xingcheng_finish);
		} else {
			holder.icon_xingcheng.setBackgroundResource(R.drawable.guest_xingcheng_unfinish);
		}

		return convertView;
	}

	class Viewholder {
		TextView xingcheng_time, xingcheng_name;
		ImageView icon_xingcheng;
		View vertical_line;
	}
}
