package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.gj.gaojiaohui.bean.NoticeCenterChildBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackChildBean;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

public class NoticeCenterAdapter extends CommonAdapter<NoticeCenterChildBean> {

	private List<NoticeCenterChildBean> items = new ArrayList<NoticeCenterChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public NoticeCenterAdapter(Context context,
			List<NoticeCenterChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, NoticeCenterChildBean bean) {
		viewHolder.setText(R.id.notice_center_item_title, bean.title);
		viewHolder.setText(R.id.notice_center_item_date, bean.date);
		ImageView imageView = (ImageView) viewHolder
				.getView(R.id.notice_center_item_icon_img);
		// 0/紧急通知,1/会议通知,2/获奖通知,3/媒体预约,4/嘉宾日程通知,5/展商[和]观众/嘉宾互相留言通知,6/广播
		if (bean.type.equals(" 0")) {// 紧急通知
			imageView.setImageResource(R.drawable.pic_icon_dahui_nitoce_nor);
		} else if (bean.type.equals("1")) {// 会议通知
			imageView.setImageResource(R.drawable.pic_icon_schdule_notice_nor);
		} else if (bean.type.equals("2")) {// 获奖通知
			imageView.setImageResource(R.drawable.pic_icon_dahui_nitoce_nor);
		} else if (bean.type.equals("3")) {// 媒体预约
			imageView.setImageResource(R.drawable.pic_icon_media_notice_nor);
		} else if (bean.type.equals("4")) {// 嘉宾日程通知
			imageView.setImageResource(R.drawable.pic_icon_schdule_notice_nor);
		} else if (bean.type.equals("5")) {// 展商[和]观众/嘉宾互相留言通知
			imageView.setImageResource(R.drawable.pic_icon_liuyan_nor);
		}else if (bean.type.equals("6")) {// 广播
			imageView.setImageResource(R.drawable.pic_icon_dahui_nitoce_nor);
		}
	}

}
