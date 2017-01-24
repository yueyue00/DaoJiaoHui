package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.BusinessInformationChildBean;
import com.gj.gaojiaohui.bean.ExhibitorsDetailsChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.L;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class BusinessAdapter extends BaseAdapter {

	Context context;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	private DisplayImageOptions options;
	private LayoutInflater inflater;
	List<BusinessInformationChildBean> list;

	public BusinessAdapter(Context context, List<BusinessInformationChildBean> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		String pic = list.get(position).pic;
		if (pic.equals("")) {
			return TYPE_1;// 无图
		} else {
			return TYPE_2;// 有图
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder1 viewHolder1 = null;
		ViewHolder2 viewHolder2 = null;

		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.zhanpinjiesao2, null);
				viewHolder1 = new ViewHolder1();
				viewHolder1.zhanpin_name1 = (TextView) convertView.findViewById(R.id.zhanpin_name1);
				viewHolder1.zhanpin_neirong1 = (TextView) convertView.findViewById(R.id.zhanpin_neirong1);
				convertView.setTag(viewHolder1);
				break;
			case TYPE_2:
				convertView = inflater.inflate(R.layout.zhanpinjiesao1, null);
				viewHolder2 = new ViewHolder2();
				viewHolder2.zhanpin_name = (TextView) convertView.findViewById(R.id.zhanpin_name);
				viewHolder2.zhanpin_neirong = (TextView) convertView.findViewById(R.id.zhanpin_neirong);
				viewHolder2.zhanshang_image = (ImageView) convertView.findViewById(R.id.zhanshang_image);
				convertView.setTag(viewHolder2);
				break;
			}

		} else {
			switch (type) {
			case TYPE_1:
				String pic = list.get(position).pic;
				viewHolder1 = (ViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				viewHolder2 = (ViewHolder2) convertView.getTag();
				break;
			}

		}

		switch (type) {
		case TYPE_1:
			viewHolder1.zhanpin_name1.setText(list.get(position).title);
			viewHolder1.zhanpin_neirong1.setText(list.get(position).value);
			break;
		case TYPE_2:
			viewHolder2.zhanpin_name.setText(list.get(position).title);
			viewHolder2.zhanpin_neirong.setText(list.get(position).value);
			if (!TextUtils.isEmpty(list.get(position).pic)) {
				ImageLoader.getInstance().displayImage(list.get(position).pic, viewHolder2.zhanshang_image, options);
			} else {
				viewHolder2.zhanshang_image.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		return convertView;
	}

	class ViewHolder1 {
		TextView zhanpin_name1;
		TextView zhanpin_neirong1;
	}

	class ViewHolder2 {
		TextView zhanpin_neirong;
		TextView zhanpin_name;
		ImageView zhanshang_image;
	}
}
