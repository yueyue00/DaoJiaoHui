package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.adapter.ExbihitorListForOfflineAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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

/**
 * 展商搜索-离线版
 * 
 * @author lixiaoming
 * 
 */
public class ExhibitorSearchForOfflineActivity extends Activity implements OnItemClickListener, OnClickListener {

	private ListView exhibit_search_lv;
	private EditText search_et;
	private TextView search_tv;

	private Context mContext;
	private ExbihitorListForOfflineAdapter adapter;
	private List<ExhibitorsBeanForOffline> list;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibitor_search_for_offline);
		mContext = this;
		initView();
		addListener();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		search_et = (EditText) findViewById(R.id.search_et);
		search_tv = (TextView) findViewById(R.id.cancel_search_tv);
		search_tv.setOnClickListener(this);
		exhibit_search_lv = (ListView) findViewById(R.id.exhibit_search_lv);

		list = new ArrayList<ExhibitorsBeanForOffline>();
		adapter = new ExbihitorListForOfflineAdapter(mContext, list, R.layout.exhibitor_offline_item);
		exhibit_search_lv.setAdapter(adapter);
		exhibit_search_lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ExhibitorsBeanForOffline bean = list.get(position);
		Intent intent = new Intent(mContext, ExhibitorDetailForOfflineActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", bean);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}

	/** 给EditText添加点击事件 */
	private void addListener() {
		search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 搜索方法
					getSearchData();

					return true;
				}
				return false;
			}

		});
	}

	/**
	 * 从数据库搜索数据 刷新适配器显示
	 */
	private void getSearchData() {
		String searchValue = search_et.getText().toString();
		/** 打开写入到本地的数据库文件 */
		SQLdm exhibitorlistDb = new SQLdm("exhibitorlist.db");// 展商列表是exhibitorlist.db
		SQLiteDatabase db = exhibitorlistDb.openDatabase(mContext);
         
		dbHelper = new DBHelper();
		list.clear();
		list.addAll(dbHelper.fuzzySearchForExhibitors(db, "name", searchValue));
		adapter.notifyDataSetChanged();
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
	public void onClick(View v) {
     switch (v.getId()) {
	case R.id.cancel_search_tv:
		getSearchData();
		break;

	default:
		break;
	}		
	}
}
