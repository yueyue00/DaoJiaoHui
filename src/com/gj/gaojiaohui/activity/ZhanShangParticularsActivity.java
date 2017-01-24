package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MyZhanPinAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsDetailsBean;
import com.gj.gaojiaohui.bean.ExhibitorsDetailsChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.QingLiuYanDialog;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.CustomListview;
import com.smartdot.wenbo.huiyi.R;

/***
 * 展商详情
 * 
 * @author Administrator
 * 
 */
public class ZhanShangParticularsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private ImageView zsxq_fanhui;
	private TextView liuyan_text;
	private String id;
	private TextView jianjie_title;
	private TextView guanzhu_text;
	private ImageView xq_guanzhu;
	private TextView xq_neirong;
	private TextView weizhi_text;
	private TextView zhanshang_phone_text;
	private CustomListview zhanpin_listview;
	private LinearLayout liuyan_ll;
	private LinearLayout guanzhu_ll;
	private LinearLayout xq_translate_ll;
	private ImageView weizhi_image;
	private ImageView zhanshang_phone_image;
	private ImageView zhanshang_phone_baocun;
	private RelativeLayout zhanshangweizhi_rl;
	private RelativeLayout zhanshangphone_rl;
	private ScrollView xq_scrollview;
	private TextView industry_text_tv;
	private TextView address_text_tv;
	private TextView weburl_text_tv;

	private String phoneNumber;
	private List<ExhibitorsDetailsChildBean> list;
	private MyZhanPinAdapter adapter;
	private TextView zhanshang_time;
	private ExhibitorsDetailsBean detailsBean;
	int positions;
	Context mContext;
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
				// 取消关注
				parseFollowData(msg);
				break;
			case 1004:
				// 关注
				parseFollowData(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhan_shang_particulars);

		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("exhibitorId");
		initView();
		getData();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isNetComplete) {// 正在请求中

		} else {// 已经请求完
			getData();
		}
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.exhibit_details));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		zhanshangweizhi_rl = (RelativeLayout) findViewById(R.id.zhanshangweizhi_rl);
		zhanshangphone_rl = (RelativeLayout) findViewById(R.id.zhanshangphone_rl);
		guanzhu_text = (TextView) findViewById(R.id.guanzhu_text);
		liuyan_text = (TextView) findViewById(R.id.liuyan_text);
		jianjie_title = (TextView) findViewById(R.id.jianjie_title);
		xq_guanzhu = (ImageView) findViewById(R.id.xq_guanzhu);
		xq_neirong = (TextView) findViewById(R.id.xq_neirong);
		weizhi_text = (TextView) findViewById(R.id.weizhi_text);
		zhanshang_time = (TextView) findViewById(R.id.zhanshang_time);
		zhanshang_phone_text = (TextView) findViewById(R.id.zhanshang_phone_text);
		zhanpin_listview = (CustomListview) findViewById(R.id.zhanpin_listview);
		liuyan_ll = (LinearLayout) findViewById(R.id.liuyan_ll);
		guanzhu_ll = (LinearLayout) findViewById(R.id.guanzhu_ll);
		xq_translate_ll = (LinearLayout) findViewById(R.id.xq_translate_ll);
		if (GloableConfig.LANGUAGE_TYPE.equals("2")) {// 英文
			xq_translate_ll.setVisibility(View.VISIBLE);
		} else if (GloableConfig.LANGUAGE_TYPE.equals("1")) {// 中文
			xq_translate_ll.setVisibility(View.GONE);
		}
		weizhi_image = (ImageView) findViewById(R.id.weizhi_image);
		zhanshang_phone_image = (ImageView) findViewById(R.id.zhanshang_phone_image);
		zhanshang_phone_baocun = (ImageView) findViewById(R.id.zhanshang_phone_baocun);
		xq_scrollview = (ScrollView) findViewById(R.id.xq_scrollview);
		industry_text_tv = (TextView) findViewById(R.id.industry_text_tv);
		address_text_tv = (TextView) findViewById(R.id.address_text_tv);
		weburl_text_tv = (TextView) findViewById(R.id.weburl_text_tv);
		setListViewHeightBasedOnChildren(zhanpin_listview);
		disableAutoScrollToBottom();

		zhanshangweizhi_rl.setOnClickListener(this);
		zhanshangphone_rl.setOnClickListener(this);
		liuyan_ll.setOnClickListener(this);
		guanzhu_ll.setOnClickListener(this);
		xq_translate_ll.setOnClickListener(this);
		zhanshang_phone_image.setOnClickListener(this);
		zhanshang_phone_baocun.setOnClickListener(this);

		list = new ArrayList<ExhibitorsDetailsChildBean>();
		adapter = new MyZhanPinAdapter(ZhanShangParticularsActivity.this, list);
		zhanpin_listview.setAdapter(adapter);
		zhanpin_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String exhibitId = detailsBean.list.get(position).id;
				Intent intent = new Intent(ZhanShangParticularsActivity.this, ZhanPinParticularsActivity.class);
				intent.putExtra("zhanPinId", exhibitId);
				startActivity(intent);
			}
		});
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		detailsBean = CommonUtil.gson.fromJson(json, ExhibitorsDetailsBean.class);
		if (detailsBean.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(detailsBean.list);

			jianjie_title.setText(detailsBean.name);
			zhanshang_time.setText(detailsBean.date);
			xq_neirong.setText(detailsBean.value);
			weizhi_text.setText(detailsBean.position);
			zhanshang_phone_text.setText(detailsBean.tel);
			industry_text_tv.setText(detailsBean.lndustry);
			address_text_tv.setText(detailsBean.address);
			weburl_text_tv.setText(detailsBean.url);
			if (detailsBean.follow) {
				guanzhu_text.setText(getResources().getString(R.string.have_follow));
				guanzhu_text.setSelected(false);
				xq_guanzhu.setImageResource(R.drawable.zsxq_btn_guanzhu_sel);
			} else {
				guanzhu_text.setText(getResources().getString(R.string.exhibit_attention));
				guanzhu_text.setSelected(true);
				xq_guanzhu.setImageResource(R.drawable.zsxq_btn_guanzhu_nor);
			}
			adapter.notifyDataSetChanged();
			// 刷新数据后需要重新绘制listview 的高度
			setListViewHeightBasedOnChildren(zhanpin_listview);
			disableAutoScrollToBottom();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.ZHANSHANG_DETAIL_URL, GloableConfig.userid, id, GloableConfig.LANGUAGE_TYPE);
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
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 300;
		listView.setLayoutParams(params);
	}

	/**
	 * 禁止ScrollView的childview自动滑动到底部
	 */
	private void disableAutoScrollToBottom() {
		xq_scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		xq_scrollview.setFocusable(true);
		xq_scrollview.setFocusableInTouchMode(true);
		xq_scrollview.setOnTouchListener(new View.OnTouchListener() {
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
		case R.id.liuyan_ll:
			if (detailsBean.follow) {
				Intent intent_liuyan = new Intent(this, LiuYanParticularsActivity.class);
				intent_liuyan.putExtra("id", id);
				intent_liuyan.putExtra("isExhibitors", 0);
				intent_liuyan.putExtra("type", "2");
				startActivity(intent_liuyan);
			} else {
				final QingLiuYanDialog qingliuyan = new QingLiuYanDialog(ZhanShangParticularsActivity.this);
				qingliuyan.show();
				TextView dialog_content = (TextView) qingliuyan.findViewById(R.id.dialog_content);
				dialog_content.setText(getResources().getString(R.string.dialog_follow_content));
				TextView quxiao_text = (TextView) qingliuyan.findViewById(R.id.quxiao_text);
				quxiao_text.setText(getResources().getString(R.string.cancel));
				final TextView qingguanzhu_text = (TextView) qingliuyan.findViewById(R.id.qingguanzhu_text);
				qingguanzhu_text.setText(getResources().getString(R.string.follow));
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
							guanzhu_text.setText(getResources().getString(R.string.have_follow));
							guanzhu_text.setSelected(false);
							xq_guanzhu.setImageResource(R.drawable.zsxq_btn_guanzhu_sel);
							qingliuyan.dismiss();
							// 执行关注操作
							VolleyUtil volleyUtil = new VolleyUtil(mContext);
							volleyUtil.onStop();
							String url = String.format(GloableConfig.FOLLOW_ZHANSHANG_URL, GloableConfig.userid, detailsBean.id);
							loadData(url, "展商搜索==关注 de url:", 1004);
						} else {
							// 没有登录直接跳登录界面
							Intent intent = new Intent(mContext, LoginActivity.class);
							mContext.startActivity(intent);

						}

					}
				});
			}
			break;
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.guanzhu_ll:
			operator();
			break;
		case R.id.zhanshang_phone_image:
			phoneNumber = zhanshang_phone_text.getText().toString();
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.zhanshangweizhi_rl:
			if(detailsBean != null){
				Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
				intent1.putExtra("mapType", "indoor");
				intent1.putExtra("startid", "");
				intent1.putExtra("endid", detailsBean.position_map);
				intent1.putExtra("endfid", "");
				startActivity(intent1);
			}			
			break;
		case R.id.zhanshangphone_rl:

			break;
		case R.id.zhanshang_phone_baocun:
			String phone = zhanshang_phone_text.getText().toString();
			String title = jianjie_title.getText().toString();
			CommonUtils.saveTel(ZhanShangParticularsActivity.this, title, null, phone, null);
			break;
		case R.id.xq_translate_ll:
			if (CommonUtils.isNetworkConnected(mContext)) {
				Intent intent_translate = new Intent(mContext, TranslateActivity.class);
				intent_translate.putExtra("value", detailsBean.value);
				startActivity(intent_translate);
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.netless_toast));
			}

			break;
		}
	}

	/** 解析json数据 */
	private void parseFollowData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
			Map<String, String> map = new HashMap<>();// Map为空，必须要有
			String url = String.format(GloableConfig.ZHANSHANG_DETAIL_URL, GloableConfig.userid, id, GloableConfig.LANGUAGE_TYPE);
			volleyUtil1.stringRequest(handler, url, map, 1002);// 1002是msg.what
		} else {
			/** 请求失败的处理 */
		}
	}

	/** 请求网络数据 */
	private void loadData(String url, String text, int msgWhat) {
		// ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, msgWhat);// 1001是msg.what
	}

	/** 关注/取消关注操作 */
	private void operator() {
		if (detailsBean.follow) {
			// guanzhu_text.setText(getResources().getString(R.string.exhibit_attention));
			// guanzhu_text.setSelected(true);
			// xq_guanzhu.setImageResource(R.drawable.zsxq_btn_guanzhu_nor);
			// // 取消关注
			// VolleyUtil volleyUtil = new VolleyUtil(mContext);
			// volleyUtil.onStop();
			// String url =
			// String.format(GloableConfig.CANCLE_FOLLOW_ZHANSHANG_URL,
			// GloableConfig.userid, detailsBean.id);
			// loadData(url, "展商搜索==取消关注 de url:", 1003);
		} else {
			if (!TextUtils.isEmpty(GloableConfig.userid)) {
				// userid不为空证明已经登录
				guanzhu_text.setText(getResources().getString(R.string.have_follow));
				guanzhu_text.setSelected(false);
				xq_guanzhu.setImageResource(R.drawable.zsxq_btn_guanzhu_sel);
				// 关注
				VolleyUtil volleyUtil = new VolleyUtil(mContext);
				volleyUtil.onStop();
				String url = String.format(GloableConfig.FOLLOW_ZHANSHANG_URL, GloableConfig.userid, detailsBean.id);
				loadData(url, "展商搜索==关注 de url:", 1004);
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
