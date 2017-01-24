package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.AddressBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 通讯录一级列表的适配器
 */
public class AddressGroupAdapter extends CommonAdapter<AddressBean> {
	private Context mContext;
	private DisplayImageOptions options;
	private List<AddressBean> items = new ArrayList<AddressBean>();
	private LayoutInflater inflater;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public AddressGroupAdapter(Context context, List<AddressBean> list, int itemLayoutResId) {
		super(context, list, itemLayoutResId);
		this.items = list;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void convert(ViewHolder viewHolder, AddressBean bean) {
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		viewHolderDept viewHolderDept = null;
		viewHolderUser viewHolderUser = null;

		int type = getItemViewType(position);
		if (convertView == null) {
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.item_address_group, parent, false);
				viewHolderDept = new viewHolderDept();
				viewHolderDept.address_name = (TextView) convertView.findViewById(R.id.address_name);
				convertView.setTag(viewHolderDept);
				break;
			case TYPE_2:
				convertView = inflater.inflate(R.layout.item_address_child, parent, false);
				viewHolderUser = new viewHolderUser();
				viewHolderUser.address_name = (TextView) convertView.findViewById(R.id.address_name);
				viewHolderUser.msg_img = (ImageView) convertView.findViewById(R.id.msg_img);
				viewHolderUser.phone_img = (ImageView) convertView.findViewById(R.id.phone_img);
				convertView.setTag(viewHolderUser);
				break;
			}

		} else {
			switch (type) {
			case TYPE_1:
				viewHolderDept = (viewHolderDept) convertView.getTag();
				break;
			case TYPE_2:
				viewHolderUser = (viewHolderUser) convertView.getTag();
				break;
			}
		}

		if (type == TYPE_2) {
			viewHolderUser.address_name.setText(getItem(position).name);
			addListener(viewHolderUser, position);
		} else {
			viewHolderDept.address_name.setText(getItem(position).name);
		}

		return convertView;
	}

	private void addListener(viewHolderUser viewHolderUser, final int position) {
		viewHolderUser.msg_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 执行发短信流程
				String tel = getItem(position).tel;
				CommonUtils.sendSms(mContext, tel);
			}
		});
		viewHolderUser.phone_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 执行打电话流程
				String tel = getItem(position).tel;
				CommonUtils.dialPhoneNumber(mContext, tel);
			}
		});
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (getItem(position).is_dept) {
			return TYPE_1;
		} else {
			return TYPE_2;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public class viewHolderDept {
		TextView address_name;
	}

	public class viewHolderUser {
		TextView address_name;
		ImageView msg_img;
		ImageView phone_img;
	}
}
