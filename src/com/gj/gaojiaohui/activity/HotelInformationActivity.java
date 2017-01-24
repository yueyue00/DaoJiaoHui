package com.gj.gaojiaohui.activity;

import java.util.HashMap;
import java.util.Map;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.GuestServiceBean;
import com.gj.gaojiaohui.bean.HotelInformationBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;
import com.smartdot.wenbo.huiyi.R.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 下榻酒店信息---嘉宾服务
 * 
 * @author lixiaoming
 * 
 */
public class HotelInformationActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {
	private Context mContext;
	private DisplayImageOptions options;
	private ImageView back_img;
	private TextView title_tv;
	private TextView hotel_name_tv;
	private TextView hotel_local_tv;
	private TextView hotel_room_tv;
	private ImageView hotel_local_img;
	private RelativeLayout hotel_local_relative;
	private ImageView hotel_img;
	private HotelInformationBean bean;
	private HotelInformationBean data;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				// 不管数据请求成功与否都要把进度加载条关闭
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel_information);
		mContext = this;
		initView();
		initData();
	}

	/** 初始化数据 */
	private void initData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.HOTEL_INFORMATION_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		L.v("lixm", "下榻酒店信息url:"+url);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.hotelinfo_title));
		hotel_name_tv = (TextView) findViewById(R.id.hotel_name_tv);
		hotel_local_tv = (TextView) findViewById(R.id.hotel_local_tv);
		hotel_local_img = (ImageView) findViewById(R.id.hotel_local_img);
		hotel_room_tv = (TextView) findViewById(R.id.hotel_room_tv);
		hotel_local_relative = (RelativeLayout) findViewById(R.id.hotel_local_relative);
		hotel_local_relative.setOnClickListener(this);
		hotel_img = (ImageView) findViewById(R.id.hotel_img);
	}

	/**
	 * 使用ImageLoader加载图片
	 * 
	 * @param imageView
	 *            控件
	 * @param imgUrl
	 *            图片url
	 */
	public void getPic(ImageView imageView, String imgUrl) {
		options = ImageLoaderUtils.initOptionsToSetDefaultPic(R.drawable.bg_hotel);
		ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "下榻酒店信息:"+json);
		// 通过gson将json映射成实体类
		data = CommonUtil.gson.fromJson(json, HotelInformationBean.class);// 这段崩溃有3种原因
																			// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			if (TextUtils.isEmpty(data.name)) {// 如果酒店名字为空
				hotel_name_tv.setText(getResources().getString(R.string.hotel_information_isnull));
			} else {
				hotel_name_tv.setText(data.name);
			}
			if (TextUtils.isEmpty(data.site)) {
				hotel_local_tv.setText(getResources().getString(R.string.hotel_information_isnull));
			} else {
				hotel_local_tv.setText(data.site);
			}
			hotel_room_tv.setText(data.room);
			getPic(hotel_img, data.pic);// 设置酒店图片
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
		case R.id.hotel_local_relative:
			if (CommonUtils.isNetworkConnected(mContext)) {
				if (TextUtils.isEmpty(data.latitude) || TextUtils.isEmpty(data.longitude)) {
					CustomToast.showToast(mContext, getResources().getString(R.string.location_empty));
				} else {
					Intent intent = new Intent(mContext, DaHuiNaviActivity.class);
					intent.putExtra("latitude", data.latitude);
					intent.putExtra("longitude", data.longitude);
					intent.putExtra("address", data.name);
					startActivity(intent);
				}
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.network));
			}
			break;
		}
	}

}
