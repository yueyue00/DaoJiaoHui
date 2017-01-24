package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dxmap.indoornavig.NaviSearchActivity;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.BusinessAdapter;
import com.gj.gaojiaohui.adapter.MyZhanPinAdapter;
import com.gj.gaojiaohui.bean.BusinessInformationBean;
import com.gj.gaojiaohui.bean.BusinessInformationChildBean;
import com.gj.gaojiaohui.bean.ExhibitorsDetailsChildBean;
import com.gj.gaojiaohui.bean.MessageDetailsBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.CustomListview;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展商资料
 * 
 * @author Administrator
 * 
 */
public class BusinessInformationActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView business_fanhui;
	private TextView business_title;
	private TextView business_time;
	private TextView business_neirong;
	private TextView business_place;
	private CustomListview business_listview;
	private ImageView business_place_image;
	private RelativeLayout zhanshangweizhi;
	private ScrollView xq_scrollowView;
	private DisplayImageOptions options;
	private String id;
	private List<BusinessInformationChildBean> list;
	private BusinessAdapter adapter;
	Context mContext;
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
	private BusinessInformationBean data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_information);

		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("ExhibitId");
		initView();
		getData();
	}

	/** 解析json数据 */
	private void parseData(Message msg) {

		String json = msg.obj.toString();
		L.v("lixm", "展商资料数据:"+json);
		data = CommonUtil.gson.fromJson(json, BusinessInformationBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			business_title.setText(data.name);
			business_time.setText(data.date);
			business_neirong.setText(data.value);
			business_place.setText(data.position);
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.EXHIBITOR_DATA_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);// userid需为展商用户
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.business_information));
		business_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		business_title = (TextView) findViewById(R.id.business_title);
		business_time = (TextView) findViewById(R.id.business_time);
		business_place_image = (ImageView) findViewById(R.id.business_place_image);
		business_neirong = (TextView) findViewById(R.id.business_neirong);
		business_place = (TextView) findViewById(R.id.business_place);
		business_listview = (CustomListview) findViewById(R.id.business_listview);
		zhanshangweizhi = (RelativeLayout) findViewById(R.id.zhanshangweizhi);
		xq_scrollowView = (ScrollView) findViewById(R.id.xq_scrollowView);
		list = new ArrayList<BusinessInformationChildBean>();
		adapter = new BusinessAdapter(BusinessInformationActivity.this, list);
		business_listview.setAdapter(adapter);
		business_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String exhibitor_id = data.list.get(position).id;
				Intent intent = new Intent(BusinessInformationActivity.this, ZhanPinParticularsActivity.class);
				intent.putExtra("zhanPinId", exhibitor_id);
				startActivity(intent);
			}
		});
		business_fanhui.setOnClickListener(this);
		business_place_image.setOnClickListener(this);
		zhanshangweizhi.setOnClickListener(this);
		setListViewHeightBasedOnChildren(business_listview);
		disableAutoScrollToBottom();
	}

	/**
	 * 动态设置ListView的高度
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 400;
		listView.setLayoutParams(params);
	}

	private void disableAutoScrollToBottom() {
		xq_scrollowView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		xq_scrollowView.setFocusable(true);
		xq_scrollowView.setFocusableInTouchMode(true);
		xq_scrollowView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.zhanshangweizhi:// 展商位置定位
			Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
			intent1.putExtra("mapType", "indoor");
			intent1.putExtra("startid", "");
			intent1.putExtra("endid", data.position_map);
			intent1.putExtra("endfid", "");
			startActivity(intent1);
			break;
		default:
			break;
		}
	}
}
