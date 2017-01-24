package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.AudienceMsgListAdapter;
import com.gj.gaojiaohui.bean.AudienceBean;
import com.gj.gaojiaohui.bean.AudienceMsgBean;
import com.gj.gaojiaohui.bean.VipRegisterBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.smartdot.wenbo.huiyi.R;

/**
 * 观众互动-观众详情 界面 add wb
 */
public class AudienceDetailActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private AudienceMsgListAdapter adapter;
	private ListView msg_lv;
	private List<AudienceMsgBean> list;
	private Context mContext;
	private EditText remark_et;
	private TextView audience_name_tv, follow_state_tv, company_value_tv, tel_tv, look_all_tv;
	private ScrollView mScrollView;
	private ImageView edit_img;
	private CheckBox audience_item_checkbox;
	private RatingBar ratingBar;
	private String userId;
	private AudienceBean bean;
	/** 请求回的数据进行解析 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 2:
				parseResult(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audience_detail);
		mContext = this;

		getData();
		initView();
		addListener();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.audience_detail));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		audience_name_tv = (TextView) findViewById(R.id.audience_name_tv);
		follow_state_tv = (TextView) findViewById(R.id.follow_state_tv);
		company_value_tv = (TextView) findViewById(R.id.company_value_tv);
		tel_tv = (TextView) findViewById(R.id.tel_tv);
		remark_et = (EditText) findViewById(R.id.remark_et);
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		look_all_tv = (TextView) findViewById(R.id.look_all_tv);
		look_all_tv.setOnClickListener(this);
		edit_img = (ImageView) findViewById(R.id.edit_img);
		edit_img.setOnClickListener(this);
		audience_item_checkbox = (CheckBox) findViewById(R.id.audience_item_checkbox);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		list = new ArrayList<AudienceMsgBean>();
		adapter = new AudienceMsgListAdapter(mContext, list, R.layout.item_audience_msg);
		msg_lv = (ListView) findViewById(R.id.msg_lv);
		msg_lv.setAdapter(adapter);
		setListViewHeightBasedOnChildren(msg_lv);// 记得请求完数据重新执行一次该方法
		disableAutoScrollToBottom();
	}

	/** 请求页面数据 */
	private void getData() {
		ProgressUtil.showPregressDialog(mContext);
		Intent intent = getIntent();
		userId = intent.getStringExtra("userId");
		String url = String.format(GloableConfig.AUDIENCE_DETAIL, GloableConfig.userid, userId);
		Constant.getDataByVolley(mContext, url, handler, 1);
	}

	/** 修改备注 */
	private void submitRemark(String value) {
		String url = String.format(GloableConfig.AUDIENCE_SAVE_REMARK, GloableConfig.userid, userId, value);
		Constant.getDataByVolley(mContext, url, handler, 2);
	}

	/** 评星 */
	private void setFollow(String userId, String exhibitorId, int star) {
		String url = String.format(GloableConfig.AUDIENCE_FOLLOW, GloableConfig.userid, exhibitorId, star);
		Constant.getDataByVolley(mContext, url, handler, 2);
	}

	/** 关注/取消关注 */
	private void setFollow(String userId, String exhibitorId, boolean state) {
		String url;
		if (state) {
			url = String.format(GloableConfig.AUDIENCE_FOLLOW, GloableConfig.userid, exhibitorId, 1);
		} else {
			url = String.format(GloableConfig.AUDIENCE_CANCEL_FOLLOW, GloableConfig.userid, exhibitorId);
		}
		Constant.getDataByVolley(mContext, url, handler, 2);
	}

	/** 解析关注/评星/备注 请求结果 */
	private void parseResult(Message msg) {
		String json = msg.obj.toString();
		VipRegisterBean data = CommonUtil.gson.fromJson(json, VipRegisterBean.class);
		if (data.resultCode == 200) {
			getData();
			//CustomToast.showToast(mContext, "成功!");
		} else {
			CustomToast.showToast(mContext, "失败!");
		}
	}

	/** 解析数据 填充数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		bean = CommonUtil.gson.fromJson(json, AudienceBean.class);
		if (bean.resultCode == 200) {

			audience_name_tv.setText(bean.name);
			company_value_tv.setText(bean.company);
			tel_tv.setText(bean.tel);
			remark_et.setText(bean.remark);

			if (bean.follow) {
				follow_state_tv.setText(getString(R.string.follow_now));
				audience_item_checkbox.setChecked(true);
			} else {
				follow_state_tv.setText(getString(R.string.follow));
				audience_item_checkbox.setChecked(false);
			}

			ratingBar.setRating(bean.star);

			list.clear();
			list.addAll(bean.list);
			adapter.notifyDataSetChanged();

		} else {
			CustomToast.showToast(mContext, "请求观众详情失败!");
		}
	}

	private void addListener() {
		/** 我的备注 */
		remark_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEND) {
					// 保存方法 这里请求一次数据
					String value = remark_et.getText().toString();
					if (StringUtils.isUserName(value)) {
						submitRemark(value);
						remark_et.clearFocus();
						CommonUtils.hideKeyboard(AudienceDetailActivity.this);
					} else {
						CustomToast.showToast(AudienceDetailActivity.this, "您的标题中包含特殊字符请重新输入");
					}
					return true;
				}
				return false;
			}
		});
		/** 关注 */
		audience_item_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					/** 防止重复操作 如果已经选中了 不继续执行 */
					if (!bean.follow) {
						// 关注
						setFollow(GloableConfig.userid, userId, true);
						follow_state_tv.setText(getString(R.string.follow_now));
					}
				} else {
					/** 防止重复操作 如果已经取消选中了 不继续执行 */
					if (bean.follow) {
						// 取消关注
						setFollow(GloableConfig.userid, userId, false);
						follow_state_tv.setText(getString(R.string.follow));
					}
				}
			}
		});
		/** 评星 */
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				int star = (int) rating;
				switch (star) {
				case 0:
				case 1:
					// TODO 点击1星
					if (bean.star != star) {
						setFollow(GloableConfig.userid, userId, 1);
					}
					break;
				case 2:
					// TODO 点击2星
					if (bean.star != star) {
						setFollow(GloableConfig.userid, userId, 2);
					}
					break;
				case 3:
					// TODO 点击3星
					if (bean.star != star) {
						setFollow(GloableConfig.userid, userId, 3);
					}
					break;

				}
			}
		});
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

	/**
	 * 禁止ScrollView的childview自动滑动到底部
	 */
	private void disableAutoScrollToBottom() {
		mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		mScrollView.setFocusable(true);
		mScrollView.setFocusableInTouchMode(true);
		mScrollView.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
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
		case R.id.edit_img:
			// TODO 弹出软键盘
			remark_et.setFocusableInTouchMode(true);
			remark_et.requestFocus();
			CommonUtils.switchKeyboard(v, true, remark_et);
			break;
		case R.id.look_all_tv:
			// TODO 在这里执行跳转到留言详情列表界面
			Intent intent = new Intent(mContext, LiuYanParticularsActivity.class);
			intent.putExtra("id", userId);
			intent.putExtra("isExhibitors", 1);
			intent.putExtra("type", "0");
			mContext.startActivity(intent);
			break;
		}

	}

}
