package com.gj.gaojiaohui.adapter;

import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceBackAdapter extends BaseAdapter{

	Integer [] head=new Integer[]{
		R.drawable.home_img_default_nor,R.drawable.home_img_default_nor,R.drawable.home_img_default_nor
	};
	String [] name=new String[]{
		"张三","张三","服务人员A"
	};
	String [] time=new String[]{
		"2016.9.12 15:32:22","2016.9.12 15:32:22","2016.9.12 15:32:22"
	};
	String [] message=new String[]{
		"二十余位重量级嘉宾将登临第十八届高交会二十余位重量级嘉宾将登临第十八届高交会",
		"二十余位重量级嘉宾将登临第十八届高交会二十余位重量级嘉宾将登临第十八届高交会",
		"二十余位重量级嘉宾将登临第十八届高交会二十余位重量级嘉宾将登临第十八届高交会"
	};
	Context context;
	public ServiceBackAdapter(Context context) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=View.inflate(context, R.layout.servicefeedback_item1, null);
			viewHolder.service_touxiang1=(ImageView) convertView.findViewById(R.id.service_touxiang1);
			viewHolder.service_name1=(TextView) convertView.findViewById(R.id.service_name1);
			viewHolder.service_liuyan1=(TextView) convertView.findViewById(R.id.service_liuyan1);
			viewHolder.service_neirong=(TextView) convertView.findViewById(R.id.service_neirong);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.service_name1.setText(name[position]);
		viewHolder.service_liuyan1.setText(time[position]);
		viewHolder.service_neirong.setText(message[position]);
		return convertView;
	}
	class ViewHolder{
		ImageView service_touxiang1;
		TextView service_name1;
		TextView service_liuyan1;
		TextView service_neirong;
	}
}
