package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.ServiceAdapter;
import com.gj.gaojiaohui.bean.ConferenceServiceBean;
import com.gj.gaojiaohui.bean.ConferenceServiceChildBean;
import com.gj.gaojiaohui.bean.SignUpListBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.huiyi.R;

/**
 * 会务总机
 * 
 * @author Administrator
 * 
 */
public class ConferenceActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private ImageView conference_fanhui;
	private ListView conference_listview;
	private List<ConferenceServiceChildBean> list;
	private Context mContext;
	private ServiceAdapter adpater;
	private String teltype; // 电话类型
	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conference);
		mContext = this;
		Intent intent = getIntent();
		teltype = intent.getStringExtra("teltype");
		initView();
		getData();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.busness_operator));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		conference_listview = (ListView) findViewById(R.id.conference_listview);

		conference_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 打电话
				CommonUtils.dialPhoneNumber(mContext, list.get(position).tel);
			}
		});
		list = new ArrayList<>();
		adpater = new ServiceAdapter(mContext, list, R.layout.service_item);
		conference_listview.setAdapter(adpater);

		switch (teltype) {
		case "1":
			title.setText(R.string.consulting_the_reception);
			break;
		case "2":
			title.setText(R.string.ticket_consultation);
			break;
		case "3":
			title.setText(R.string.professional_audience);
			break;
		case "4":
			title.setText(R.string.convention_and_exhibition_center_complaints);
			break;
		case "5":
			title.setText(R.string.the_food_hygiene);
			break;
		case "6":
			title.setText(R.string.on_site_medical_care);
			break;
		case "7":
			title.setText(R.string.security_alarm);
			break;
		case "8":
			title.setText(R.string.fire_alarm);
			break;
		default:
			break;
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.TEL_URL, GloableConfig.userid, teltype, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				list.clear();
				List<ConferenceServiceChildBean> tmpList = CommonUtils.gson.fromJson(jsonObject.getString("list"),
						new TypeToken<List<ConferenceServiceChildBean>>() {
						}.getType());
				list.addAll(tmpList);
				adpater.notifyDataSetChanged();
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
			}
		} catch (JSONException e) {
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
			e.printStackTrace();
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
