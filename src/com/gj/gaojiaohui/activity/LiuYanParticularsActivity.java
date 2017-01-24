package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.adapter.MyLiuYanAdapter;
import com.gj.gaojiaohui.bean.MessageDetailsBean;
import com.gj.gaojiaohui.bean.MessageDetailsChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 留言详情
 * 
 * @author Administrator
 * 
 */
public class LiuYanParticularsActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView lyxq_fanhui;
	private ListView liuyanxq_list;
	private EditText edit_liuyan;
	private TextView send_message;
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
		setContentView(R.layout.activity_liu_yan_particulars);

		mContext = this;
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		isExhibitors = intent.getIntExtra("isExhibitors", -1);
		type = intent.getStringExtra("type");
		initView();
		// initData();
		getData();

	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
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
		String url = String.format(GloableConfig.LEAVE_WORD_DETAIL_URL, GloableConfig.userid, id, isExhibitors);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
	}

	private void initData() {
		MessageDetailsChildBean detailsChildBean = new MessageDetailsChildBean();
		detailsChildBean.title = "慧点科技";
		detailsChildBean.value = "北京慧点科技有限公司(简称慧点科技)于1998年八月创立,是以GRC为理念的管理软件与服务提供商.";
		detailsChildBean.date = "2016.9.12 15:32:22";
		for (int i = 0; i < 5; i++) {
			list.add(detailsChildBean);
		}
	}

	private void initView() {

		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.message_details_title));
		lyxq_fanhui = (ImageView) findViewById(R.id.custom_back_img);

		liuyanxq_list = (ListView) findViewById(R.id.liuyanxq_list);
		edit_liuyan = (EditText) findViewById(R.id.edit_liuyan);
		CommonUtils.EmojiFilter(edit_liuyan);
		CommonUtils.stringFilter(edit_liuyan);
		send_message = (TextView) findViewById(R.id.send_message);
		send_message.setOnClickListener(this);

		list = new ArrayList<MessageDetailsChildBean>();
		adapter = new MyLiuYanAdapter(mContext, list, R.layout.liuyanxq_item);
		liuyanxq_list.setAdapter(adapter);
		lyxq_fanhui.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.send_message:
			try {
				String editContent = edit_liuyan.getText().toString();
				if (!TextUtils.isEmpty(editContent)) {
					String value = URLEncoder.encode(edit_liuyan.getText().toString().trim().replace("\"", "\\\""), "UTF-8");
					ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

					VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
					Map<String, String> map = new HashMap<>();// Map为空，必须要有
					String url = String.format(GloableConfig.LEAVE_WORD_COMMINT_URL, GloableConfig.userid, id, value, type);// type值为0或2
																															// 根据入口来判断，如果是从观众互动进入则type为0，如果是从展商详情进入则type为2
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
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {// 留言成功刷新数据
			getData();
			edit_liuyan.setText("");
			CommonUtils.hideKeyboard(this);
		}
	}
}
