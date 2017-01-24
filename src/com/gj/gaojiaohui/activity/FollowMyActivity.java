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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.FollowListAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsSearchChildBean;
import com.gj.gaojiaohui.bean.FollowBean;
import com.gj.gaojiaohui.bean.FollowListBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.port.OnChangeFollowListener;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的关注 界面 add wb
 */
public class FollowMyActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener, OnChangeFollowListener {

	private FollowListAdapter adapter;
	private ListView meeting_news_lv;
	private List<FollowBean> list;
	private Context mContext;
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
				ProgressUtil.dismissProgressDialog();
				parseRefreshData(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follow);
		mContext = this;

		initView();
		getData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isNetComplete) {

		} else {
			getData();
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.MY_FOLLOW_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 最后一个1001是和handler的case值对上的,怎么起都行
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.my_follows));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		ImageView serach = (ImageView) findViewById(R.id.serach_img);
		serach.setVisibility(View.VISIBLE);
		serach.setOnClickListener(this);

		list = new ArrayList<FollowBean>();
		adapter = new FollowListAdapter(mContext, list, R.layout.item_follow);
		adapter.setOnShopAppRefreshListener(this);
		meeting_news_lv = (ListView) findViewById(R.id.follow_lv);
		meeting_news_lv.setAdapter(adapter);
		meeting_news_lv.setOnItemClickListener(this);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		FollowListBean data = CommonUtil.gson.fromJson(json, FollowListBean.class);// 这段崩溃有3种原因
																					// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
			isNetComplete = false;
		} else {
			/** 请求失败的处理 */
		}
	}

	/** 解析json数据 */
	private void parseRefreshData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);// 这段崩溃有3种原因
																								// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此刷新数据 */
			getData();
		} else {
			/** 请求失败的处理 */
		}
	}

	@Override
	public void OnRefresh(String id) {
		// 在这里执行取消关注该厂商的方法 事后记得要重新请求数据
		// refreshData(id);
	}

	/** 取消关注并在成功后刷新数据 */
	private void refreshData(String id) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.CANCLE_FOLLOW_ZHANSHANG_URL, GloableConfig.userid, id);
		volleyUtil1.stringRequest(handler, url, map, 1002);// 最后一个1001是和handler的case值对上的,怎么起都行
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.serach_img:
			// TODO 在这里执行跳转到搜索界面的流程
			mContext.startActivity(new Intent(mContext, FollowSearchActivity.class));
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 在这里执行跳转到展商详情的流程
		Intent intent = new Intent(mContext, ZhanShangParticularsActivity.class);
		intent.putExtra("exhibitorId", list.get(position).id);
		mContext.startActivity(intent);
	}
}
