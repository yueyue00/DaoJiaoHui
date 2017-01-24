package com.gj.gaojiaohui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 二维码 -赠票专用
 * 
 * @author Administrator
 * 
 */
public class QrCodeGiveActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	DisplayImageOptions options;
	private ImageView app_qr_image;
	private TextView noTicket;
	private String qrCodeType;
	private String qrCodeString;
	private String coupon;
	private TextView qr_code_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_give);

		options = ImageLoaderUtils.initOptions();
		Intent intent = getIntent();
		coupon = intent.getStringExtra("coupon");
		qrCodeString = intent.getStringExtra("qrCodeString");
		qrCodeType = intent.getStringExtra("qrCodeType");
		initView();

	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.qr_code_title));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		app_qr_image = (ImageView) findViewById(R.id.app_qr_image);
		noTicket = (TextView) findViewById(R.id.app_no_ticket_tv);
//		qr_code_tv = (TextView) findViewById(R.id.qr_code_tv);

		back.setOnClickListener(this);

		switch (qrCodeType) {
		// 观众二维码
		case "1":
			noTicket.setVisibility(View.GONE);
			Bitmap bitmap = CommonUtils.create2DCode(qrCodeString);
			app_qr_image.setImageBitmap(bitmap);
			title.setVisibility(View.GONE);
			qr_code_tv.setVisibility(View.GONE);
			break;
		// 高交会二维码
		case "2":
			noTicket.setVisibility(View.GONE);
			app_qr_image.setImageResource(R.drawable.chtf_qr_code_new);
			qr_code_tv.setVisibility(View.GONE);
			break;
		// 免费赠票
		case "3":
			if (coupon != null && !coupon.equals("")) {
				noTicket.setVisibility(View.GONE);
				Bitmap bitmap1 = CommonUtils.create2DCode(coupon);
				app_qr_image.setImageBitmap(bitmap1);
			} else {
				noTicket.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;

		default:
			break;
		}
	}
}
