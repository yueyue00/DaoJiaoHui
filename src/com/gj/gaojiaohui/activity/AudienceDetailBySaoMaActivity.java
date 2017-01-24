package com.gj.gaojiaohui.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众详情-扫描进入 扫码详情 界面 add zyj
 */
public class AudienceDetailBySaoMaActivity extends GaoJiaoHuiBaseActivity {
	Context mContext;
	ImageView guest_icon_iv, custom_back_img;
	TextView guest_name_tv, guest_mobile_tv, guest_workplace_tv, custom_title_tv, follow_tv;
	Button follow_btn, save_btn;
	LinearLayout follow_linear;
	DisplayImageOptions options;
	String ScanValue = "";
	String name = "";
	String org = "";
	String tel = "";
	String email = "";
	String exhibitId = "";
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == 1001) {
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						CustomToast.showToast(mContext, getString(R.string.guanzhu_success));
					}else {
						CustomToast.showToast(mContext, getString(R.string.getdata_error));
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getString(R.string.getdata_error));
					e.printStackTrace();
				}
				
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audience_detail_bysaoma);
		SharePreferenceUtils.getAppConfig(this);
		mContext = AudienceDetailBySaoMaActivity.this;
		options = ImageLoaderUtils.initOptions();
		Intent intent = getIntent();
		ScanValue = intent.getStringExtra("ScanValue");
		initView();
		parseData();
		setAllClick();
	}

	private void initView() {
		guest_icon_iv = (ImageView) findViewById(R.id.guest_icon_iv);
		custom_back_img = (ImageView) findViewById(R.id.custom_back_img);

		guest_name_tv = (TextView) findViewById(R.id.guest_name_tv);
		guest_mobile_tv = (TextView) findViewById(R.id.guest_mobile_tv);
		guest_workplace_tv = (TextView) findViewById(R.id.guest_workplace_tv);
		custom_title_tv = (TextView) findViewById(R.id.custom_title_tv);
		follow_tv = (TextView) findViewById(R.id.follow_tv);
		follow_linear = (LinearLayout) findViewById(R.id.follow_linear);
		custom_title_tv.setText(getResources().getString(R.string.audience_detail));
		follow_btn = (Button) findViewById(R.id.follow_btn);
		save_btn = (Button) findViewById(R.id.save_btn);
	}

	/**
	 * 解析并且填充数据
	 */
	private void parseData() {
		try {
			JSONObject jsonObject = new JSONObject(ScanValue);
			if (jsonObject.has("NOTE")) {
				exhibitId = jsonObject.getString("NOTE");
				follow_linear.setVisibility(View.VISIBLE);
				follow_tv.setVisibility(View.VISIBLE);
			}
			if (jsonObject.has("FN")) {
				name = jsonObject.getString("FN");
				guest_name_tv.setText(name);
			}
			if (jsonObject.has("ORG")) {
				org = jsonObject.getString("ORG");
				guest_workplace_tv.setText(org);
			}
			if (jsonObject.has("TEL;WORK")) {
				tel = jsonObject.getString("TEL;WORK");
				if (!StringUtils.isNull(tel)) {
					guest_mobile_tv.setText(tel);
				}
			}
			if (jsonObject.has("TEL;CELL")) {
				tel = jsonObject.getString("TEL;CELL");
				if (!StringUtils.isNull(tel)) {
					guest_mobile_tv.setText(tel);
				}
			}
			if (jsonObject.has("TEL")) {
				tel = jsonObject.getString("TEL");
				if (!StringUtils.isNull(tel)) {
					guest_mobile_tv.setText(tel);
				}
			}
			if (jsonObject.has("EMAIL")) {
				email = jsonObject.getString("EMAIL");
				if (!StringUtils.isNull(email)) {
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setAllClick() {
		follow_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO 执行关注
				VolleyUtil volleyUtil = new VolleyUtil(mContext);
				Map<String, String> map = new HashMap<>();
				String url = String.format(GloableConfig.FOLLOW_ZHANSHANG_URL, GloableConfig.userid, exhibitId);
				volleyUtil.stringRequest(handler, url, map, 1001);
			}
		});
		save_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 执行保存
				CommonUtils.saveTel(mContext, name, email, tel, null);
			}
		});
		custom_back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// public void writeContacts(String username, String mobile) {
	// try {
	// ContentResolver resolver = getContentResolver();
	// Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
	// Uri dataUri = Uri.parse("content://com.android.contacts/data");
	// // 查出最后一个id
	// Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
	// null);
	// cursor.moveToLast();
	// int lastId = cursor.getInt(0);
	// int newId = lastId + 1;
	// // 插入一个联系人id
	// ContentValues values = new ContentValues();
	// values.put("contact_id", newId);
	// resolver.insert(uri, values);
	// // 插入电话数据
	// ContentValues dataValues = new ContentValues();
	// dataValues.put("raw_contact_id", newId);
	// dataValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
	// dataValues.put("data1", mobile);
	// resolver.insert(dataUri, dataValues);
	// // 插入姓名数据
	// ContentValues data1Values = new ContentValues();
	// data1Values.put("raw_contact_id", newId);
	// data1Values.put("mimetype", "vnd.android.cursor.item/name");
	// data1Values.put("data1", username);
	// resolver.insert(dataUri, data1Values);
	// } catch (Exception e) {
	// }
	//
	// }
}
