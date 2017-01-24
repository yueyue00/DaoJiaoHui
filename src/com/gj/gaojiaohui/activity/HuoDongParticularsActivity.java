package com.gj.gaojiaohui.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.ActivityDetailsBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 活动详情
 * 
 * @author Administrator
 * 
 */
public class HuoDongParticularsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView custom_back_img;
	private ImageView huodong_baoming;
	private TextView faqibaoming_tv;
	private TextView baomingshenhe_tv;
	private TextView shenhetongguo_tv;
	private TextView huodong_value;
	private TextView huodong_time;
	private ImageView huodong_image;
	private RelativeLayout huodong_weizhi_rl;
	private RelativeLayout huodong_pingzheng_rl;
	DisplayImageOptions options;
	Context mContext;
	private TextView huodong_title;
	private TextView huizhan_didian;
	private TextView huodong_pingzheng;
	private ProgressBar progressbar_updown;
	private int actionId;
	private ActivityDetailsBean data;
	private ImageView kaishibaoming_img;
	private ImageView shenhebaoming_img;
	private ImageView tongguoshenhe_img;
	private int apply;
	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			// 发起报名与取消报名
			case 1:
				SignUpData(msg);
				break;
			}
		}
	};
	private TextView convention_time_start;
	private TextView convention_time_end;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huo_dong_particulars);

		mContext = this;
		options = ImageLoaderUtils.initOptions();
		actionId = getIntent().getIntExtra("userId", 0);
		initView();
		getData();
	}

	private void initView() {
		huodong_weizhi_rl = (RelativeLayout) findViewById(R.id.huodong_weizhi_rl);
		huodong_pingzheng_rl = (RelativeLayout) findViewById(R.id.huodong_pingzheng_rl);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.activity_details));
		custom_back_img = (ImageView) findViewById(R.id.custom_back_img);
		huodong_image = (ImageView) findViewById(R.id.xq_huodong_image);
		faqibaoming_tv = (TextView) findViewById(R.id.faqibaoming);
		baomingshenhe_tv = (TextView) findViewById(R.id.baomingshenhe);
		progressbar_updown = (ProgressBar) findViewById(R.id.progressbar_updown);
		shenhetongguo_tv = (TextView) findViewById(R.id.shenhetongguo);
		huodong_baoming = (ImageView) findViewById(R.id.xq_huodong_baoming);
		huodong_value = (TextView) findViewById(R.id.xq_huodong_neirong);
		huodong_time = (TextView) findViewById(R.id.activity_time);
		huodong_title = (TextView) findViewById(R.id.huodong_xiangqing_title);
		huizhan_didian = (TextView) findViewById(R.id.huichang_num);
		huodong_pingzheng = (TextView) findViewById(R.id.huodong_pingzheng);
		kaishibaoming_img = (ImageView) findViewById(R.id.yuan1);
		shenhebaoming_img = (ImageView) findViewById(R.id.yuan2);
		tongguoshenhe_img = (ImageView) findViewById(R.id.yuan3);
		convention_time_start = (TextView) findViewById(R.id.convention_time_start);
		convention_time_end = (TextView) findViewById(R.id.convention_time_end);

		huodong_weizhi_rl.setOnClickListener(this);
		huodong_pingzheng_rl.setOnClickListener(this);
		huodong_baoming.setOnClickListener(this);
		custom_back_img.setOnClickListener(this);
		faqibaoming_tv.setSelected(false);
		baomingshenhe_tv.setSelected(false);
		shenhetongguo_tv.setSelected(false);
		// permissionJudge();
	}

	/** 解析json数据 刷新数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		data = CommonUtil.gson.fromJson(json, ActivityDetailsBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			setData();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 解析json数据 报名/取消报名状态 */
	private void SignUpData(Message msg) {
		String json = msg.obj.toString();
		PublicResultCodeBean data1 = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data1.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			getData();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void setData() {
		// apply = data.apply;
		// switch (apply) {
		// case 0:// 未报名
		// cancelSignUp();
		// cancelTextChange();
		// shenhetongguo_tv.setText(R.string.shenhe_pass);
		// progressbar_updown.setProgress(0);
		// shenhetongguo_tv.setTextColor(Color.rgb(111, 113, 112));
		// huodong_baoming.setImageResource(R.drawable.hdxq_btn_baoming_nor);
		// huodong_pingzheng.setText("");
		// break;
		// case 1:// 审核中
		// faqibaoming_tv.setSelected(true);
		// baomingshenhe_tv.setSelected(true);
		// shenhetongguo_tv.setSelected(false);
		// shenhetongguo_tv.setText(R.string.shenhe_pass);
		// shenhetongguo_tv.setTextColor(Color.rgb(111, 113, 112));
		// progressbar_updown.setProgress(50);
		// kaishibaoming_img.setImageResource(R.drawable.hdxq_img_shenhetg_nor);
		// shenhebaoming_img.setImageResource(R.drawable.hdxq_img_shenhetg_nor);
		// tongguoshenhe_img.setImageResource(R.drawable.hdxq_img_shenhezhong_nor);
		// huodong_baoming.setImageResource(R.drawable.sign_up_btn_cancel_nor);
		// huodong_pingzheng.setText(getResources().getString(R.string.in_the_application));
		// break;
		// case 2:// 审核通过
		// successSignUp();
		// successTextChange();
		// progressbar_updown.setProgress(100);
		// shenhetongguo_tv.setText(R.string.shenhe_pass);
		// huodong_baoming.setImageResource(R.drawable.sign_up_btn_cancel_nor);
		// huodong_pingzheng.setText(R.string.join);
		// break;
		// case 3:// 审核未通过
		// successSignUp();
		// successTextChange();
		// shenhetongguo_tv.setTextColor(Color.rgb(230, 27, 18));
		// shenhetongguo_tv.setText(R.string.shenhe_unpass);
		// progressbar_updown.setProgress(100);
		// huodong_baoming.setImageResource(R.drawable.hdxq_btn_baoming_nor);
		// huodong_pingzheng.setText("");
		// break;
		// case 4:// 取消报名
		// cancelSignUp();
		// cancelTextChange();
		// shenhetongguo_tv.setText(R.string.shenhe_pass);
		// progressbar_updown.setProgress(0);
		// huodong_baoming.setImageResource(R.drawable.hdxq_btn_baoming_nor);
		// huodong_pingzheng.setText("");
		// shenhetongguo_tv.setTextColor(Color.rgb(111, 113, 112));
		// break;
		// }
		// 判断是否有图片 没图片时隐藏图片 有图片时加载图片
		// String pic = data.pic;
		// if (pic.equals("")) {
		// huodong_image.setVisibility(View.GONE);
		// } else {
		// huodong_image.setVisibility(View.VISIBLE);
		// ImageLoader.getInstance().displayImage(pic, huodong_image, options);
		// }
		huodong_title.setText(data.title);
		huodong_value.setText(data.value);
		huizhan_didian.setText(data.site);
		convention_time_start.setText(data.start_date);
		convention_time_end.setText(data.end_date);
	}


	/** 刷新数据 */
	private void getData() {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		String url = String.format(GloableConfig.HUODONG_PARTICULARS, GloableConfig.userid, actionId, GloableConfig.LANGUAGE_TYPE);
		// 活动详情接口调试。。。。。。
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, 0);
	}
	/** 报名 */
	private void signUpData() {
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		String url1 = String.format(GloableConfig.HUODONG_PARTICULARS_BAOMING, GloableConfig.userid, actionId);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url1, map, 1);
	}

	/** 取消报名 */
	private void cancelData() {
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		String url2 = String.format(GloableConfig.HUODONG_PARTICULARS_PASSBAOMING, GloableConfig.userid, actionId);
		Map<String, String> map = new HashMap<>();
		volleyUtil1.stringRequest(handler, url2, map, 1);
	}

	/** 取消报名后textview的颜色 */
	private void cancelTextChange() {
		faqibaoming_tv.setSelected(false);
		baomingshenhe_tv.setSelected(false);
		shenhetongguo_tv.setSelected(false);
	}

	/** 取消报名后小圆点的状态 */
	private void cancelSignUp() {
		kaishibaoming_img.setImageResource(R.drawable.hdxq_img_shenhezhong_nor);
		shenhebaoming_img.setImageResource(R.drawable.hdxq_img_shenhezhong_nor);
		tongguoshenhe_img.setImageResource(R.drawable.hdxq_img_shenhezhong_nor);
	}

	/** 审核通过textview颜色的变化 */
	private void successTextChange() {
		faqibaoming_tv.setSelected(true);
		baomingshenhe_tv.setSelected(true);
		shenhetongguo_tv.setSelected(true);
	}

	/** 审核通过后小圆点的状态 */
	private void successSignUp() {
		kaishibaoming_img.setImageResource(R.drawable.hdxq_img_shenhetg_nor);
		shenhebaoming_img.setImageResource(R.drawable.hdxq_img_shenhetg_nor);
		tongguoshenhe_img.setImageResource(R.drawable.hdxq_img_shenhetg_nor);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.xq_huodong_baoming:
			// 判断用户是否登录
			// if (GloableConfig.userid.equals("")) {
			// Intent intent = new Intent(HuoDongParticularsActivity.this,
			// LoginActivity.class);
			// startActivity(intent);
			// } else {
			// if (GloableConfig.username.equals("")) {
			// System.out.println(GloableConfig.username+"..................");
			// CustomToast.showToast(mContext,
			// getResources().getString(R.string.finish_you_personal_info));
			// startActivity(new Intent(HuoDongParticularsActivity.this,
			// PersonalDataActivity.class));
			// } else {
			// System.out.println("权限:" + GloableConfig.permission + "状态:" +
			// apply);
			// if (apply == 0) {
			// signUpData();
			// } else if (apply == 3) {
			// signUpData();
			// } else if (apply == 4) {
			// signUpData();
			// } else {
			// cancelData();
			// }
			// }
			// }
			break;
		case R.id.huodong_weizhi_rl:
			if (data.type == 1) {// 室外

				Intent intent = new Intent(mContext, DaHuiNaviActivity.class);
				intent.putExtra("latitude", data.latitude);
				intent.putExtra("longitude", data.longitude);
				intent.putExtra("address", data.site);
				startActivity(intent);

			} else if (data.type == 2) {// 室内
				Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
				intent1.putExtra("mapType", "indoor");
				intent1.putExtra("startid", "");
				intent1.putExtra("endid", data.tag);
				intent1.putExtra("endfid", "");
				startActivity(intent1);
			}
			break;
		case R.id.huodong_pingzheng_rl:

			break;
		default:
			break;
		}
	}

	private void permissionJudge() {
		if (GloableConfig.permission.equals("3") || GloableConfig.permission.equals("")) {
			huodong_baoming.setVisibility(View.VISIBLE);
		} else {
			huodong_baoming.setVisibility(View.GONE);
		}
	}
}
