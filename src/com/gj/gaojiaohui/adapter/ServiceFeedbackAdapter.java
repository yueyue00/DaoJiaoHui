package com.gj.gaojiaohui.adapter;

import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceFeedbackAdapter extends BaseAdapter{

	Integer [] head=new Integer[]{
		R.drawable.home_img_default_nor,R.drawable.home_img_default_nor,R.drawable.home_img_default_nor,R.drawable.home_img_default_nor,
	};
	String [] name=new String[]{
		"嘉宾一","嘉宾二","嘉宾三","嘉宾四",
	};
	Context context;
	public ServiceFeedbackAdapter(Context context) {
		this.context=context;
	}

	@Override
	public int getCount() {
		return name.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView = View.inflate(context, R.layout.servicefeedback_item, null);
			viewHolder.service_touxiang=(ImageView) convertView.findViewById(R.id.service_touxiang);
			viewHolder.service_name=(TextView) convertView.findViewById(R.id.service_name);
			viewHolder.service_liuyan=(TextView) convertView.findViewById(R.id.service_liuyan);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(position==0){
			viewHolder.service_liuyan.setVisibility(View.VISIBLE);
		}else if(position==1){
			viewHolder.service_liuyan.setVisibility(View.VISIBLE);
		}
		viewHolder.service_name.setText(name[position]);
		return convertView;
	}
	class ViewHolder{
		ImageView service_touxiang;
		TextView service_name;
		TextView service_liuyan;
	}
}
