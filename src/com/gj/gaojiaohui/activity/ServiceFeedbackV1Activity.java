package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.ServiceFeedbackV1Adapter;
import com.gj.gaojiaohui.bean.FollowListBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 服务反馈-嘉宾列表
 * 
 * @author lixiaoming
 * 
 */
public class ServiceFeedbackV1Activity extends Activity implements OnClickListener, OnRefreshListener, OnItemClickListener {
	private ImageView back_img;
	private TextView title_tv;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	private Context mContext;
	private List<ServiceFeedbackChildBean> list;
	private ServiceFeedbackV1Adapter adapter;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				// 不管数据请求成功与否都要把进度加载条关闭
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_feedback_v1);
		mContext = this;
		initView();
		initData();

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.service_feedback));
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.service_feedback_v1_swiperefresh_layout);
		listView = (ListView) findViewById(R.id.service_feedback_v1_listview);

		list = new ArrayList<ServiceFeedbackChildBean>();
		adapter = new ServiceFeedbackV1Adapter(mContext, list, R.layout.service_feedback_item);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		// 设置背景颜色
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
		// 设置转动的颜色
		swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
		// 设置向下拉动多少触发下啦刷新
		swipeRefreshLayout.setDistanceToTriggerSync(100);
		// 设置下啦刷新显示的位置
		swipeRefreshLayout.setProgressViewEndTarget(false, 300);
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	public void initData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.SERVICE_FEEDBACK_GUEST_LIST_URL, GloableConfig.userid);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		ServiceFeedbackBean data = CommonUtil.gson.fromJson(json, ServiceFeedbackBean.class);// 这段崩溃有3种原因
																								// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
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

	@Override
	public void onRefresh() {
		// TODO 进行数据请求 在刷新数据后要记得更新适配器
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.SERVICE_FEEDBACK_GUEST_LIST_URL, GloableConfig.userid);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
		adapter.notifyDataSetChanged();
		if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO listView 的 item 点击事件
		Intent intent = new Intent(mContext, ServiceFeedbackDetailActivity.class);
		intent.putExtra("vipId", list.get(position).id);
		startActivityForResult(intent, 11);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 再次请求数据
			ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
			VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
			Map<String, String> map = new HashMap<>();// Map为空，必须要有
			String url = String.format(GloableConfig.SERVICE_FEEDBACK_GUEST_LIST_URL, GloableConfig.userid);
			volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
		}
	}
}
