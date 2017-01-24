package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.adapter.SchedulehListForOfflineAdapter;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.gj.gaojiaohui.utils.StringUtils;
import com.smartdot.wenbo.huiyi.R;

/**
 * 离线版-日程列表-搜索 界面 add wb
 */
public class ScheduleSearchForOfflineActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, OnItemClickListener {

	private SchedulehListForOfflineAdapter adapter;
	private ListView meeting_news_lv;
	private List<MeetingChildBeanForOffline> list;
	private Context mContext;
	private EditText search_et;
	private DBHelper dbHelper;
	private SQLdm activityDb;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_searchs);
		mContext = this;

		initView();
		addListener();
	}

	private void search() {
		/** 打开写入到本地的数据库文件 */
		String value = search_et.getText().toString();
		if (!StringUtils.isAsNull(value)) {
			dbHelper = new DBHelper();
			list.clear();
			list.addAll(dbHelper.fuzzySearchForActivity(db, "title", value));
			adapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.cancel_search_tv);
		title.setOnClickListener(this);
		search_et = (EditText) findViewById(R.id.search_et);
		list = new ArrayList<MeetingChildBeanForOffline>();
		adapter = new SchedulehListForOfflineAdapter(mContext, list, R.layout.item_schedule_child);
		meeting_news_lv = (ListView) findViewById(R.id.search_lv);
		meeting_news_lv.setAdapter(adapter);
		meeting_news_lv.setOnItemClickListener(this);

		activityDb = new SQLdm("activityList.db");// 展商列表是exhibitorlist.db
		db = activityDb.openDatabase(mContext);
	}

	private void addListener() {
		search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 搜索方法
					search();
					return true;
				}
				return false;
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long Id) {
		// TODO 在这里执行跳转到日程详情的流程
		String ID = list.get(position).id;
		Intent intent = new Intent(mContext, HuoDongParticularsActivity.class);
		intent.putExtra("userId", Integer.parseInt(ID));
		mContext.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.cancel_search_tv:
			search();
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
}
