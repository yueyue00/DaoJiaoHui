package com.gj.gaojiaohui.activity;

import java.util.HashMap;
import java.util.Map;

import com.gheng.exhibit.http.body.response.InitData;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.FollowListBean;
import com.gj.gaojiaohui.bean.GuestServiceBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 嘉宾服务
 * 
 * @author lixiaoming
 * 
 */
public class GuestServiceActivity extends GaoJiaoHuiBaseActivity implements
		OnClickListener {

	private Context mContext;
	private ImageView back_img;
	private TextView tittle_tv;
	private RelativeLayout hotelInfo_relative;
	private RelativeLayout hotelNavi_realtive;
	private RelativeLayout hallNavi_relative;
	private RelativeLayout exclusiveMember_relative;
	private ImageView hotelInfo_img;
	private ImageView hotelNavi_img;
	private ImageView hallNavi_img;
	private ImageView exclusive_img;
	private Button oneKey_call_btn;
	private GuestServiceBean data;
	private int position = -1;
	private String[] hall_arr;

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
		setContentView(R.layout.activity_guest_service);
		mContext = this;
		initView();
		InitData();
	}

	/** 初始化数据 */
	private void InitData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.GUEST_SERVICE_URL,
				GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		tittle_tv = (TextView) findViewById(R.id.custom_title_tv);
		tittle_tv.setText(getResources().getString(R.string.guest_service));
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		hotelInfo_relative = (RelativeLayout) findViewById(R.id.guestService_hotel_info_relative);
		hotelInfo_relative.setOnClickListener(this);
		hotelNavi_realtive = (RelativeLayout) findViewById(R.id.guestService_hotel_navi_relative);
		hotelNavi_realtive.setOnClickListener(this);
		hallNavi_relative = (RelativeLayout) findViewById(R.id.guestService_hall_navi_relative);
		hallNavi_relative.setOnClickListener(this);
		exclusiveMember_relative = (RelativeLayout) findViewById(R.id.guestService_exclusive_member_relative);
		exclusiveMember_relative.setOnClickListener(this);
		hotelInfo_img = (ImageView) findViewById(R.id.guestService_hotelInfo_iv);
		hotelNavi_img = (ImageView) findViewById(R.id.guestService_hotelNavi_iv);
		hallNavi_img = (ImageView) findViewById(R.id.guestService_hallNavi_iv);
		exclusive_img = (ImageView) findViewById(R.id.guestService_exclusive_iv);
		oneKey_call_btn = (Button) findViewById(R.id.oneKey_call_btn);
		oneKey_call_btn.setOnClickListener(this);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		data = CommonUtil.gson.fromJson(json, GuestServiceBean.class);// 这段崩溃有3种原因
																		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			hall_arr = new String[data.exhibition_map.size()];
			for (int i = 0; i < hall_arr.length; i++) {
				hall_arr[i] = data.exhibition_map.get(i).name;
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext,
					getResources().getString(R.string.getdata_error));
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.guestService_hotel_info_relative:// 下榻酒店信息
			if (CommonUtils.isNetworkConnected(mContext)) {
				intent.setClass(mContext, HotelInformationActivity.class);
				startActivity(intent);
			} else {
				CustomToast.showToast(mContext,
						getResources().getString(R.string.network));
			}

			break;
		case R.id.guestService_hotel_navi_relative:// 酒店导航
			if (CommonUtils.isNetworkConnected(mContext)) {
				if (TextUtils.isEmpty(data.hotel_map.latitude)
						|| TextUtils.isEmpty(data.hotel_map.longitude)) {
					CustomToast.showToast(mContext,
							getResources().getString(R.string.location_empty));
				} else {
					intent.setClass(mContext, DaHuiNaviActivity.class);
					intent.putExtra("latitude", data.hotel_map.latitude);
					intent.putExtra("longitude", data.hotel_map.longitude);
					intent.putExtra("address", data.hotel_map.name);
					startActivity(intent);
				}

			} else {
				CustomToast.showToast(mContext,
						getResources().getString(R.string.network));
			}
			break;
		case R.id.guestService_hall_navi_relative:// 会场导航
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(getResources().getString(R.string.dialog_title));
			builder.setSingleChoiceItems(hall_arr, -1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							position = which;
						}
					});
			builder.setPositiveButton(
					getResources().getString(R.string.dialog_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (CommonUtils.isNetworkConnected(mContext)) {
								String name = data.exhibition_map.get(position).name;
								String latitude = data.exhibition_map
										.get(position).latitude;
								String longitude = data.exhibition_map
										.get(position).longitude;
								if (TextUtils.isEmpty(latitude)
										|| TextUtils.isEmpty(longitude)) {
									CustomToast.showToast(
											mContext,
											getResources().getString(
													R.string.location_empty));
								} else {
									Intent intent = new Intent(mContext,
											DaHuiNaviActivity.class);
									intent.putExtra("latitude", latitude);
									intent.putExtra("longitude", longitude);
									intent.putExtra("address", name);
									startActivity(intent);
								}
							} else {
								CustomToast.showToast(mContext, getResources()
										.getString(R.string.network));
							}

						}
					});
			builder.setNegativeButton(
					getResources().getString(R.string.dialog_cancle), null);
			builder.create().show();
			break;
		case R.id.guestService_exclusive_member_relative:// 专属服务人员
			if (CommonUtils.isNetworkConnected(mContext)) {
				intent.setClass(mContext, ExclusiveMemberActivity.class);
				startActivity(intent);
			} else {
				CustomToast.showToast(mContext,
						getResources().getString(R.string.network));
			}
			break;
		case R.id.oneKey_call_btn:
			if (CommonUtils.isNetworkConnected(mContext)) {
				if (!TextUtils.isEmpty(data.tel)) {
					CommonUtils.dialPhoneNumber(mContext, data.tel);
				} else {
					Toast.makeText(mContext,
							getResources().getString(R.string.empty_mobile),
							Toast.LENGTH_SHORT);
				}
			} else {
				CustomToast.showToast(mContext,
						getResources().getString(R.string.network));
			}

			break;
		}
	}
}
