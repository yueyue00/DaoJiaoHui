package com.gj.gaojiaohui.activity;

import io.rong.imlib.model.Conversation.ConversationType;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.widget.MyListView;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.GuestScheduleAdapter;
import com.gj.gaojiaohui.bean.GuestInfoBean;
import com.gj.gaojiaohui.bean.GuestInfosBean;
import com.gj.gaojiaohui.bean.GuestSchedule;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.RongUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

//嘉宾详情界面-- zyj
public class GuestInfoActivity extends GaoJiaoHuiBaseActivity {

	Context mContext;
	MyListView guest_xingcheng_lv;
	GuestScheduleAdapter scheduleAdapter;
	ArrayList<GuestSchedule> datalists = new ArrayList<>();
	GuestInfoBean guestinfo = null;
	//
	ImageView guest_icon_iv, icon_guest_introduction, icon_guest_xingcheng, custom_back_img;
	TextView guest_name_tv, guest_mobile_tv, guest_position_tv, guest_introduction_tv, tv_guest_xingcheng, guest_workplace_tv, custom_title_tv;
	Button guest_sms_btn, guest_call_btn, guest_sendinfo_btn;
	RelativeLayout rl_guest_xingcheng;
	private String tag;
	/** 请求回的数据进行解析 */
	Handler guestinfo_handler = new Handler() {
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

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		if (json != null && !json.equals("")) {
			// 通过gson将json映射成实体类
			GuestInfosBean guestInfosBean = CommonUtil.gson.fromJson(json, GuestInfosBean.class);// 这段崩溃有3种原因
			// 1/字段名缺失,2/格式类型不对,3/字段null了
			switch (guestInfosBean.resultCode) {
			case "200":
				datalists.clear();
				datalists.addAll(guestInfosBean.list);
				scheduleAdapter.notifyDataSetChanged();
				//
				guestinfo = guestInfosBean.data;
				guest_name_tv.setText(guestInfosBean.data.name);
				guest_mobile_tv.setText(guestInfosBean.data.tel);
				guest_position_tv.setText(guestInfosBean.data.position);
				guest_workplace_tv.setText(guestInfosBean.data.company);
				guest_introduction_tv.setText(guestInfosBean.data.value);
				//
				// ImageLoader.getInstance().displayImage(guestInfosBean.data.pic,
				// guest_icon_iv, options);
				break;

			default:
				break;
			}
		}
	}

	DisplayImageOptions options;
	String userid = "";
	String language = "";
	String type = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guestinfo);
		mContext = GuestInfoActivity.this;
		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");
		L.v("lixm", "嘉宾详情tag:" + tag);
		SharePreferenceUtils.getAppConfig(this);
		options = ImageLoaderUtils.initOptionsForCirclePic(R.drawable.pic_vip, 40);
		process();
		initView();
		String murl = String.format(GloableConfig.GUESTS_INFO_URL, userid, language, type, tag);
		L.v("lixm", "嘉宾详情 url:" + murl);
		Constant.getDataByVolley(mContext, murl, guestinfo_handler, 0);
		setAllClick();
	}

	private void process() {
		if (getIntent().getStringExtra("userid") != null)
			userid = getIntent().getStringExtra("userid");
		language = (String) SharePreferenceUtils.getParam("language", "");
		if (language.equals("en")) {
			language = "2";
		} else {
			language = "1";
		}
		if (getIntent().getStringExtra("type") != null) {
			switch (getIntent().getStringExtra("type")) {// 获取type类型
			case "liebiao":
				type = "0";
				break;
			case "jiaoliu":
				type = "1";
				break;
			default:
				break;
			}
		}
	}

	private void initView() {
		guest_xingcheng_lv = (MyListView) findViewById(R.id.guest_xingcheng_lv);
		guest_xingcheng_lv.setFocusable(false);
		guest_icon_iv = (ImageView) findViewById(R.id.guest_icon_iv);
		icon_guest_introduction = (ImageView) findViewById(R.id.icon_guest_introduction);
		icon_guest_xingcheng = (ImageView) findViewById(R.id.icon_guest_xingcheng);
		custom_back_img = (ImageView) findViewById(R.id.custom_back_img);

		guest_name_tv = (TextView) findViewById(R.id.guest_name_tv);
		guest_mobile_tv = (TextView) findViewById(R.id.guest_mobile_tv);
		guest_position_tv = (TextView) findViewById(R.id.guest_position_tv);
		guest_introduction_tv = (TextView) findViewById(R.id.guest_introduction_tv);
		tv_guest_xingcheng = (TextView) findViewById(R.id.tv_guest_xingcheng);
		tv_guest_xingcheng.setText(getResources().getString(R.string.guestinfo_guest_xingcheng));
		guest_workplace_tv = (TextView) findViewById(R.id.guest_workplace_tv);
		custom_title_tv = (TextView) findViewById(R.id.custom_title_tv);
		custom_title_tv.setText(R.string.title_guestinfo);
		guest_sms_btn = (Button) findViewById(R.id.guest_sms_btn);
		guest_call_btn = (Button) findViewById(R.id.guest_call_btn);
		guest_sendinfo_btn = (Button) findViewById(R.id.guest_sendinfo_btn);
		rl_guest_xingcheng = (RelativeLayout) findViewById(R.id.rl_guest_xingcheng);
		if (type.equals("0")) {// 嘉宾列表
			rl_guest_xingcheng.setVisibility(View.VISIBLE);
		} else if (type.equals("1")) {// 嘉宾交流
			rl_guest_xingcheng.setVisibility(View.GONE);
		}
		//
		// InitData();
		scheduleAdapter = new GuestScheduleAdapter(mContext, datalists);
		guest_xingcheng_lv.setAdapter(scheduleAdapter);
	}

	private void setAllClick() {
		guest_sms_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (guestinfo != null)
					doSendSMSTo(guestinfo.tel, "");
			}
		});
		guest_call_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (guestinfo != null) {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + guestinfo.tel));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		});
		guest_sendinfo_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RongUtil.startChat(mContext, ConversationType.PRIVATE, guestinfo.id, guestinfo.name);
			}
		});
		custom_back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void doSendSMSTo(String phoneNumber, String message) {
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
			intent.putExtra("sms_body", message);
			mContext.startActivity(intent);
		}
	}
}
