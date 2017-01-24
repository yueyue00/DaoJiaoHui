package com.gj.gaojiaohui.activity;

import io.rong.imlib.location.RealTimeLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dxmap.indoornavig.NaviSearchActivity;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.LiangdianExhibitAdapter;
import com.gj.gaojiaohui.bean.LiangdianDetailBean;
import com.gj.gaojiaohui.bean.LiangdianExhibitBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * 亮点推荐的详情界面
 * 
 * @author zhangt
 * 
 */
public class LiangDianDetailsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView liangdian_detail_title;
	private ImageView liangdian_detail_img;
	private TextView liangdian_detail_time;
	private TextView liangdian_detail_contain;
	private ImageView navigation_path_img;
	private ListView exhibit_lv;
	private LiangdianExhibitAdapter mAdapter;
	private List<LiangdianExhibitBean> exhibitBeans;
	private LiangdianDetailBean liangdianDetailBean;
	private DisplayImageOptions options;
	private RelativeLayout navigation_layout;
	private ScrollView scrollView1;
	private String Id;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				parseData(msg);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liang_dian_details);
		Intent intent = getIntent();
		Id = intent.getStringExtra("Id");
		initView();
		getData();
	}

	private void initView() {
		mContext = this;
		options = ImageLoaderUtils.initOptions();
		exhibitBeans = new ArrayList<>();
		backImageView = (ImageView) findViewById(R.id.custom_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_tv);
		navigation_path_img = (ImageView) findViewById(R.id.navigation_path_img);
		liangdian_detail_title = (TextView) findViewById(R.id.liangdian_detail_title);
		liangdian_detail_img = (ImageView) findViewById(R.id.liangdian_detail_img);
		liangdian_detail_time = (TextView) findViewById(R.id.liangdian_detail_time);
		liangdian_detail_contain = (TextView) findViewById(R.id.liangdian_detail_contain);
		exhibit_lv = (ListView) findViewById(R.id.exhibit_lv);
		navigation_layout = (RelativeLayout) findViewById(R.id.navigation_layout);
		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);

		setListViewHeightBasedOnChildren(exhibit_lv);
		disableAutoScrollToBottom(scrollView1);

		titleTextView.setText("");

		mAdapter = new LiangdianExhibitAdapter(mContext, exhibitBeans, R.layout.item_exhibit_layout);
		exhibit_lv.setAdapter(mAdapter);

		backImageView.setOnClickListener(this);
		navigation_path_img.setOnClickListener(this);

		exhibit_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 跳转到展商详情界面
				Intent intent = new Intent(mContext, ZhanShangParticularsActivity.class);
				intent.putExtra("exhibitorId", exhibitBeans.get(position).id);
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取 亮点推荐 详情的数据
	 */
	private void getData() {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.LIANGDIAN_DETAIL_URL, Id, GloableConfig.LANGUAGE_TYPE);
		volleyUtil1.stringRequest(handler, url, map, 1001);

	}

	/**
	 * 解析数据
	 * 
	 * @param msg
	 */
	private void parseData(android.os.Message msg) {
		liangdianDetailBean = CommonUtils.gson.fromJson(msg.obj.toString(), LiangdianDetailBean.class);
		liangdian_detail_title.setText(liangdianDetailBean.title);
		liangdian_detail_time.setText(liangdianDetailBean.date);
		liangdian_detail_contain.setText(liangdianDetailBean.value);
		if (liangdianDetailBean.list.size() == 0) {
			navigation_layout.setVisibility(View.GONE);
		}
		// 如果图片url为空隐藏imgview
		if (!TextUtils.isEmpty(liangdianDetailBean.pic)) {
			ImageLoader.getInstance().displayImage(liangdianDetailBean.pic, liangdian_detail_img, options);
		} else {
			liangdian_detail_img.setVisibility(View.GONE);
		}

		try {
			exhibitBeans.addAll(liangdianDetailBean.list);
		} catch (NullPointerException e) {
		}
		mAdapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(exhibit_lv);
		disableAutoScrollToBottom(scrollView1);
		ProgressUtil.dismissProgressDialog();
	};

	/**
	 * 动态设置ListView组建的高度
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 禁止ScrollView的childview自动滑动到底部
	 */
	private void disableAutoScrollToBottom(ScrollView scrollview) {
		scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollview.setFocusable(true);
		scrollview.setFocusableInTouchMode(true);
		scrollview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.navigation_path_img:
			ArrayList<String> fids = new ArrayList<>();
			for (int i = 0; i < exhibitBeans.size(); i++) {
				fids.add(exhibitBeans.get(i).exhibitNumber);
			}
			Intent intent = new Intent(mContext, NaviSearchActivity.class);
			intent.putStringArrayListExtra("data", fids);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
