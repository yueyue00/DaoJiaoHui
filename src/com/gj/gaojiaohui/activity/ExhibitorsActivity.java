package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MyExhibitorsAdapter;
import com.gj.gaojiaohui.bean.HuiZhanDetailsBean;
import com.gj.gaojiaohui.bean.HuiZhanDetailsChildBean;
import com.gj.gaojiaohui.bean.MettingDetailsBean;
import com.gj.gaojiaohui.bean.MettingDetailsChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展商详情--会展
 */
public class ExhibitorsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private ListView exhibitors_listview;
	private ImageView schedule_fanhui;
	private TextView title_tv;
	private RelativeLayout convention_place_rl;
	/** 会展标题 */
	private TextView activity_convention;
	/** 会展内容 */
	private TextView convention_content;
	/** 是否参加 */
	private TextView exhibitors_text;
	/** 会展时间 */
	private TextView convention_time;
	/** 会展地点 */
	private TextView convention_place;
	/** 定位到 */
	private ImageView convention_place_image;

	private List<HuiZhanDetailsChildBean> list;
	Context mContext;
	private String id;
	private HuiZhanDetailsBean data;
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
	private MyExhibitorsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibitors);

		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		initView();
		getData();
	}

	/** 解析json数据 */
	private void parseData(Message msg) {

		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		data = CommonUtil.gson.fromJson(json, HuiZhanDetailsBean.class);// 这段崩溃有3种原因
																		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			activity_convention.setText(data.meeting_title);
			convention_content.setText(data.meeting_value);
			if (data.meeting_join) {
				exhibitors_text.setText(getResources().getString(R.string.have_to_attend));
			} else {
				exhibitors_text.setText(getResources().getString(R.string.have_no_attend));
			}
			convention_time.setText(data.meeting_date);
			convention_place.setText(data.meeting_site);
			list.clear();
			list.addAll(data.vip_list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		// String url =
		// "http://172.20.15.2:8080/gaojiao/meetings.do?method=huiyiDetail&userid=sunjd&language="+GloableConfig.LANGUAGE_TYPE+"&id=28601";
		String url = String.format(GloableConfig.MEETINT_DETAIL_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE, id);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	private void initView() {
		exhibitors_listview = (ListView) findViewById(R.id.exhibitors_listview);
		schedule_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		schedule_fanhui.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.convention_title));
		convention_place_rl = (RelativeLayout) findViewById(R.id.convention_place_rl);
		convention_place_rl.setOnClickListener(this);
		activity_convention = (TextView) findViewById(R.id.activity_convention);
		convention_content = (TextView) findViewById(R.id.convention_content);
		exhibitors_text = (TextView) findViewById(R.id.exhibitors_text);
		convention_time = (TextView) findViewById(R.id.convention_time);
		convention_place = (TextView) findViewById(R.id.convention_place);
		convention_place_image = (ImageView) findViewById(R.id.convention_place_image);

		list = new ArrayList<HuiZhanDetailsChildBean>();
		adapter = new MyExhibitorsAdapter(mContext, list, R.layout.exhibitors_item);
		exhibitors_listview.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.convention_place_rl:
			// 跳转到地图定位
			if (CommonUtils.isNetworkConnected(mContext)) {
				if (TextUtils.isEmpty(data.latitude) || TextUtils.isEmpty(data.longitude)) {
					CustomToast.showToast(mContext, getResources().getString(R.string.location_empty));
				} else {
					Intent intent = new Intent(mContext, DaHuiNaviActivity.class);
					intent.putExtra("latitude", data.latitude);
					intent.putExtra("longitude", data.longitude);
					intent.putExtra("address", data.meeting_site);
					startActivity(intent);
				}
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.network));
			}
			break;
		}
	}
}
