package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MenuGridViewAdapter;
import com.gj.gaojiaohui.adapter.PullListViewAdapter;
import com.gj.gaojiaohui.bean.InfoBean;
import com.gj.gaojiaohui.bean.MenuBean;
import com.gj.gaojiaohui.utils.CustomToast;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 中间显示菜单的activity
 * 
 * @author Administrator
 * 
 */
public class MenuActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView closeImageView;
	private GridView mGridView;
	private MenuGridViewAdapter mAdapter;
	private List<MenuBean> mMenuBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		initView();

	}

	private void initView() {
		mContext = this;
		closeImageView = (ImageView) findViewById(R.id.close_menu_img);
		mGridView = (GridView) findViewById(R.id.menu_gridview);

		mAdapter = new MenuGridViewAdapter(mContext,GloableConfig.menuBeans, R.layout.item_menu_gridview);
		mGridView.setAdapter(mAdapter);

		closeImageView.setOnClickListener(this);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_menu_img:
			finish();
			overridePendingTransition(0, R.anim.push_bottom_out);
			break;

		default:
			break;
		}

	}
//	private void getData() {
//		mMenuBean = new ArrayList<>();
//		mMenuBean.add(new MenuBean("通知中心","1"));
//		mMenuBean.add(new MenuBean("扫描二维码","2"));
//		mMenuBean.add(new MenuBean("导航","3"));
//		mMenuBean.add(new MenuBean("大会资讯","4"));
//		mMenuBean.add(new MenuBean("亮点推荐","5"));
//		mMenuBean.add(new MenuBean("观众互动","6"));
//		mMenuBean.add(new MenuBean("会议服务","7"));
//		mMenuBean.add(new MenuBean("我的关注","8"));
//		mMenuBean.add(new MenuBean("我的报名","9"));
//		mMenuBean.add(new MenuBean("大会点评","10"));
//		mMenuBean.add(new MenuBean("服务调度","11"));
//		mMenuBean.add(new MenuBean("嘉宾列表","12"));
//		mMenuBean.add(new MenuBean("大会统计","13"));
//		mMenuBean.add(new MenuBean("服务签到","14"));
//		mMenuBean.add(new MenuBean("通讯录","15"));
//		mMenuBean.add(new MenuBean("嘉宾服务","16"));
//		mMenuBean.add(new MenuBean("嘉宾交流","17"));
//		mMenuBean.add(new MenuBean("展商资料","18"));
//	}

}
