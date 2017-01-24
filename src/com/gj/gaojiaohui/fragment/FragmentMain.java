package com.gj.gaojiaohui.fragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.ConversationListActivity;
import com.gj.gaojiaohui.activity.DaHuiEmergencyNoticeActivity;
import com.gj.gaojiaohui.activity.DaHuiNaviActivity;
import com.gj.gaojiaohui.activity.GaoJiaoHuiBaseActivity;
import com.gj.gaojiaohui.activity.GaojiaoMainActivity;
import com.gj.gaojiaohui.activity.LiangDianDetailsActivity;
import com.gj.gaojiaohui.activity.LiangdianActivity;
import com.gj.gaojiaohui.activity.MeetingNewsActivity;
import com.gj.gaojiaohui.activity.NoticeCenterActivity;
import com.gj.gaojiaohui.activity.ZiXunDetailActivity;
import com.gj.gaojiaohui.adapter.PullListViewAdapter;
import com.gj.gaojiaohui.bean.HomePageBean;
import com.gj.gaojiaohui.bean.HomePageNewsListBean;
import com.gj.gaojiaohui.qrcode.activity.CaptureActivity;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.NetUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.MyListener;
import com.gj.gaojiaohui.view.PullToRefreshLayout;
import com.gj.gaojiaohui.view.PullableScrollView;
import com.gj.gaojiaohui.view.PullableScrollView.onRefreshLisenter;
import com.gj.gaojiaohui.view.PullableScrollView.onScrollChangeLisenter;
import com.gj.gaojiaohui.view.ScrollHeadView;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class FragmentMain extends Fragment implements MyListener, OnClickListener, onScrollChangeLisenter, onRefreshLisenter {

	private Context mContext;

	private DisplayImageOptions options;

	private PullableScrollView pullableScrollView;

	/** 新闻列表 */
	private ListView mListView;
	/** 新闻adapter */
	private PullListViewAdapter mAdapter;
	/** 新闻数据 */
	private List<HomePageNewsListBean> mNewsBeans;
	/** 带刷新的布局 */
	private PullToRefreshLayout mPullToRefreshLayout;

	/** 语言 */
	private LinearLayout language_ll;
	/** 消息 */
	private LinearLayout message_ll;
	/** 扫面二维码 */
	private LinearLayout scanning_ll;
	/** 消息和二维码之间的线 */
	private View message_line;

	/** 顶部渐变背景 */
	private static ImageView titleBg;
	/** 语言tv */
	private static TextView languageTV;
	/** 消息tv */
	private static TextView messageTV;
	/** 扫描tv */
	private static TextView scanningTV;
	/** 新消息提醒的红点 */
	private static View message_point;
	/** 头图 */
	private ImageView homepage_top_img;
	/** 头图上的标题 */
	private TextView home_title_contain_tv;

	/** 通知第一条 */
	private RelativeLayout tongzhiRelativeLayout1;
	/** 通知第二条 */
	private RelativeLayout tongzhiRelativeLayout2;
	/** 通知第一条内容 */
	private TextView home_tongzhi_tv1;
	/** 通知第二条内容 */
	private TextView home_tongzhi_tv2;
	/** 亮点推荐第一条 */
	private RelativeLayout notifiction_rl1;
	/** 亮点推荐第一条内容 */
	private TextView notifiction_tv1;
	/** 亮点推荐第一条图 */
	private ImageView notifiction_img;

	/** 更多通知 */
	private TextView tongzhi_more;
	/** 更多亮点 */
	private TextView liangdian_more;
	/** 更多资讯 */
	private TextView home_zixun_more_tv;

	/** 展馆导航 */
	private ImageView navigation_img;

	private static ImageView loadingImageView;
	/** 均匀旋转动画 */
	private RotateAnimation refreshingAnimation;

	/** 全部数据 */
	private HomePageBean homePageBean;
	/** 大会资讯的请求页数 (加载更多从第二页开始请求，正常获取数据page已经写死为1) */
	private int page = 2;

	private ScrollHeadView scrollHeadView;
	/** 无网界面 */
	private RelativeLayout net_less_ll;
	/** 无网刷新 */
	private LinearLayout netless_refresh_ll;
	/** 头图的黑色蒙版 */
	private View home_img_top_bg;

	private ImageView netless_offline_img;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				parseData(msg);
			} else if (msg.what == 1002) {
				parseLoadMore(msg);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mian, null);
		mContext = getActivity();
		initView(view);
		getData();
		return view;
	}

	private void initView(View view) {
		options = ImageLoaderUtils.initOptions();

		pullableScrollView = (PullableScrollView) view.findViewById(R.id.homepage_pullableScrollView);
		pullableScrollView.setOnScrollChangeLisenter(this);
		pullableScrollView.setOnRefreshLisenter(this);

		language_ll = (LinearLayout) view.findViewById(R.id.language_ll);
		message_ll = (LinearLayout) view.findViewById(R.id.message_ll);
		scanning_ll = (LinearLayout) view.findViewById(R.id.scanning_ll);
		message_line = view.findViewById(R.id.message_line);
		homepage_top_img = (ImageView) view.findViewById(R.id.homepage_top_img);
		home_title_contain_tv = (TextView) view.findViewById(R.id.home_title_contain_tv);

		titleBg = (ImageView) view.findViewById(R.id.titleBg);
		languageTV = (TextView) view.findViewById(R.id.language_tv);
		messageTV = (TextView) view.findViewById(R.id.message_tv);
		scanningTV = (TextView) view.findViewById(R.id.scanning_tv);
		loadingImageView = (ImageView) view.findViewById(R.id.home_loading_img);
		message_point = view.findViewById(R.id.message_point);

		tongzhiRelativeLayout1 = (RelativeLayout) view.findViewById(R.id.home_tongzhi_rl1);
		tongzhiRelativeLayout2 = (RelativeLayout) view.findViewById(R.id.home_tongzhi_rl2);
		notifiction_rl1 = (RelativeLayout) view.findViewById(R.id.notifiction_rl);
		home_tongzhi_tv1 = (TextView) view.findViewById(R.id.home_tongzhi_tv1);
		home_tongzhi_tv2 = (TextView) view.findViewById(R.id.home_tongzhi_tv2);
		notifiction_tv1 = (TextView) view.findViewById(R.id.notifiction_tv);
		notifiction_img = (ImageView) view.findViewById(R.id.notifiction_img);

		mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		mPullToRefreshLayout.setOnRefreshListener(this);
		mListView = (ListView) view.findViewById(R.id.mlistview);
		tongzhi_more = (TextView) view.findViewById(R.id.home_tongzhi_more_tv);
		liangdian_more = (TextView) view.findViewById(R.id.home_liangdian_more_tv);
		home_zixun_more_tv = (TextView) view.findViewById(R.id.home_zixun_more_tv);

		navigation_img = (ImageView) view.findViewById(R.id.navigation_img);

		net_less_ll = (RelativeLayout) view.findViewById(R.id.home_net_less);
		netless_refresh_ll = (LinearLayout) view.findViewById(R.id.netless_refresh_ll);
		netless_offline_img = (ImageView) view.findViewById(R.id.netless_offline_img);

		if (!NetUtils.isConnected(mContext)) {
			netless_offline_img.setVisibility(View.GONE);
			net_less_ll.setVisibility(View.VISIBLE);
			mPullToRefreshLayout.setVisibility(View.GONE);
		} else {
			net_less_ll.setVisibility(View.GONE);
			mPullToRefreshLayout.setVisibility(View.VISIBLE);
		}

		homepage_top_img.setOnClickListener(this);
		language_ll.setOnClickListener(this);
		message_ll.setOnClickListener(this);
		scanning_ll.setOnClickListener(this);
		tongzhi_more.setOnClickListener(this);
		liangdian_more.setOnClickListener(this);
		home_zixun_more_tv.setOnClickListener(this);
		tongzhiRelativeLayout1.setOnClickListener(this);
		tongzhiRelativeLayout2.setOnClickListener(this);
		notifiction_rl1.setOnClickListener(this);
		netless_refresh_ll.setOnClickListener(this);
		navigation_img.setOnClickListener(this);

		mNewsBeans = new ArrayList<>();
		mAdapter = new PullListViewAdapter(mContext, mNewsBeans, R.layout.item_home_list);
		mListView.setAdapter(mAdapter);

		scrollHeadView = new ScrollHeadView(mContext);
		mListView.addHeaderView(scrollHeadView.getView());
		setListViewHeightBasedOnChildren(mListView);

		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.rotating);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		refreshingAnimation.setInterpolator(lir);
		loadingImageView.startAnimation(refreshingAnimation);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, ZiXunDetailActivity.class);
				intent.putExtra("title", homePageBean.news_list.get(position - 1).title);
				intent.putExtra("content", homePageBean.news_list.get(position - 1).value);
				intent.putExtra("imgurl", homePageBean.news_list.get(position - 1).pic);
				intent.putExtra("webviewUrl", homePageBean.news_list.get(position - 1).url);
				startActivity(intent);
			}
		});

	}

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
	 * 改变loading条的透明度
	 * 
	 * @param value
	 */
	public static void ChangeLoading(float value) {
		loadingImageView.setAlpha(value);
	}

	/**
	 * 请求数据
	 */
	private void getData() {
		if (NetUtils.isConnected(mContext)) {
			net_less_ll.setVisibility(View.GONE);
			mPullToRefreshLayout.setVisibility(View.VISIBLE);
		} else {
			net_less_ll.setVisibility(View.VISIBLE);
			mPullToRefreshLayout.setVisibility(View.GONE);
		}
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.HOME_PAGE_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/**
	 * 加载更多（大会资讯）
	 */
	private void loadMore() {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		volleyUtil.onStop();
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.HOME_MORE_NEWS_URL, page, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1002);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// 加载操作
		loadMore();
	}

	/**
	 * 控制消息的角标是否显示
	 */
	public static void showPoint(Boolean isShow) {
		if (isShow) {
			message_point.setVisibility(View.VISIBLE);
		} else {
			message_point.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.homepage_top_img:
			// Intent intentTop = new Intent(mContext,
			// AddScheduleActivity.class);
			// mContext.startActivity(intentTop);
			break;
		case R.id.language_ll:
			String languageString = (String) SharePreferenceUtils.getParam("language", "zh");
			GaoJiaoHuiBaseActivity activity = (GaoJiaoHuiBaseActivity) getActivity();
			if (languageString.equals("zh")) {
				activity.switchLanguage("en");
				GloableConfig.LANGUAGE_TYPE = "2";
			} else if (languageString.equals("en")) {
				activity.switchLanguage("zh");
				GloableConfig.LANGUAGE_TYPE = "1";
			}
			// 更新语言后，destroy当前页面，重新绘制
			getActivity().finish();
			Intent it = new Intent(getActivity(), GaojiaoMainActivity.class);
			startActivity(it);
			break;
		case R.id.message_ll:
			Intent intentMsg = new Intent(mContext, ConversationListActivity.class);
			mContext.startActivity(intentMsg);
			break;
		case R.id.scanning_ll:
			Intent intent = new Intent(mContext, CaptureActivity.class);
			intent.putExtra("isToMap", false);
			startActivityForResult(intent, 2001);
			break;
		case R.id.home_tongzhi_more_tv:
			Intent intent2 = new Intent(mContext, NoticeCenterActivity.class);
			startActivity(intent2);
			break;
		case R.id.home_liangdian_more_tv:
			Intent intent3 = new Intent(mContext, LiangdianActivity.class);
			startActivity(intent3);
			break;
		case R.id.home_zixun_more_tv:
			Intent intent9 = new Intent(mContext, MeetingNewsActivity.class);
			startActivity(intent9);
			break;
		case R.id.home_tongzhi_rl1:
			Intent intent4 = new Intent(mContext, DaHuiEmergencyNoticeActivity.class);
			intent4.putExtra("notifiContent", homePageBean.inform_list.get(0).content);
			intent4.putExtra("notifiTitle", homePageBean.inform_list.get(0).title);
			intent4.putExtra("notifiDate", homePageBean.inform_list.get(0).date);
			startActivity(intent4);
			break;
		case R.id.home_tongzhi_rl2:
			Intent intent5 = new Intent(mContext, DaHuiEmergencyNoticeActivity.class);
			intent5.putExtra("notifiContent", homePageBean.inform_list.get(1).content);
			intent5.putExtra("notifiTitle", homePageBean.inform_list.get(1).title);
			intent5.putExtra("notifiDate", homePageBean.inform_list.get(1).date);
			startActivity(intent5);
			break;
		case R.id.notifiction_rl:
			Intent intent6 = new Intent(mContext, LiangdianActivity.class);
			startActivity(intent6);
			// Intent intent6 = new Intent(mContext,
			// LiangDianDetailsActivity.class);
			// intent6.putExtra("Id",
			// homePageBean.recommend_list.get(0).news_id);
			// startActivity(intent6);
			break;
		case R.id.notifiction_rl2:
			Intent intent7 = new Intent(mContext, LiangDianDetailsActivity.class);
			intent7.putExtra("Id", homePageBean.recommend_list.get(1).news_id);
			startActivity(intent7);
			break;
		case R.id.netless_refresh_ll:
			getData();
			break;
		case R.id.navigation_img:
			Intent intent8 = new Intent(mContext, DaHuiNaviActivity.class);
			intent8.putExtra("mapType", "indoor");
			startActivity(intent8);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			CustomToast.showToast(mContext, data.getStringExtra("qr_result"));
		}
	}

	/**
	 * 解析首页数据
	 * 
	 * @param msg
	 */
	private void parseData(android.os.Message msg) {
		ProgressUtil.dismissProgressDialog();
		homePageBean = CommonUtils.gson.fromJson(msg.obj.toString(), HomePageBean.class);
		if (homePageBean.resultCode == 200) {
			mNewsBeans.clear();
			for (int i = 0; i < homePageBean.news_list.size(); i++) {
				mNewsBeans.add(homePageBean.news_list.get(i));
			}
			scrollHeadView.refreshData(homePageBean.news_top_list);
			mAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(mListView);
			if (homePageBean.is_msg) {
				message_ll.setVisibility(View.VISIBLE);
				message_line.setVisibility(View.VISIBLE);
			} else {
				message_ll.setVisibility(View.GONE);
				message_line.setVisibility(View.GONE);
			}
			ImageLoader.getInstance().displayImage(homePageBean.top.pic, homepage_top_img, options);
			ImageLoader.getInstance().displayImage(homePageBean.exhibition, navigation_img, options);
			home_title_contain_tv.setText(homePageBean.top.title);

			if (homePageBean.inform_list.size() >= 2) {
				tongzhiRelativeLayout1.setVisibility(View.VISIBLE);
				tongzhiRelativeLayout2.setVisibility(View.VISIBLE);
			} else if (homePageBean.inform_list.size() == 1) {
				tongzhiRelativeLayout1.setVisibility(View.VISIBLE);
				tongzhiRelativeLayout2.setVisibility(View.GONE);
			} else {
				tongzhiRelativeLayout1.setVisibility(View.GONE);
				tongzhiRelativeLayout2.setVisibility(View.GONE);
			}

			for (int i = 0; i < homePageBean.inform_list.size(); i++) {
				if (i == 0) {
					home_tongzhi_tv1.setText(homePageBean.inform_list.get(0).title);

				} else if (i == 1) {
					home_tongzhi_tv2.setText(homePageBean.inform_list.get(1).title);
				} else {
					break;
				}
			}

			if (homePageBean.recommend_list.size() > 0) {
				notifiction_rl1.setVisibility(View.VISIBLE);
			} else {
				notifiction_rl1.setVisibility(View.GONE);
			}

			if (homePageBean.recommend_list.size() > 0) {
				// notifiction_tv1.setText(homePageBean.recommend_list.get(0).news_title);
				ImageLoader.getInstance().displayImage(homePageBean.recommend_list.get(0).news_pic, notifiction_img, options);
			}
			page = 2;
		} else {
			CustomToast.showToast(mContext, getActivity().getResources().getString(R.string.getdata_error));
		}
	}

	private void parseLoadMore(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				List<HomePageNewsListBean> tmPageNewsListBeans = CommonUtils.gson.fromJson(jsonObject.getString("list"),
						new TypeToken<List<HomePageNewsListBean>>() {
						}.getType());
				if (tmPageNewsListBeans.size() > 0) {
					homePageBean.news_list.addAll(tmPageNewsListBeans);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(mListView);
					mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					page++;
				} else {
					CustomToast.showToast(mContext, getActivity().getResources().getString(R.string.lastestPage));
					mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}

			} else {
				mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
			}
		} catch (JSONException e) {
			mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
			e.printStackTrace();
		}
	}

	@Override
	public void changeAlphe(int value) {
		float percentage = ((float) value / (float) 600);
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		nt.format(percentage);

		if (percentage < 0.1) {
			languageTV.setVisibility(View.VISIBLE);
			messageTV.setVisibility(View.VISIBLE);
			scanningTV.setVisibility(View.VISIBLE);
		}
		if (percentage > 0.9) {
			languageTV.setVisibility(View.GONE);
			messageTV.setVisibility(View.GONE);
			scanningTV.setVisibility(View.GONE);
		}
		titleBg.setAlpha(percentage);
		languageTV.setAlpha(1 - percentage);
		messageTV.setAlpha(1 - percentage);
		scanningTV.setAlpha(1 - percentage);

	}

	@Override
	public void refresh() {
		getData();
	}

	@Override
	public void onResume() {
		if (!NetUtils.isConnected(mContext)) {
			netless_offline_img.setVisibility(View.GONE);
			net_less_ll.setVisibility(View.VISIBLE);
			mPullToRefreshLayout.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			if (!NetUtils.isConnected(mContext)) {
				netless_offline_img.setVisibility(View.GONE);
				net_less_ll.setVisibility(View.VISIBLE);
				mPullToRefreshLayout.setVisibility(View.GONE);
			} else {
				if (net_less_ll.getVisibility() == View.VISIBLE) {
					getData();
				}
			}

		}
		super.onHiddenChanged(hidden);
	}

}
