package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SectionIndexer;

import com.gj.gaojiaohui.bean.AddressBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.FirstLetterUtil;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 通讯录二级列表的适配器
 */
public class AddressChildAdapter extends CommonAdapter<AddressBean> implements SectionIndexer {
	private List<AddressBean> items = new ArrayList<AddressBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public AddressChildAdapter(Context context, List<AddressBean> list, int itemLayoutResId) {
		super(context, list, itemLayoutResId);
		this.items = list;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, AddressBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.address_name, bean.name);
		addListener(viewHolder, position);

	}

	private void addListener(ViewHolder viewHolder, final int position) {
		ImageView msg_img = (ImageView) viewHolder.getView(R.id.msg_img);
		ImageView phone_img = (ImageView) viewHolder.getView(R.id.phone_img);
		msg_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 执行打电话流程
				String tel = getItem(position).tel;
				CommonUtils.dialPhoneNumber(mContext, tel);
			}
		});
		phone_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 执行发短信流程
				String tel = getItem(position).tel;
				CommonUtils.sendSms(mContext, tel);
			}
		});
	}

	public void changeData(List<AddressBean> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		AddressBean bean;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				bean = (AddressBean) items.get(i);
				// 取首字母
				l = FirstLetterUtil.getFirstLetter(bean.name);
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		}
		bean = null;
		l = null;
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
