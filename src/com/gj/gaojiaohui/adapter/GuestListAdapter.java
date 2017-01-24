package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SectionIndexer;

import com.gj.gaojiaohui.bean.GuestInfoBean;
import com.gj.gaojiaohui.utils.FirstLetterUtil;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * 通讯录一级列表的适配器
 */
public class GuestListAdapter extends CommonAdapter<GuestInfoBean> implements SectionIndexer {
	private List<GuestInfoBean> items = new ArrayList<GuestInfoBean>();
	private DisplayImageOptions options;
	private Context mContext;

	public GuestListAdapter(Context context, List<GuestInfoBean> list, int itemLayoutResId) {
		super(context, list, itemLayoutResId);
		this.items = list;
		this.mContext = context;
		options = ImageLoaderUtils.initOptionsForCirclePic(R.drawable.pic_vip, 40);

	}

	@Override
	public void convert(ViewHolder viewHolder, final GuestInfoBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.guest_name, bean.name);
		viewHolder.setText(R.id.guest_workplace, bean.company);
		viewHolder.setText(R.id.guest_job, bean.position);
		ImageView guest_icon = (ImageView) viewHolder.getView(R.id.richeng_line_view);
		// ImageLoader.getInstance().displayImage(bean.pic, guest_icon,
		// options);
		ImageView msg_img = (ImageView) viewHolder.getView(R.id.msg_img);
		msg_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bean.tel != null && !bean.tel.equals("")) {
					doSendSMSTo(bean.tel, "");
				}
			}
		});
		ImageView phone_img = (ImageView) viewHolder.getView(R.id.phone_img);
		phone_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bean.tel != null && !bean.tel.equals("")) {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + bean.tel));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			}
		});
	}

	public void changeData(List<GuestInfoBean> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		GuestInfoBean bean;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				bean = (GuestInfoBean) items.get(i);
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
		return 0;
	}

	public void doSendSMSTo(String phoneNumber, String message) {
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
			intent.putExtra("sms_body", message);
			context.startActivity(intent);
		}
	}
}
