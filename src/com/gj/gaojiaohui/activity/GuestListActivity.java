package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.GuestListAdapter;
import com.gj.gaojiaohui.bean.GuestBean;
import com.gj.gaojiaohui.bean.GuestInfoBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.PinyinGuestListComparator;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.view.SideBar;
import com.smartdot.wenbo.huiyi.R;

/**
 * h=== 嘉宾列表
 */
public class GuestListActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	GuestListAdapter adapter;
	private ListView listView;
	private ArrayList<GuestInfoBean> listData = new ArrayList<>();
	private Context mContext;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;

	ImageView custom_back_img, serach_img;
	TextView custom_title_tv;
	/** 请求回的数据进行解析 */
	Handler guestlist_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		if (json != null && !json.equals("")) {
			// 通过gson将json映射成实体类
			GuestBean guestBean = CommonUtil.gson.fromJson(json, GuestBean.class);// 这段崩溃有3种原因
																					// 1/字段名缺失,2/格式类型不对,3/字段null了
			switch (guestBean.resultCode) {
			case 200:
				listData.clear();
				listData.addAll(guestBean.list);
				if (type.equals("0")) {// 嘉宾列表
					Collections.sort(listData, new PinyinGuestListComparator());
				}
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}

	String muser_id = "";
	String language = "";
	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guestlist);
		type = getIntent().getStringExtra("type");

		mContext = this;
		SharePreferenceUtils.getAppConfig(this);
		initView();
		process();
		// loadData();//如果加载数据放在后面可能排序不对。
		String murl = String.format(GloableConfig.GUESTS_LIST_URL, muser_id, language, type);
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		Constant.getDataByVolley(mContext, murl, guestlist_handler, 0);
		setAllClick();

	}

	private void process() {
		try {// 获取用户id
//			muser_id = Constant.decode(Constant.key, (String) SharePreferenceUtils.getParam("user_id", ""));
			muser_id = GloableConfig.userid;
			language = (String) SharePreferenceUtils.getParam("language", "");
			if (language.equals("en")) {
				language = "2";
			} else {
				language = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getIntent().getStringExtra("type") != null) {
			switch (getIntent().getStringExtra("type")) {// 获取type类型
			case "liebiao":
				type = "0";
				custom_title_tv.setText(R.string.title_guestlist);
				break;
			case "jiaoliu":
				type = "1";
				custom_title_tv.setText(R.string.title_guestjiaoliu);
				break;
			default:
				break;
			}
		}
	}

	private void initView() {
		serach_img = (ImageView) findViewById(R.id.serach_img);
		custom_back_img = (ImageView) findViewById(R.id.custom_back_img);
		custom_title_tv = (TextView) findViewById(R.id.custom_title_tv);
		serach_img.setVisibility(View.VISIBLE);
		serach_img.setOnClickListener(this);
		custom_back_img.setOnClickListener(this);
		//
		listView = (ListView) findViewById(R.id.guestlist_listview);
		adapter = new GuestListAdapter(mContext, listData, R.layout.guestlist_item);
		listView.setAdapter(adapter);
		initIndexView();
		//
	}

	/** 初始化A-Z索引view */
	@SuppressLint("InflateParams")
	private void initIndexView() {
		indexBar = (SideBar) findViewById(R.id.guestlist_myletterlistview);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		// 根据a-z进行排序
		// FirstLetterUtil.sortList(list);
		indexBar.setListView(listView);
	}

	private void setAllClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, GuestInfoActivity.class);
				if (type.equals("0")) {// 由列表进入-嘉宾详情
					intent.putExtra("type", "liebiao");
				} else if (type.equals("1")) {// 由嘉宾交流-嘉宾详情
					intent.putExtra("type", "jiaoliu");
				}

				intent.putExtra("userid", listData.get(position).id);
				intent.putExtra("tag", listData.get(position).tag);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.serach_img:
			Intent intent = new Intent(mContext, GuestSearchActivity.class);
			if (type.equals("0")) {// 由列表进入-嘉宾列表搜索
				intent.putExtra("type", "liebiao");
			} else if (type.equals("1")) {// 由嘉宾交流-嘉宾详情
				intent.putExtra("type", "jiaoliu");
			}
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
