package com.gj.gaojiaohui.activity;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.LiangDianAdapter;
import com.gj.gaojiaohui.bean.LiangDianBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smartdot.wenbo.huiyi.R;

/**
 * 亮点推荐的列表界面
 * 
 * @author zhangt
 * 
 */
public class LiangdianActivity extends GaoJiaoHuiBaseActivity implements OnClickListener,
		com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView> {

	private Context mContext;
	private ImageView backImageView;
	private TextView titleTextView;

	private PullToRefreshListView mLiangDianListView;
	private LiangDianAdapter mLiangdianAdapter;
	private List<LiangDianBean> liangDianBeans;

	private int page = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				ProgressUtil.dismissProgressDialog();
				mLiangDianListView.onRefreshComplete();
				parseLiangdianData(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liangdian);
		initView();
		getLiangDianData(page);
	}

	private void initView() {
		mContext = this;
		backImageView = (ImageView) findViewById(R.id.custom_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_tv);
		mLiangDianListView = (PullToRefreshListView) findViewById(R.id.liangdian_lv);
		mLiangDianListView.setMode(Mode.PULL_FROM_END);// 只有上拉加载
		mLiangDianListView.setOnRefreshListener(this);

		titleTextView.setText(getResources().getString(R.string.home_liangdian));
		backImageView.setOnClickListener(this);

		liangDianBeans = new ArrayList<>();
		mLiangdianAdapter = new LiangDianAdapter(mContext, liangDianBeans, R.layout.item_liangdian_listview);
		mLiangDianListView.setAdapter(mLiangdianAdapter);

		mLiangDianListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, LiangDianDetailsActivity.class);
				intent.putExtra("Id", liangDianBeans.get(position - 1).id);
				startActivity(intent);

			}
		});

	}

	/**
	 * 获取亮点推荐数据
	 */
	private void getLiangDianData(int page) {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.LIANGDIAN_URL, GloableConfig.LANGUAGE_TYPE,page);
		volleyUtil1.stringRequest(handler, url, map, 1001);
	}

	/**
	 * 处理亮点推荐返回的数据
	 * 
	 * @param msg
	 */
	private void parseLiangdianData(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				if (jsonArray.length() == 0) {
					CustomToast.showToast(mContext, getResources().getString(R.string.lastestPage));
					return;
				}
				
				List<LiangDianBean> tmpBeans = CommonUtils.gson.fromJson(jsonObject.getString("list"), 
						new TypeToken<List<LiangDianBean>>(){}.getType());
				liangDianBeans.addAll(tmpBeans);
				mLiangdianAdapter.notifyDataSetChanged();
				page++;

			} else {
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			}
		} catch (JSONException e) {
			CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			e.printStackTrace();
		}
	};

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
		getLiangDianData(page);
	}
}
