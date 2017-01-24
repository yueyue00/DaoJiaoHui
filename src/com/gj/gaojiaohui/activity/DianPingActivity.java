package com.gj.gaojiaohui.activity;

import io.rong.imkit.R.bool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.DianPingAdapter;
import com.gj.gaojiaohui.adapter.DianPingAdapter.EditReplyCallBack;
import com.gj.gaojiaohui.bean.DianPingBean;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.gj.gaojiaohui.bean.ReplyBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

/**
 * 大会点评
 * 
 * @author zhangt
 * 
 */
public class DianPingActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

	private int GETDATA = 0;
	private int LOADMORE = 1;
	private Context mContext;
	private ImageView dianping_back_img;
	private TextView title_tv;
	private ImageView dianping_feedback_img;
	private SwipeRefreshLayout dianping_swiperefreshlayout;
	private ListView dianping_lv;
	private ImageView dianping_leavemsg_img;
	private List<DianPingBean> dianPingBeans;
	private DianPingAdapter mAdapter;
	private int page = 1;
	/** 该权限用于判断服务人员和嘉宾进入服务反馈的哪个界面 */
	private boolean permission;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				dianping_swiperefreshlayout.setRefreshing(false);
				parseData(msg);
			} else if (msg.what == 1002) {
				parseLoadMoreData(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dian_ping);
		mContext = this;
		initView();
		getData(GETDATA);
	}

	private void initView() {
		dianping_back_img = (ImageView) findViewById(R.id.custom_back_img);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		dianping_feedback_img = (ImageView) findViewById(R.id.feedback_img);
		dianping_feedback_img.setVisibility(View.VISIBLE);
		dianping_swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.dianping_swiperefreshlayout);
		dianping_lv = (ListView) findViewById(R.id.dianping_lv);
		dianping_leavemsg_img = (ImageView) findViewById(R.id.dianping_leavemsg_img);

		title_tv.setText(mContext.getResources().getString(R.string.dianping));
		dianping_feedback_img.setVisibility(View.GONE);

		dianPingBeans = new ArrayList<>();
		mAdapter = new DianPingAdapter(mContext, dianPingBeans,
				R.layout.item_dianping);
		dianping_lv.setAdapter(mAdapter);

		dianping_back_img.setOnClickListener(this);
		dianping_feedback_img.setOnClickListener(this);
		dianping_leavemsg_img.setOnClickListener(this);

		// 刷新时，指示器旋转后变化的颜色
		dianping_swiperefreshlayout.setColorSchemeResources(
				R.color.main_blue_light, R.color.main_blue_dark);
		dianping_swiperefreshlayout.setOnRefreshListener(DianPingActivity.this);

		dianping_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 判断滚动到底部
					if (dianping_lv.getLastVisiblePosition() == (dianping_lv
							.getCount() - 1)) {
						getData(LOADMORE);
						mAdapter.Changed();
					}
					// 判断滚动到顶部
					if (dianping_lv.getFirstVisiblePosition() == 0) {
					}
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

		});

		mAdapter.setCallBack(new EditReplyCallBack() {

			@Override
			public void clickReply(int position, ReplyBean bean) {
				dianPingBeans.get(position).comment_list.add(bean);
				mAdapter.notifyDataSetChanged();
			}

		});
	}

	/**
	 * 获取数据
	 */
	private void getData(int type) {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		volleyUtil.onStop();
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.DIANPING_LIST_URL, GloableConfig.userid, page);
		if (type == GETDATA) {
			volleyUtil.stringRequest(handler, url, map, 1001);
		} else {
			volleyUtil.stringRequest(handler, url, map, 1002);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.feedback_img:
			Intent intent1 = new Intent();
			if (permission) {// true 服务人员
				intent1.setClass(mContext, ServiceFeedbackV1Activity.class);
			} else {// 嘉宾
				intent1.setClass(mContext, ServiceFeedbackDetailActivity.class);
			}
			startActivity(intent1);
			break;
		case R.id.dianping_leavemsg_img:
			Intent intent = new Intent(mContext, LeaveMessageActivity.class);
			startActivityForResult(intent, 2001);
			break;

		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		page = 1;
		getData(GETDATA);
	}

	private void parseLoadMoreData(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				List<DianPingBean> tmpBeans = CommonUtils.gson.fromJson(jsonObject.getString("list"), new TypeToken<List<DianPingBean>>() {
				}.getType());
				if (tmpBeans.size() != 0) {
					dianPingBeans.clear();
					dianPingBeans.addAll(tmpBeans);
					mAdapter.Changed();
					ProgressUtil.dismissProgressDialog();
				} else {
					CustomToast.showToast(mContext, getResources().getString(R.string.nothing));
					ProgressUtil.dismissProgressDialog();
				}

			} else {
				ProgressUtil.dismissProgressDialog();
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			}

		} catch (JSONException e) {
			ProgressUtil.dismissProgressDialog();
			CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			e.printStackTrace();
		}
	}

	private void parseData(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			permission = jsonObject.getBoolean("permission");
			if (jsonObject.getInt("resultCode") == 200) {
				List<DianPingBean> tmpBeans = CommonUtils.gson.fromJson(jsonObject.getString("list"), new TypeToken<List<DianPingBean>>() {
				}.getType());
				dianPingBeans.clear();
				dianPingBeans.addAll(tmpBeans);
				mAdapter.Changed();
				page++;
				ProgressUtil.dismissProgressDialog();
			} else {
				ProgressUtil.dismissProgressDialog();
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			}

		} catch (JSONException e) {
			ProgressUtil.dismissProgressDialog();
			CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			e.printStackTrace();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 2001) {
				page = 1;
				getData(GETDATA);
			}
		}
	}
}
