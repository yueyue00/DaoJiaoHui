package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gj.gaojiaohui.bean.MeetingDatasChildBean;
import com.gj.gaojiaohui.utils.DownLoadUtil;
import com.smartdot.wenbo.huiyi.R;

public class MeetingDatasAdapter extends CommonAdapter<MeetingDatasChildBean> {

	private List<MeetingDatasChildBean> items = new ArrayList<MeetingDatasChildBean>();
	private Context mContext;

	public MeetingDatasAdapter(Context context, List<MeetingDatasChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		// TODO Auto-generated constructor stub
		this.items = items;
		this.mContext = context;
	}

	@Override
	public void convert(ViewHolder viewHolder, MeetingDatasChildBean bean) {
		viewHolder.setText(R.id.meeting_datas_name_tv, bean.name);
		addListener(viewHolder, bean);
	}

	// 下载资料的监听事件
	private void addListener(ViewHolder viewHolder, final MeetingDatasChildBean bean) {
		LinearLayout download_ll = (LinearLayout) viewHolder.getView(R.id.meeting_datas_download_ll);
		download_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DownLoadUtil downLoadUtil = new DownLoadUtil(mContext, bean.url);
				downLoadUtil.showDownload();
			}
		});
	}

}
