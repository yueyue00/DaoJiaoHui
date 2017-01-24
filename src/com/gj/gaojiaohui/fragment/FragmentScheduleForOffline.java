package com.gj.gaojiaohui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.activity.HuoDongParticularsActivity;
import com.gj.gaojiaohui.activity.ScheduleSearchForOfflineActivity;
import com.gj.gaojiaohui.adapter.SchedulehListForOfflineAdapter;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.smartdot.wenbo.huiyi.R;

/**
 * 日程列表-离线版
 */
@SuppressLint("InflateParams")
public class FragmentScheduleForOffline extends Fragment implements OnClickListener, OnItemClickListener {
	private View view;
	private SchedulehListForOfflineAdapter adapter;
	private ListView meeting_news_lv;
	private List<MeetingChildBeanForOffline> list;
	private Context mContext;
	private DBHelper dbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_schedule_offline, null);
		mContext = getActivity();

		getData();
		initView();
		return view;
	}

	private void getData() {
		/** 打开写入到本地的数据库文件 */
		SQLdm activityDb = new SQLdm("activityList.db");// 展商列表是exhibitorlist.db
		SQLiteDatabase db = activityDb.openDatabase(mContext);

		dbHelper = new DBHelper();
		list = new ArrayList<MeetingChildBeanForOffline>();
		list.clear();
		list.addAll(dbHelper.selectDataForActivity(db));
	}

	private void initView() {
		TextView title_tv = (TextView) view.findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.all_schedule));
		ImageView back = (ImageView) view.findViewById(R.id.custom_back_img);
		back.setVisibility(View.GONE);
		ImageView serach_img = (ImageView) view.findViewById(R.id.serach_img);
		serach_img.setVisibility(View.VISIBLE);
		serach_img.setOnClickListener(this);
		adapter = new SchedulehListForOfflineAdapter(mContext, list, R.layout.item_schedule_child);
		meeting_news_lv = (ListView) view.findViewById(R.id.search_lv);
		meeting_news_lv.setAdapter(adapter);
		meeting_news_lv.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.serach_img:
			mContext.startActivity(new Intent(mContext, ScheduleSearchForOfflineActivity.class));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String ID = list.get(position).id;
		Intent intent = new Intent(mContext, HuoDongParticularsActivity.class);
		intent.putExtra("userId", Integer.parseInt(ID));
		mContext.startActivity(intent);
	}

}
