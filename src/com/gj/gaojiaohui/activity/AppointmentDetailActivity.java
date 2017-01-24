package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dxmap.indoornavig.NaviSearchActivity;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MyZhanPinAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsDetailsBean;
import com.gj.gaojiaohui.bean.ExhibitorsDetailsChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.QingLiuYanDialog;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.CustomListview;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 采访预约详情
 * 
 * @author lixiaoming
 * 
 */
public class AppointmentDetailActivity extends Activity implements OnClickListener {

	private ImageView img_back;
	private TextView title_tv;
	private ScrollView scrollView;
	private TextView detail_title_tv;
	private TextView date_tv;
	private LinearLayout appointment_ll;
	private ImageView appointment_img;
	private TextView appointment_tv;
	private LinearLayout leavemessage_ll;
	private TextView content_tv;
	private RelativeLayout location_rl;
	private TextView location_tv;
	private ImageView location_img;
	private TextView mobile_tv;
	private ImageView mobile_save_img;
	private ImageView mobile_call_img;
	private CustomListview exhibits_lv;

	private String exhibitId;
	private Context mContext;
	private ExhibitorsDetailsBean detailsBean;
	private List<ExhibitorsDetailsChildBean> list;
	private MyZhanPinAdapter adapter;
	/** 判断数据请求是否成功了 成功了不可以再次请求 */
	private boolean isNetComplete = true;
	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 1002:
				parseData(msg);
				break;
			case 1003:
				// 取消预约
				parseFollowData(msg);
				break;
			case 1004:
				// 预约
				ProgressUtil.dismissProgressDialog();
				parseFollowData(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_detail);
		mContext = this;
		Intent intent = getIntent();
		exhibitId = intent.getStringExtra("id");
		initView();
		getData();
	}

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// if (isNetComplete) {// 正在请求中
	//
	// } else {// 已经请求完
	// getData();
	// }
	// }

	/**
	 * 初始化view
	 */
	private void initView() {
		img_back = (ImageView) findViewById(R.id.custom_back_img);
		img_back.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.appointment_detail_title));
		scrollView = (ScrollView) findViewById(R.id.appointmentDetail_scrollview);
		detail_title_tv = (TextView) findViewById(R.id.appointmentDetail_title_tv);
		date_tv = (TextView) findViewById(R.id.appointmentDetail_date_tv);
		appointment_ll = (LinearLayout) findViewById(R.id.appointmentDetail_order_ll);
		appointment_ll.setOnClickListener(this);
		appointment_img = (ImageView) findViewById(R.id.appointmentDetail_order_img);
		appointment_tv = (TextView) findViewById(R.id.appointmentDetail_order_tv);
		leavemessage_ll = (LinearLayout) findViewById(R.id.appointmentDetail_leavemessage_ll);
		leavemessage_ll.setOnClickListener(this);
		content_tv = (TextView) findViewById(R.id.appointmentDetail_content_tv);
		location_rl = (RelativeLayout) findViewById(R.id.appointmentDetail_location_rl);
		location_rl.setOnClickListener(this);
		location_tv = (TextView) findViewById(R.id.appointmentDetail_location_tv);
		location_img = (ImageView) findViewById(R.id.appointmentDetail_location_img);
		mobile_tv = (TextView) findViewById(R.id.appointmentDetail_mobile_tv);
		mobile_call_img = (ImageView) findViewById(R.id.appointmentDetail_mobile_img);
		mobile_call_img.setOnClickListener(this);
		mobile_save_img = (ImageView) findViewById(R.id.appointmentDetail_save_img);
		mobile_save_img.setOnClickListener(this);
		exhibits_lv = (CustomListview) findViewById(R.id.appointmentDetail_exhibits_lv);
		setListViewHeightBasedOnChildren(exhibits_lv);
		disableAutoScrollToBottom();
		list = new ArrayList<ExhibitorsDetailsChildBean>();
		adapter = new MyZhanPinAdapter(mContext, list);
		exhibits_lv.setAdapter(adapter);
		exhibits_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String exhibitId = detailsBean.list.get(position).id;
				Intent intent = new Intent(mContext, ZhanPinParticularsActivity.class);
				intent.putExtra("zhanPinId", exhibitId);
				startActivity(intent);
			}
		});
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "采访预约详情json:"+json);
		detailsBean = CommonUtil.gson.fromJson(json, ExhibitorsDetailsBean.class);
		if (detailsBean.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(detailsBean.list);

			detail_title_tv.setText(detailsBean.name);
			date_tv.setText(detailsBean.date);
			content_tv.setText(detailsBean.value);
			location_tv.setText(detailsBean.position);
			mobile_tv.setText(detailsBean.tel);
			if (detailsBean.follow) {
				appointment_tv.setText(getResources().getString(R.string.appointment_in));
				appointment_tv.setSelected(false);
				appointment_img.setImageResource(R.drawable.zsxq_btn_guanzhu_sel);
			} else {
				appointment_tv.setText(getResources().getString(R.string.appointment_text));
				appointment_tv.setSelected(true);
				appointment_img.setImageResource(R.drawable.zsxq_btn_guanzhu_nor);
			}
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.APPOINTMENT_URL, GloableConfig.userid, exhibitId, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
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
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 200;
		listView.setLayoutParams(params);
	}

	/**
	 * 禁止ScrollView的childview自动滑动到底部
	 */
	private void disableAutoScrollToBottom() {
		scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollView.setFocusable(true);
		scrollView.setFocusableInTouchMode(true);
		scrollView.setOnTouchListener(new View.OnTouchListener() {
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
		case R.id.appointmentDetail_order_ll:
			operator();
			break;
		case R.id.appointmentDetail_leavemessage_ll:
			if (detailsBean.follow) {
				Intent intent_liuyan = new Intent(this, AppointmentLeaveMessageActivity.class);
				intent_liuyan.putExtra("id", exhibitId);
				intent_liuyan.putExtra("isExhibitors", 0);
				intent_liuyan.putExtra("type", "6");
				startActivity(intent_liuyan);
			} else {
				final QingLiuYanDialog qingliuyan = new QingLiuYanDialog(mContext);
				qingliuyan.show();
				TextView dialog_content = (TextView) qingliuyan.findViewById(R.id.dialog_content);
				dialog_content.setText(getResources().getString(R.string.dialog_appointment_content));
				TextView quxiao_text = (TextView) qingliuyan.findViewById(R.id.quxiao_text);
				quxiao_text.setText(getResources().getString(R.string.cancel));
				final TextView qingguanzhu_text = (TextView) qingliuyan.findViewById(R.id.qingguanzhu_text);
				qingguanzhu_text.setText(getResources().getString(R.string.appointment_text));
				quxiao_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						qingliuyan.dismiss();
					}
				});
				qingguanzhu_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!TextUtils.isEmpty(GloableConfig.userid)) {
							// userid不为空证明已经登录
							appointment_tv.setText(getResources().getString(R.string.appointment_in));
							appointment_tv.setSelected(false);
							appointment_img.setImageResource(R.drawable.zsxq_btn_guanzhu_sel);
							qingliuyan.dismiss();
							// 执行预约操作
							VolleyUtil volleyUtil = new VolleyUtil(mContext);
							volleyUtil.onStop();
							String url = String.format(GloableConfig.APPOINTMENT_DETAIL_APPOINTMENT_URL, GloableConfig.userid, detailsBean.id);
							loadData(url, "采访预约详情==关注 de url:", 1004);
						} else {
							// 没有登录直接跳登录界面
							Intent intent = new Intent(mContext, LoginActivity.class);
							mContext.startActivity(intent);

						}

					}
				});
			}
			break;
		case R.id.appointmentDetail_location_rl:
			Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
			intent1.putExtra("mapType", "indoor");
			intent1.putExtra("startid", "");
			intent1.putExtra("endid", detailsBean.position_map);
			intent1.putExtra("endfid", "");
			startActivity(intent1);
			break;
		case R.id.appointmentDetail_mobile_img:
			String mobile = mobile_tv.getText().toString();
			CommonUtils.dialPhoneNumber(mContext, mobile);
			break;
		case R.id.appointmentDetail_save_img:
			String phone = mobile_tv.getText().toString();
			String title = detail_title_tv.getText().toString();
			CommonUtils.saveTel(mContext, title, null, phone, null);
			break;

		}
	}

	/** 解析json数据 */
	private void parseFollowData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "采访预约详情-预约结果:"+json);
		// 通过gson将json映射成实体类
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 预约成功
			VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
			Map<String, String> map = new HashMap<>();// Map为空，必须要有
			String url = String.format(GloableConfig.APPOINTMENT_URL, GloableConfig.userid, exhibitId, GloableConfig.LANGUAGE_TYPE);
			volleyUtil1.stringRequest(handler, url, map, 1002);// 1002是msg.what
		} else {
			/** 请求失败的处理 */
			// 预约失败
		}
	}

	/** 请求网络数据 */
	private void loadData(String url, String text, int msgWhat) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, msgWhat);// 1001是msg.what
	}

	/** 关注/取消关注操作 */
	private void operator() {
		if (detailsBean.follow) {
			// appointment_tv.setText(getResources().getString(R.string.appointment_text));
			// appointment_tv.setSelected(true);
			// appointment_img.setImageResource(R.drawable.zsxq_btn_guanzhu_nor);
			// // 取消关注
			// VolleyUtil volleyUtil = new VolleyUtil(mContext);
			// volleyUtil.onStop();
			// String url =
			// String.format(GloableConfig.CANCLE_FOLLOW_ZHANSHANG_URL,
			// GloableConfig.userid, detailsBean.id);
			// loadData(url, "采访预约==取消预约 de url:", 1003);
			// CustomToast.showToast(mContext, "您已经预约该展商,不能取消预约");
		} else {
			if (!TextUtils.isEmpty(GloableConfig.userid)) {
				// userid不为空证明已经登录
				appointment_tv.setText(getResources().getString(R.string.appointment_in));
				appointment_tv.setSelected(false);
				appointment_img.setImageResource(R.drawable.zsxq_btn_guanzhu_sel);
				// 预约
				VolleyUtil volleyUtil = new VolleyUtil(mContext);
				volleyUtil.onStop();
				String url = String.format(GloableConfig.APPOINTMENT_DETAIL_APPOINTMENT_URL, GloableConfig.userid, detailsBean.id);
				loadData(url, "采访预约==预约 的 url:", 1004);
			} else {
				// 没有登录直接跳登录界面
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);

			}

		}
	}

	/** 触摸空白区域关闭软键盘 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		}
		return false;
	}
}
