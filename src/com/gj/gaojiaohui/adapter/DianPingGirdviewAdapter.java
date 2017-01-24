package com.gj.gaojiaohui.adapter;

import java.io.Serializable;
import java.util.List;

import com.gj.gaojiaohui.activity.PicViewPagerActivity;
import com.gj.gaojiaohui.bean.PicBean;
import com.gj.gaojiaohui.bean.ReplyBean;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 点评的girdview的Adapter
 * 
 * @author zhangt
 * 
 */

public class DianPingGirdviewAdapter extends CommonAdapter<PicBean> {

	private DisplayImageOptions options;
	private ImageView imageView;
	private Context mContext;
	private List<PicBean> datas;

	public DianPingGirdviewAdapter(Context context, List<PicBean> pic_list, int itemLayoutResId) {
		super(context, pic_list, itemLayoutResId);
		mContext = context;
		options = ImageLoaderUtils.initOptions();
		this.datas = pic_list;
	}

	@Override
	public void convert(final ViewHolder viewHolder, PicBean bean) {
		ImageView imageView = viewHolder.getView(R.id.item_dianping_gridview_img);
		ImageLoader.getInstance().displayImage(bean.pic, imageView, options);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PicViewPagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("page", viewHolder.getPosition());
				bundle.putSerializable("datas", (Serializable) datas);
				intent.putExtra("pic", bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
				mContext.startActivity(intent);
			}
		});
	}


}
