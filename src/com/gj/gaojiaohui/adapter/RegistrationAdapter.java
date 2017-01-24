package com.gj.gaojiaohui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.MyRegistrationActivity;
import com.gj.gaojiaohui.bean.PublicCodeBean;
import com.gj.gaojiaohui.bean.SignUpListChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class RegistrationAdapter extends CommonAdapter<SignUpListChildBean> {

	private DisplayImageOptions options;

	private Context mContext;

	private ImageView registration_image;
	private ImageView registration_examine;

	private ImageView registration_baoming;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				SignUpData(msg);
				break;
			}
		}
	};

	/**
	 * 解析json数据 报名/取消报名状态
	 * 
	 * @param msg
	 */
	private void SignUpData(Message msg) {
		String json = msg.obj.toString();
		PublicCodeBean data = CommonUtil.gson.fromJson(json,
				PublicCodeBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			((MyRegistrationActivity) mContext).getData();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext,
					mContext.getResources().getString(R.string.getdata_error));
		}
	}

	public RegistrationAdapter(Context context,
			List<SignUpListChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	/**
	 * 报名
	 * 
	 * @param id
	 */
	private void signUpData(int actionId) {
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		String url = String.format(GloableConfig.HUODONG_PARTICULARS_BAOMING,
				GloableConfig.userid, actionId);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, 1);
	}

	/**
	 * 取消报名
	 * 
	 * @param id
	 */
	private void cancelData(int actionId) {
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		String url = String.format(
				GloableConfig.HUODONG_PARTICULARS_PASSBAOMING,
				GloableConfig.userid, actionId);
		Map<String, String> map = new HashMap<>();
		volleyUtil1.stringRequest(handler, url, map, 1);
	}

	public void setData(int join_state) {
		if (join_state == 0) {
			registration_examine
					.setImageResource(R.drawable.sign_up_icon_audit_nor);
			registration_baoming
					.setImageResource(R.drawable.sign_up_btn_cancel_nor);
			// 状态为1时 报名审核中
		} else if (join_state == 1) {
			registration_examine
					.setImageResource(R.drawable.sign_up_icon_audit_nor);
			registration_baoming
					.setImageResource(R.drawable.sign_up_btn_cancel_nor);
			// 状态为2时 报名审核通过
		} else if (join_state == 2) {
			registration_examine
					.setImageResource(R.drawable.sign_up_icon_adopt_nor);
			registration_baoming
					.setImageResource(R.drawable.sign_up_btn_cancel_nor);
			// 状态为3时 报名审核不通过
		} else if (join_state == 3) {
			registration_examine
					.setImageResource(R.drawable.sign_up_icon_auditpass_nor);
			registration_baoming
					.setImageResource(R.drawable.sign_up_icon_again_nor);
			// 状态为4时 取消报名
		} else if (join_state == 4) {
			registration_examine
					.setImageResource(R.drawable.sign_up_icon_cancel_nor);
			registration_baoming
					.setImageResource(R.drawable.sign_up_icon_again_nor);
		}
	}

	@Override
	public void convert(ViewHolder viewHolder, SignUpListChildBean bean) {

		int position = viewHolder.getPosition();

		viewHolder.setText(R.id.registration_title, bean.title);
		viewHolder.setText(R.id.registration_neirong, bean.value);
		viewHolder.setText(R.id.registration_time, bean.date);
		registration_image = (ImageView) viewHolder
				.getView(R.id.registration_image);
		registration_examine = (ImageView) viewHolder
				.getView(R.id.registration_examine);
		registration_baoming = (ImageView) viewHolder
				.getView(R.id.registration_baoming);

		setData(bean.join_state);
		String pic = bean.pic;

		// 判断图片是否为空 为空时隐藏图片 不为空时加载图片
		if (pic.equals("")) {
			registration_image.setVisibility(View.GONE);
		} else {
			registration_image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(bean.pic,
					registration_image, options);
		}
		
		imgOnclick(position);

	}
	
	// userId和actionId没分清 把join_state position id定义成了全局变量 每次getView的时候值发生了改变
	// 没有搞清楚final的意义
	// getItem(position)获取bean 可以得到bean里面的值
	private void imgOnclick(final int position) {
		registration_baoming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 状态为1,2时 点击按钮取消报名
				if (getItem(position).join_state == 1) {
					cancelData(getItem(position).id);
				} else if (getItem(position).join_state == 2) {
					cancelData(getItem(position).id);
					// 状态为3,4时 点击按钮发起报名
				} else {
					signUpData(getItem(position).id);
				}
			}
		});
	}
}
