package com.gj.gaojiaohui.activity;

import java.util.ArrayList;

import com.dxmap.indoornavig.NaviSearchActivity;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.ExhibitorsBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 展商详情-离线版
 * 
 * @author lixiaoming
 * 
 */
public class ExhibitorDetailForOfflineActivity extends Activity implements OnClickListener {

	private TextView titleTv;
	private ImageView back_img;
	private TextView exhibitor_title_tv;
	private TextView date_tv;
	private TextView content_tv;
	private TextView location_tv;
	private RelativeLayout location_rl;
	private TextView mobile_tv;
	private ImageView save_img;
	private ImageView dial_img;
	private LinearLayout translate_ll;
	private TextView industry_offline_text_tv;
	private TextView address_offline_text_tv;
	private TextView weburl_offline_text_tv;

	private Context mContext;
	private String exhibitorId;
	private DBHelper dbHelper;
	private ExhibitorsBeanForOffline bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibitor_detail_for_offline);
		mContext = this;
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bean = (ExhibitorsBeanForOffline) bundle.getSerializable("data");
		initView();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		titleTv = (TextView) findViewById(R.id.custom_title_tv);
		titleTv.setText(getResources().getString(R.string.exhibit_details));
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		exhibitor_title_tv = (TextView) findViewById(R.id.jianjie_title);
		date_tv = (TextView) findViewById(R.id.zhanshang_time);
		content_tv = (TextView) findViewById(R.id.xq_neirong);
		location_tv = (TextView) findViewById(R.id.weizhi_text);
		location_tv.setText(bean.position);
		location_rl = (RelativeLayout) findViewById(R.id.zhanshangweizhi_rl);
		location_rl.setOnClickListener(this);
		mobile_tv = (TextView) findViewById(R.id.zhanshang_phone_text);
		mobile_tv.setText(bean.tel);
		save_img = (ImageView) findViewById(R.id.zhanshang_phone_baocun);
		save_img.setOnClickListener(this);
		dial_img = (ImageView) findViewById(R.id.zhanshang_phone_image);
		dial_img.setOnClickListener(this);
		translate_ll = (LinearLayout) findViewById(R.id.xq_translate_ll);
		translate_ll.setOnClickListener(this);
		industry_offline_text_tv = (TextView) findViewById(R.id.industry_offline_text_tv);
		address_offline_text_tv = (TextView) findViewById(R.id.address_offline_text_tv);
		weburl_offline_text_tv = (TextView) findViewById(R.id.weburl_offline_text_tv);
		weburl_offline_text_tv.setText(bean.url);
		switch (GloableConfig.LANGUAGE_TYPE) {
		case "1":// 中文
			translate_ll.setVisibility(View.GONE);
			exhibitor_title_tv.setText(bean.name);
			content_tv.setText(bean.value);
			industry_offline_text_tv.setText(bean.lndustry);
			address_offline_text_tv.setText(bean.address);
			break;

		case "2":// 英文
			translate_ll.setVisibility(View.VISIBLE);
			exhibitor_title_tv.setText(bean.name_en);
			content_tv.setText(bean.value_en);
			industry_offline_text_tv.setText(bean.lndustry_en);
			address_offline_text_tv.setText(bean.address_en);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.zhanshang_phone_baocun:
			String phone = bean.tel;
			String title = "";
			if (GloableConfig.LANGUAGE_TYPE.equals("1")) {
				title = bean.name;
			} else if (GloableConfig.LANGUAGE_TYPE.equals("2")) {
				title = bean.name_en;
			}

			CommonUtils.saveTel(mContext, title, null, phone, null);
			break;
		case R.id.zhanshang_phone_image:
			String phoneNumber = bean.tel;
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.zhanshangweizhi_rl:
			Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
			intent1.putExtra("mapType", "indoor");
			intent1.putExtra("startid", "");
			intent1.putExtra("endid", bean.position);
			intent1.putExtra("endfid", "");
			startActivity(intent1);
			break;
		case R.id.xq_translate_ll:
			if (CommonUtils.isNetworkConnected(mContext)) {
				Intent intent_translate = new Intent(mContext, TranslateActivity.class);
				intent_translate.putExtra("value", bean.value);
				startActivity(intent_translate);
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.netless_toast));
			}
			break;
		}
	}
}
