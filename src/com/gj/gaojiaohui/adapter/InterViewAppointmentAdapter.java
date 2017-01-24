package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.ExhibitorsListChildBean;
import com.gj.gaojiaohui.bean.InterviewAppointmentChildBean;
import com.gj.gaojiaohui.bean.VipRegisterListBean;
import com.gj.gaojiaohui.utils.FirstLetterUtil;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

public class InterViewAppointmentAdapter extends CommonAdapter<InterviewAppointmentChildBean> {
	
	private List<InterviewAppointmentChildBean> items = new ArrayList<InterviewAppointmentChildBean>();

	private Context mContext;

	public InterViewAppointmentAdapter(Context context, List<InterviewAppointmentChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
	}

	@Override
	public void convert(ViewHolder viewHolder, InterviewAppointmentChildBean bean) {
       int position = viewHolder.getPosition();	
       viewHolder.setText(R.id.appointment_title_tv, bean.title);
	}

}
