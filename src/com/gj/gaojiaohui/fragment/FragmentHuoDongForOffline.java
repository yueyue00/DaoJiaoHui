package com.gj.gaojiaohui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.activity.HuoDongParticularsActivity;
import com.gj.gaojiaohui.adapter.MyHuoDongForOfflineAdapter;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 活动列表 (离线状态)
 * 
 * @author Administrator
 * 
 */
public class FragmentHuoDongForOffline extends Fragment implements OnItemClickListener {

	private View view;
	private ListView activity_offline_lv;
	private Context mContext;
	private MyHuoDongForOfflineAdapter adapter;
	private List<MeetingChildBeanForOffline> list;
	private DBHelper dbHelper;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_huodong_for_offline, container, false);
		mContext = getActivity();
		getData();
		initView();
		return view;
	}

	private void initView() {
		activity_offline_lv = (ListView) view.findViewById(R.id.activity_offline_lv);
		
		adapter = new MyHuoDongForOfflineAdapter(mContext, list, R.layout.three_huodong_item);
		activity_offline_lv.setAdapter(adapter);
		activity_offline_lv.setOnItemClickListener(this);
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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String ID = list.get(position).id;
		Intent intent = new Intent(mContext, HuoDongParticularsActivity.class);
		intent.putExtra("userId", Integer.parseInt(ID));
		mContext.startActivity(intent);
	}
}
