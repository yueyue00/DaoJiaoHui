package com.gj.gaojiaohui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.bean.ActivityDetailsBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * 报名详情
 * 
 * @author Administrator
 * 
 */
public class SignUpDetailsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	int i = 0;
	private TextView title_tv;
	private ImageView sign_up_fanhui;
	private ImageView sign_up_baoming;
	private ImageView sign_up_place;
	private ImageView sign_up_pz_image;
	private RelativeLayout rl_huodong_dizhi;
	private RelativeLayout rl_huodong_pingzheng;
	private TextView faqibaoming;
	private TextView baomingshenhe;
	private TextView shenhetongguo;
	Context mContext;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};
	private TextView sign_up_title;
	private TextView sign_up_time;
	private TextView sign_up_neirong1;
	private TextView sign_up_num;
	private TextView sign_up_pingzheng;
	private TextView sign_up_jieshao;
	private ImageView sign_up_image;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_details);

		mContext = this;
		options = ImageLoaderUtils.initOptions();
		Intent intent = getIntent();
		String userId = intent.getStringExtra("userId");
		initView();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.registration_details));
		sign_up_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		sign_up_baoming = (ImageView) findViewById(R.id.sign_up_baoming);
		sign_up_place = (ImageView) findViewById(R.id.sign_up_place);
		sign_up_pz_image = (ImageView) findViewById(R.id.sign_up_pz_image);
		rl_huodong_dizhi = (RelativeLayout) findViewById(R.id.rl_huodong_dizhi1);
		rl_huodong_pingzheng = (RelativeLayout) findViewById(R.id.rl_huodong_pingzheng1);
		faqibaoming = (TextView) findViewById(R.id.faqibaoming1);
		baomingshenhe = (TextView) findViewById(R.id.baomingshenhe1);
		shenhetongguo = (TextView) findViewById(R.id.shenhetongguo1);
		sign_up_title = (TextView) findViewById(R.id.sign_up_title);
		sign_up_time = (TextView) findViewById(R.id.sign_up_time);
		sign_up_neirong1 = (TextView) findViewById(R.id.sign_up_neirong1);
		sign_up_num = (TextView) findViewById(R.id.sign_up_num);
		sign_up_pingzheng = (TextView) findViewById(R.id.sign_up_pingzheng);
		sign_up_jieshao = (TextView) findViewById(R.id.sign_up_jieshao);
		sign_up_image = (ImageView) findViewById(R.id.sign_up_image);

		rl_huodong_dizhi.setOnClickListener(this);
		rl_huodong_pingzheng.setOnClickListener(this);
		sign_up_place.setOnClickListener(this);
		sign_up_pz_image.setOnClickListener(this);
		sign_up_baoming.setOnClickListener(this);
		sign_up_fanhui.setOnClickListener(this);
		faqibaoming.setSelected(true);
		baomingshenhe.setSelected(true);
		shenhetongguo.setSelected(false);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		ActivityDetailsBean data = CommonUtil.gson.fromJson(json, ActivityDetailsBean.class);// 这段崩溃有3种原因
																								// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			// int apply = data.apply;
			// switch (apply) {
			// case 0:
			// faqibaoming.setSelected(false);
			// baomingshenhe.setSelected(false);
			// shenhetongguo.setSelected(false);
			// sign_up_pingzheng.setText(R.string.unjoin);
			// break;
			// case 1:
			// faqibaoming.setSelected(true);
			// baomingshenhe.setSelected(true);
			// shenhetongguo.setSelected(false);
			// sign_up_baoming.setImageResource(R.drawable.sign_up_btn_cancel_nor);
			// sign_up_pingzheng.setText(R.string.unjoin);
			// break;
			// case 2:
			// faqibaoming.setSelected(true);
			// baomingshenhe.setSelected(true);
			// shenhetongguo.setSelected(true);
			// sign_up_baoming.setImageResource(R.drawable.sign_up_btn_cancel_nor);
			// sign_up_pingzheng.setText(R.string.join);
			// break;
			// }
			// String pic = data.pic;
			// if (pic.equals("")) {
			// sign_up_image.setVisibility(View.GONE);
			// } else {
			// sign_up_image.setVisibility(View.VISIBLE);
			// ImageLoader.getInstance().displayImage(pic, sign_up_image,
			// options);
			// }
			sign_up_title.setText(data.title);
			sign_up_neirong1.setText(data.value);
			sign_up_num.setText(data.site);
			// sign_up_jieshao.setText(data.introduce);
		} else {
			/** 请求失败的处理 */
		}
	}

	private void getData() {
		Constant.getDataByVolley(mContext, null, handler, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.sign_up_baoming:
			if (i == 0) {
				sign_up_baoming.setImageResource(R.drawable.sign_up_btn_cancel_nor);
				i = 1;
			} else {
				sign_up_baoming.setImageResource(R.drawable.hdxq_btn_baoming_nor);
				i = 0;
			}
			break;
		case R.id.sign_up_place:

			break;
		case R.id.sign_up_pz_image:

			break;
		case R.id.rl_huodong_dizhi1:

			break;
		case R.id.rl_huodong_pingzheng1:

			break;
		default:
			break;
		}
	}
}
