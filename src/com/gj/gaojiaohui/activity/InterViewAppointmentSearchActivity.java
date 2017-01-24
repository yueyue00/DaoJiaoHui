package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.InterViewAppointmentAdapter;
import com.gj.gaojiaohui.adapter.ZhanShangSearchAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsSearchBean;
import com.gj.gaojiaohui.bean.ExhibitorsSearchChildBean;
import com.gj.gaojiaohui.bean.InterviewAppointmentBean;
import com.gj.gaojiaohui.bean.InterviewAppointmentChildBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 采访预约搜索界面
 * 
 * @author lixiaoming
 * 
 */
public class InterViewAppointmentSearchActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener,
		com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>, TextWatcher {

	private InterViewAppointmentAdapter adapter;
	private PullToRefreshListView listview;
	private List<InterviewAppointmentChildBean> list;
	private Context mContext;
	private EditText search_et;
	/** 首页 */
	private int FIRST_PAGE = 1;
	/** 数据请求页码 */
	private int toPage = 1;
	/** 加载更所数据 有数据/true-没数据/false */
	private boolean isMore = true;
	/** 搜索的内容 */
	private String searchContent = "";
	/** 判断数据请求是否成功了 成功了不可以再次请求 */
	private boolean isNetComplete = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inter_view_appointment_search);
		mContext = this;
		initView();
		addListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isNetComplete) {// 正在请求中

		} else {// 已经请求完
			getData(searchContent);
		}
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.cancel_search_tv);
		title.setOnClickListener(this);
		search_et = (EditText) findViewById(R.id.search_et);
		listview = (PullToRefreshListView) findViewById(R.id.interviewAppointment_search_listview);

		list = new ArrayList<InterviewAppointmentChildBean>();
		adapter = new InterViewAppointmentAdapter(mContext, list, R.layout.interview_appointment_item);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);

		listview.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		listview.setOnRefreshListener(this);
		search_et.addTextChangedListener(this);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		InterviewAppointmentBean data = CommonUtil.gson.fromJson(json, InterviewAppointmentBean.class);// 这段崩溃有3种原因
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
		InterviewAppointmentBean data = CommonUtil.gson.fromJson(json, InterviewAppointmentBean.class);// 这段崩溃有3种原因
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
			if (listview.isRefreshing()) {
				listview.onRefreshComplete();
			}
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData(String search) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.INTERVIEW_APPOINTMENT_SEARCH_URL, FIRST_PAGE, GloableConfig.LANGUAGE_TYPE, search);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what

	}

	private void addListener() {
		search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 搜索方法
					getSearchResult();

					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 搜索方法
	 */
	public void getSearchResult() {
		try {
			searchContent = URLEncoder.encode(search_et.getText().toString(), "UTF-8");
			getData(searchContent);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 在这里执行跳转到展商详情的流程
		String exhibitorId = list.get(position - 1).id;
		Intent intent = new Intent(mContext, AppointmentDetailActivity.class);
		intent.putExtra("id", exhibitorId);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_search_tv:
			getSearchResult();
			break;

		default:
			break;
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

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 上拉加载
		if (isMore) {
			listview.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
			listview.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
			listview.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		} else {
			listview.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.nothing));
			listview.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.nothing));
			listview.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.nothing));
		}
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.INTERVIEW_APPOINTMENT_SEARCH_URL, toPage, GloableConfig.LANGUAGE_TYPE, searchContent);
		volleyUtil1.stringRequest(handler, url, map, 1002);// 1002是msg.what

	}

	/** 分页加载数据 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);

				break;
			case 1002:
				pageLoad(msg);
				break;
			default:
				break;
			}
		};
	};
	private InterviewAppointmentChildBean business;

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO 在editText的内容改变了之后进行操作
		// if (s.length() == 0) {
		// getData("");
		// toPage = 1;
		// }

	}
}
