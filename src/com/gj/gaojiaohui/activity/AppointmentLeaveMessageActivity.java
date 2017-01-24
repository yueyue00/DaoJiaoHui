package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MyLiuYanAdapter;
import com.gj.gaojiaohui.bean.MessageDetailsBean;
import com.gj.gaojiaohui.bean.MessageDetailsChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppointmentLeaveMessageActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {
	private TextView title_tv;
	private ImageView back_img;
	private ListView leaveMessage_lv;
	private EditText leaveMessage_et;
	private TextView leaveMessage_send_tv;
	private List<MessageDetailsChildBean> list;
	private Context mContext;
	private String id;
	private int isExhibitors;
	private String type;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 1002:
				ProgressUtil.dismissProgressDialog();
				parseLiuyanData(msg);
				break;
			}
		}
	};
	private MyLiuYanAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_leave_message);
		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		isExhibitors = intent.getIntExtra("isExhibitors", -1);
		type = intent.getStringExtra("type");
		initView();
		getData();
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "采访预约留言列表："+json);
		// 通过gson将json映射成实体类
		MessageDetailsBean data = CommonUtil.gson.fromJson(json, MessageDetailsBean.class);// 这段崩溃有3种原因
																							// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			// 此步是对数组中的数据做清空,并重新添加,再将adapter刷新
			list.clear();
			list.addAll(data.list);
			adapter.notifyDataSetChanged();
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.LEAVEMESSAGE_URL, GloableConfig.userid, id, isExhibitors);
		L.v("lixm", "采访预约留言："+url);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
	}

	private void initView() {

		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.message_details_title));
		back_img = (ImageView) findViewById(R.id.custom_back_img);

		leaveMessage_lv = (ListView) findViewById(R.id.leaveMessage_lv);
		leaveMessage_et = (EditText) findViewById(R.id.leaveMessage_et);
		CommonUtils.EmojiFilter(leaveMessage_et);
		CommonUtils.stringFilter(leaveMessage_et);
		leaveMessage_send_tv = (TextView) findViewById(R.id.leaveMessage_send_tv);
		leaveMessage_send_tv.setOnClickListener(this);

		list = new ArrayList<MessageDetailsChildBean>();
		adapter = new MyLiuYanAdapter(mContext, list, R.layout.liuyanxq_item);
		leaveMessage_lv.setAdapter(adapter);
		back_img.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.leaveMessage_send_tv:
			try {
				String editContent = leaveMessage_et.getText().toString();
				if (!TextUtils.isEmpty(editContent)) {
					String value = URLEncoder.encode(leaveMessage_et.getText().toString().trim().replace("\"", "\\\""), "UTF-8");
					ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

					VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
					Map<String, String> map = new HashMap<>();// Map为空，必须要有
					String url = String.format(GloableConfig.COMMIT_LEAVEMESSAGE_URL, GloableConfig.userid, id, value, type);// type值为0或2
					L.v("lixm", "采访预约提交留言："+url);																											// 根据入口来判断，如果是从观众互动进入则type为0，如果是从展商详情进入则type为2
					volleyUtil1.stringRequest(handler, url, map, 1002);// 1001是msg.what
				} else {
					CustomToast.showToast(mContext, getResources().getString(R.string.liuyan_empty));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/** 留言结果 */
	private void parseLiuyanData(Message msg) {
		String json = msg.obj.toString();
		L.v("lixm", "采访预约提交留言结果："+json);
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {// 留言成功刷新数据
			getData();
			leaveMessage_et.setText("");
			CommonUtils.hideKeyboard(this);
		}
	}
}
