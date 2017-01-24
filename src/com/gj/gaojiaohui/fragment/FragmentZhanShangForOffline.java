package com.gj.gaojiaohui.fragment;

import java.util.ArrayList;
import java.util.List;

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

import com.gj.gaojiaohui.activity.ExhibitorDetailForOfflineActivity;
import com.gj.gaojiaohui.adapter.ExbihitorListForOfflineAdapter;
import com.gj.gaojiaohui.bean.ExhibitorsBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展商列表-离线模式
 * 
 * @author lixiaoming
 * 
 */
public class FragmentZhanShangForOffline extends Fragment implements OnItemClickListener {

	private View view;
	private ListView exhibitor_offline_lv;

	private Context mContext;
	private ExbihitorListForOfflineAdapter adapter;
	private List<ExhibitorsBeanForOffline> list;
	private DBHelper dbHelper;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_zhanshang_for_offline, container, false);
		mContext = getActivity();
		initView(view);
		getData();
		return view;
	}

	/**
	 * 初始化数据
	 */
	private void getData() {
		/** 打开写入到本地的数据库文件 */
		SQLdm exhibitorlistDb = new SQLdm("exhibitorlist.db");// 展商列表是exhibitorlist.db
		SQLiteDatabase db = exhibitorlistDb.openDatabase(mContext);

		dbHelper = new DBHelper();
		list.clear();
		list.addAll(dbHelper.selectDataForExhibitors(db));
		adapter.notifyDataSetChanged();
	}

	/**
	 * 初始化view
	 */
	private void initView(View view) {
		exhibitor_offline_lv = (ListView) view.findViewById(R.id.exhibitor_offline_lv);
		list = new ArrayList<ExhibitorsBeanForOffline>();
		adapter = new ExbihitorListForOfflineAdapter(mContext, list, R.layout.exhibitor_offline_item);
		exhibitor_offline_lv.setAdapter(adapter);
		exhibitor_offline_lv.setOnItemClickListener(this);
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

}
