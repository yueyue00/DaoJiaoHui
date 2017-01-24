package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.ServiceFeedbackDetailAdapter;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackDetailBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackDetailChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 服务反馈-嘉宾留言
 * 
 * @author lixiaoming
 * 
 */
public class ServiceFeedbackDetailActivity extends Activity implements OnClickListener, OnRefreshListener {

	private ImageView back_img;
	private TextView titile_tv;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	private EditText liuyan_et;
	private Button send_btn;

	private Context mContext;
	private String vipId = "";
	private List<ServiceFeedbackDetailChildBean> list;
	private ServiceFeedbackDetailAdapter adapter;

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
				break;
			case 1002:
				ProgressUtil.dismissProgressDialog();
				parseLiuyanData(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_feedback_detail);
		mContext = this;
		Intent intent = getIntent();
		vipId = intent.getStringExtra("vipId");
		if (TextUtils.isEmpty(vipId)) {
			// 嘉宾直接进入该界面，那么请求数据的时候userid传用户id，vipid传空字符串
			vipId = "";
		}

		initView();
		String url = String.format(GloableConfig.SERVICE_FEEDBACK_GUEST_LEAVE_WORD_URL, GloableConfig.userid, vipId);
		loadData(url, "服务反馈-嘉宾留言url：", 1001);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		titile_tv = (TextView) findViewById(R.id.custom_title_tv);
		titile_tv.setText(R.string.service_feedback);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.service_feedback_detail_swiperefresh_layout);
		listView = (ListView) findViewById(R.id.service_feedback_detail_listview);
		liuyan_et = (EditText) findViewById(R.id.service_feedback_detail_liuyan_et);
		CommonUtils.EmojiFilter(liuyan_et);
		CommonUtils.stringFilter(liuyan_et);
		send_btn = (Button) findViewById(R.id.service_feedback_detail_send);
		send_btn.setOnClickListener(this);

		list = new ArrayList<ServiceFeedbackDetailChildBean>();
		adapter = new ServiceFeedbackDetailAdapter(mContext, list, R.layout.service_feedback_detail_item);
		listView.setAdapter(adapter);

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

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();

		// 通过gson将json映射成实体类
		ServiceFeedbackDetailBean data = CommonUtil.gson.fromJson(json, ServiceFeedbackDetailBean.class);// 这段崩溃有3种原因
		// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 留言反馈 */
	private void parseLiuyanData(Message msg) {
		String json = msg.obj.toString();
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {
			CustomToast.showToast(mContext, getResources().getString(R.string.send_leaveword_success));
			String url = String.format(GloableConfig.SERVICE_FEEDBACK_GUEST_LEAVE_WORD_URL, GloableConfig.userid, vipId);
			loadData(url, "服务反馈-嘉宾留言url：", 1001);
			liuyan_et.setText("");
			CommonUtils.hideKeyboard(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.service_feedback_detail_send:
			// TODO 点击发送后要刷新列表数据
			try {
				String editContent = liuyan_et.getText().toString();
				if (!TextUtils.isEmpty(editContent)) {
					String value = URLEncoder.encode(liuyan_et.getText().toString().trim().replace("\"", "\\\\"), "UTF-8");
					String url = String.format(GloableConfig.LEAVE_WORD_COMMINT_URL, GloableConfig.userid, vipId, value, "1");// type值为1
					loadData(url, "服务反馈-嘉宾留言-发送留言反馈：", 1002);
				} else {
					CustomToast.showToast(mContext, getResources().getString(R.string.liuyan_empty));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO 请求数据 刷新数据后要更新适配器
		String url = String.format(GloableConfig.SERVICE_FEEDBACK_GUEST_LEAVE_WORD_URL, GloableConfig.userid, vipId);
		loadData(url, "服务反馈-嘉宾留言url：", 1001);
		adapter.notifyDataSetChanged();
		if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	/** 请求数据 */
	private void loadData(String url, String text, int msgWhat) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, msgWhat);// 1001是msg.what
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
