package com.gj.gaojiaohui.activity;

import io.rong.imlib.model.Conversation.ConversationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.DiaoDuAdapter;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.RongUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.huiyi.R;

/**
 * 服务调度界面
 * 
 * @author zhangt
 * 
 */
public class DiaoDuActivity extends GaoJiaoHuiBaseActivity {

	private Context mContext;
	private TextView titleTV;
	private ImageView backImg;
	private ListView diaoduLV;
	private DiaoDuAdapter mAdapter;
	private List<DiaoDuBean> mBeans;

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
		setContentView(R.layout.activity_diao_du);
		initView();
		getDatas();
	}

	private void initView() {
		mContext = this;
		titleTV = (TextView) findViewById(R.id.custom_title_tv);
		backImg = (ImageView) findViewById(R.id.custom_back_img);
		diaoduLV = (ListView) findViewById(R.id.diaodu_lv);

		titleTV.setText(getResources().getString(R.string.fuwudiaodu));

		mBeans = new ArrayList<>();
		mAdapter = new DiaoDuAdapter(mContext, mBeans, R.layout.item_diaodu_layout);
		diaoduLV.setAdapter(mAdapter);

		diaoduLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RongUtil.startChat(mContext, ConversationType.GROUP, mBeans.get(position).group_id, mBeans.get(position).name);
			}
		});
	}

	/**
	 * 获取调度群组列表
	 */
	private void getDatas() {

		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.DIAODU_URL, GloableConfig.userid);
		volleyUtil1.stringRequest(handler, url, map, 1001);

	}

	/**
	 * 处理调度列表返回的数据
	 * 
	 * @param msg
	 */
	private void parseData(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				JSONObject infoJsonObject = jsonObject.getJSONObject("info");
				mBeans.clear();
				List<DiaoDuBean> tmpBeans = CommonUtils.gson.fromJson(infoJsonObject.getString("qunzu"), new TypeToken<List<DiaoDuBean>>() {
				}.getType());
				mBeans.addAll(tmpBeans);
				mAdapter.notifyDataSetChanged();
				ProgressUtil.dismissProgressDialog();
			} else {
				ProgressUtil.dismissProgressDialog();
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			}

		} catch (JSONException e) {
			ProgressUtil.dismissProgressDialog();
			CustomToast.showToast(mContext, mContext.getResources().getString(R.string.getdata_error));
			e.printStackTrace();
		}
	};
}
