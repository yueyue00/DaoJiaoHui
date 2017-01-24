package com.gj.gaojiaohui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import cn.jpush.a.a.l;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.CarPlaceAdapter;
import com.gj.gaojiaohui.bean.ActivityListBean;
import com.gj.gaojiaohui.bean.AssemblyVehicleBean;
import com.gj.gaojiaohui.bean.AssemblyVehicleChildBean;
import com.gj.gaojiaohui.bean.SignUpListBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会车辆
 * 
 * @author Administrator
 * 
 */
public class ConferenceCarActivity extends GaoJiaoHuiBaseActivity implements OnClickListener,
		com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView> {
	private List<AssemblyVehicleChildBean> list;
	private TextView title_tv;
	private ImageView assembly_vehicle_fanhui;
	private PullToRefreshListView assembly_vehicle_listview;
	private AssemblyVehicleChildBean carBean;
	private Context mContext;
	private int page = 1;
	private CarPlaceAdapter adapter;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1001) {
				if (assembly_vehicle_listview.isRefreshing()) {
					assembly_vehicle_listview.onRefreshComplete();
				}
				ProgressUtil.dismissProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						List<AssemblyVehicleChildBean> tmplist = CommonUtils.gson.fromJson(jsonObject.getString("list"),
								new TypeToken<List<AssemblyVehicleChildBean>>() {
								}.getType());
						if (tmplist.size() > 0) {
							list.addAll(tmplist);
							adapter.notifyDataSetChanged();
							page++;
						} else {
							CustomToast.showToast(mContext, getResources().getString(R.string.lastestPage));
						}

					} else {
						CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
					e.printStackTrace();
				}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assembly);
		mContext = this;
		initView();
		getData();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.assembly_vehicle));
		assembly_vehicle_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		assembly_vehicle_listview = (PullToRefreshListView) findViewById(R.id.assembly_vehicle_listview);
		assembly_vehicle_listview.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		assembly_vehicle_listview.setOnRefreshListener(this);

		list = new ArrayList<AssemblyVehicleChildBean>();
		adapter = new CarPlaceAdapter(mContext, list, R.layout.firstname);
		assembly_vehicle_listview.setAdapter(adapter);
		assembly_vehicle_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ConferenceCarActivity.this, SpecialLineActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", (Serializable) list.get(position - 1).approach);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});

		assembly_vehicle_fanhui.setOnClickListener(this);
	}

	/**
	 * 获取车辆信息
	 */
	private void getData() {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.CAR_URL, page, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		assembly_vehicle_listview.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.getmore));
		assembly_vehicle_listview.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.getmore));
		assembly_vehicle_listview.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.getmore));
		getData();
	}
}
