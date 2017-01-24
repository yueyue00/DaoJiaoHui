package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import com.gheng.exhibit.http.body.response.InitData;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.NoticeCenterAdapter;
import com.gj.gaojiaohui.bean.NoticeCenterBean;
import com.gj.gaojiaohui.bean.NoticeCenterChildBean;
import com.gj.gaojiaohui.bean.ServiceFeedbackBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
 * 通知中心
 * 
 * @author lixiaoming
 * 
 */
public class NoticeCenterActivity extends Activity implements OnClickListener, OnItemClickListener,
		com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView> {
	private ImageView back_img;
	private TextView title_tv;
	private PullToRefreshListView listView;

	private Context mContext;
	private List<NoticeCenterChildBean> list;
	private NoticeCenterAdapter adapter;
	/** 首页 */
	private int FIRST_PAGE = 1;
	/** 数据请求页码 */
	private int toPage = 1;
	/** 加载更所数据 有数据/true-没数据/false */
	private boolean isMore = true;
	private String userId = "lisan";

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
			case 1002:// 分页加载数据
				pageLoad(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_center);
		mContext = this;
		initView();
		InitData();
	}

	/**
	 * 初始化数据
	 */
	private void InitData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.NOTIENCE_CENTER_URL, GloableConfig.userid, FIRST_PAGE, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(R.string.notice_center_text);
		listView = (PullToRefreshListView) findViewById(R.id.notice_center_listview);

		list = new ArrayList<NoticeCenterChildBean>();
		adapter = new NoticeCenterAdapter(mContext, list, R.layout.notice_center_item);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		listView.setOnRefreshListener(this);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		NoticeCenterBean data = CommonUtil.gson.fromJson(json, NoticeCenterBean.class);// 这段崩溃有3种原因
																						// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
			toPage++;
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 分页加载json数据 */
	private void pageLoad(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		NoticeCenterBean data = CommonUtil.gson.fromJson(json, NoticeCenterBean.class);// 这段崩溃有3种原因
																						// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			if (data.list.size() != 0) {// 有数据
				isMore = true;
				list.addAll(data.list);
				adapter.notifyDataSetChanged();
				toPage++;
			} else {// 没有数据
				isMore = false;
			}
			if (listView.isRefreshing()) {
				listView.onRefreshComplete();
			}
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
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO listview的item点击事件
		// 0/紧急通知,1/会议通知,2/会展通知,3/报名活动通知,4/嘉宾日程通知,5/展商[和]观众/嘉宾互相留言通知
		NoticeCenterChildBean bean = list.get(position - 1);
		Intent intent = new Intent();
		intent.setClass(mContext, DaHuiEmergencyNoticeActivity.class);
		intent.putExtra("notifiTitle", bean.title);
		intent.putExtra("notifiDate", bean.date);
		intent.putExtra("notifiContent", bean.content);
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 下拉刷新

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载
		if (isMore) {
			listView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
			listView.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
			listView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		} else {
			listView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.nothing));
			listView.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.nothing));
			listView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.nothing));
		}

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.NOTIENCE_CENTER_URL, GloableConfig.userid, toPage, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1002);// 1001是msg.what
	}

}
