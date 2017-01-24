package com.gj.gaojiaohui.activity;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.a.a.o;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.ExhibitsDetailBean;
import com.gj.gaojiaohui.bean.NoticeCenterBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 展品详情webview
 * 
 * @author Administrator
 * 
 */
public class ZhanPinParticularsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView exhibits_details_fanhui;
	private TextView exhibits_title_tv;
	private ImageView exhibits_pic_img;
	private TextView exhibits_content_tv;
	private Context mContext;
	private String userId;
	private String id;
	private ExhibitsDetailBean data;
	private DisplayImageOptions options;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhan_pin_particulars);
		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("zhanPinId");
		options = ImageLoaderUtils.initOptions();
		initView();
		getData();
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.EXHIBITOR_DETAIL_URL, id, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.exhibits_details));
		exhibits_details_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		exhibits_details_fanhui.setOnClickListener(this);
		exhibits_title_tv = (TextView) findViewById(R.id.exhibits_title_tv);
		exhibits_pic_img = (ImageView) findViewById(R.id.exhibits_pic_img);
		exhibits_content_tv = (TextView) findViewById(R.id.exhibits_content_tv);

	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		data = CommonUtil.gson.fromJson(json, ExhibitsDetailBean.class);// 这段崩溃有3种原因
																		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			exhibits_title_tv.setText(data.title);
			exhibits_content_tv.setText(data.value);
			if (!TextUtils.isEmpty(data.pic)) {
				ImageLoader.getInstance().displayImage(data.pic, exhibits_pic_img, options);
			}else {
				exhibits_pic_img.setVisibility(View.GONE);
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
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