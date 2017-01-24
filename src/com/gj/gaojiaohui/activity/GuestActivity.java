package com.gj.gaojiaohui.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.MettingDetailsBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 日程 - 会议详情
 * 
 * @author Administrator
 * 
 */
public class GuestActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView activity_fanhui;
	private GridView guest_gridview;
	private RelativeLayout activity_place_rl;
	/** 会议标题 */
	private TextView activity_convention;
	/** 会议内容 */
	private TextView convention_content;
	/** 是否参加 */
	private TextView activity_text;
	/** 会议开始时间 */
	private TextView activity_time;
	/** 会议结束时间 */
	private TextView activity_endTime_tv;
	/** 会议地点 */
	private TextView activity_place;
	/** 定位到 */
	private ImageView activity_place_image;

	Context mContext;
	private String id;
	private MettingDetailsBean data;
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

	// private List<MettingDetailsChildBean> list;
	// private MyGuestAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest);
		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		initView();
		getData();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.meeting_title));
		activity_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		activity_fanhui.setOnClickListener(this);
		activity_convention = (TextView) findViewById(R.id.activity_convention);
		convention_content = (TextView) findViewById(R.id.convention_content);
		activity_text = (TextView) findViewById(R.id.activity_text);
		activity_time = (TextView) findViewById(R.id.activity_time);
		activity_endTime_tv = (TextView) findViewById(R.id.activity_endTime_tv);
		activity_place = (TextView) findViewById(R.id.activity_place);
		activity_place_image = (ImageView) findViewById(R.id.activity_place_image);
		activity_place_rl = (RelativeLayout) findViewById(R.id.activity_place_rl);
		activity_place_rl.setOnClickListener(this);

		guest_gridview = (GridView) findViewById(R.id.guest_gridview);
		// list = new ArrayList<MettingDetailsChildBean>();
		// adapter = new MyGuestAdapter(mContext, list, R.layout.guest_item);
		// guest_gridview.setAdapter(adapter);
		// guest_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	/** 解析json数据 */
	private void parseData(Message msg) {

		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		data = CommonUtil.gson.fromJson(json, MettingDetailsBean.class);// 这段崩溃有3种原因
																		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			activity_convention.setText(data.meeting_title);
			convention_content.setText(data.meeting_value);
			if (data.meeting_join) {
				activity_text.setText(getResources().getString(R.string.have_to_attend));
			} else {
				activity_text.setText(getResources().getString(R.string.have_no_attend));
			}
			activity_time.setText(data.start_date);
			activity_endTime_tv.setText(data.end_date);
			activity_place.setText(data.meeting_site);
			// list.clear();
			// list.addAll(data.vip_list);
			// adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.MEETINT_DETAIL_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE, id);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;

		case R.id.activity_place_rl:
			// 跳转到地图定位
			if (CommonUtils.isNetworkConnected(mContext)) {
				if (data.type == 1) {// 室外
					if (TextUtils.isEmpty(data.latitude) || TextUtils.isEmpty(data.longitude)) {
						CustomToast.showToast(mContext, getResources().getString(R.string.location_empty));
					} else {
						Intent intent = new Intent(mContext, DaHuiNaviActivity.class);
						intent.putExtra("latitude", data.latitude);
						intent.putExtra("longitude", data.longitude);
						intent.putExtra("address", data.meeting_site);
						startActivity(intent);
					}
				} else if (data.type == 2) {// 室内
					Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
					intent1.putExtra("mapType", "indoor");
					intent1.putExtra("startid", "");
					intent1.putExtra("endid", data.tag);
					intent1.putExtra("endfid", "");
					startActivity(intent1);
				}

			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.network));
			}
			break;
		}
	}
}
