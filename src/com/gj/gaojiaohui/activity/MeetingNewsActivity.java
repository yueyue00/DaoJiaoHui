package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MeetingNewsListAdapter;
import com.gj.gaojiaohui.adapter.MeetingNewsRecyclerAdapter;
import com.gj.gaojiaohui.bean.MeetingNewsBean;
import com.gj.gaojiaohui.bean.MeetingNewsTitleBean;
import com.gj.gaojiaohui.bean.MeetingNewsTotalBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.MeetingNewsHeadView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会资讯界面 add wb
 */
public class MeetingNewsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener,
		com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView> {

	private Context mContext;
	private MeetingNewsListAdapter adapter;
	private PullToRefreshListView meeting_news_lv;
	private List<MeetingNewsBean> list;

	private RecyclerView recyclerView;
	private List<MeetingNewsTitleBean> mDatas;
	private MeetingNewsRecyclerAdapter recycleAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private MeetingNewsHeadView headView;
	private ListView listView;
	/** 首页 */
	private int FIRST_PAGE = 1;
	/** 数据请求页码 */
	private int toPage = 1;
	/** 加载更所数据 有数据/true-没数据/false */
	private boolean isMore = true;
	private String id = "";

	/** 请求回的数据进行解析 */
	@SuppressLint("HandlerLeak")
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
		setContentView(R.layout.activity_meeting_news);
		mContext = this;

		initView();
		initRecyclerView();
		initData();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(getResources().getString(R.string.meeting_information_title));
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
		meeting_news_lv = (PullToRefreshListView) findViewById(R.id.meeting_news_lv);
		list = new ArrayList<MeetingNewsBean>();
		adapter = new MeetingNewsListAdapter(mContext, list, R.layout.item_home_list);
		meeting_news_lv.setAdapter(adapter);
		meeting_news_lv.setOnItemClickListener(this);
		meeting_news_lv.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		meeting_news_lv.setOnRefreshListener(this);

		headView = new MeetingNewsHeadView(mContext);
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);    		View view = headView.getView();
		view.setLayoutParams(layoutParams);
		listView = meeting_news_lv.getRefreshableView();
		listView.addHeaderView(view);

	}

	private void initData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.MEETING_NEWS_URL, id, FIRST_PAGE, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);
	}

	/** 初始化全部栏目滑块列表 */
	private void initRecyclerView() {
		recyclerView = (RecyclerView) findViewById(R.id.meeting_news_recyclerView);
		mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

		mDatas = new ArrayList<MeetingNewsTitleBean>();
		recycleAdapter = new MeetingNewsRecyclerAdapter(mContext, mDatas);
		recyclerView.setAdapter(recycleAdapter);
		recyclerView.setLayoutManager(mLayoutManager);
		recycleAdapter.setOnItemClickListener(new MeetingNewsRecyclerAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, String data, int position) {
				id = mDatas.get(position).id;
				toPage = 1;
				initData();
			}
		});
		// 指定默认选中的item并刷新
		recycleAdapter.setDefaultSelected(0);
		recycleAdapter.notifyDataSetChanged();
		// 滚动到指定位置
		recyclerView.smoothScrollToPosition(0);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		MeetingNewsTotalBean data = CommonUtil.gson.fromJson(json, MeetingNewsTotalBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			mDatas.clear();
			mDatas.addAll(data.type_list);
			recycleAdapter.notifyDataSetChanged();
			list.clear();
			list.addAll(data.news_list);
			adapter.notifyDataSetChanged();
			headView.refreshData(data.news_top_list);
			toPage++;
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	/** 分页加载json数据 */
	private void pageLoad(Message msg) {
		String json = msg.obj.toString();
		MeetingNewsTotalBean data = CommonUtil.gson.fromJson(json, MeetingNewsTotalBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			if (data.news_list.size() != 0) {// 有数据
				isMore = true;
				list.addAll(data.news_list);
				toPage++;
				adapter.notifyDataSetChanged();
			} else {// 没有数据                                                             
				isMore = false;
			}
			if (meeting_news_lv.isRefreshing()) {
				meeting_news_lv.onRefreshComplete();
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 进入到webView详情界面
		Intent intent = new Intent(mContext, ZiXunDetailActivity.class);
		intent.putExtra("title", list.get(position - 2).title);
		intent.putExtra("content", "");
		intent.putExtra("imgurl", list.get(position - 2).pic);
		intent.putExtra("webviewUrl", list.get(position - 2).url);
		startActivity(intent);
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
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 下拉刷新

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载
		if (isMore) {
			meeting_news_lv.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
			meeting_news_lv.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
			meeting_news_lv.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		} else {
			meeting_news_lv.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.nothing));
			meeting_news_lv.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.nothing));
			meeting_news_lv.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.nothing));
		}

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.MEETING_NEWS_URL, id, toPage, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1002);
	}

}
