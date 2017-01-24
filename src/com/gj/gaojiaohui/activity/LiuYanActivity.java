package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.LiuYanDialog;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

import android.app.Activity;
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
import android.widget.TextView;

/**
 * 留言
 * 
 * @author Administrator
 * 
 */
public class LiuYanActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView liuyan_tijiao;
	private ImageView liuyan_fanhui;
	private TextView title_tv;
	private EditText edit_liuyan;
	private LiuYanDialog liuYanDialog;
	private String id;
	private Context mContext;

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
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liu_yan);
		mContext = this;
		initView();
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
	}

	private void initView() {
		liuyan_tijiao = (TextView) findViewById(R.id.liuyan_tijiao);
		liuyan_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.message_title));
		edit_liuyan = (EditText) findViewById(R.id.edit_liuyan);
		liuyan_tijiao.setOnClickListener(this);
		liuyan_fanhui.setOnClickListener(this);
		CommonUtils.EmojiFilter(edit_liuyan);
		CommonUtils.stringFilter(edit_liuyan);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.liuyan_tijiao:
			try {
				String liuyanContent = edit_liuyan.getText().toString();
				if (!TextUtils.isEmpty(liuyanContent)) {
					String value = URLEncoder.encode(edit_liuyan.getText().toString().trim().replace("\"", "\\\\"), "UTF-8");
					// String url =
					// "http://wuzhen.smartdot.com:8088/gaojiao/feedBack.do?method=fkliuyan&userid=lixhe&vipid="
					// + id + "&type=2&value=" + value;//此处type=2
					String url = String.format(GloableConfig.LEAVE_WORD_COMMINT_URL, GloableConfig.userid, id, value, "2");// type值为2
					loadData(url, "展商详情-留言：", 1001);
				} else {
					CustomToast.showToast(mContext, getResources().getString(R.string.liuyan_empty));
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case R.id.custom_back_img:
			finish();
			break;
		default:
			break;
		}
	}

	/** 请求数据 */
	private void loadData(String url, String text, int msgWhat) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, msgWhat);// 1001是msg.what
	}

	/** 留言反馈 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {
			// CustomToast.showToast(mContext, "留言发送成功");
			edit_liuyan.setText("");
			CommonUtils.hideKeyboard(this);
			liuYanDialog = new LiuYanDialog(LiuYanActivity.this);
			liuYanDialog.show();
			TextView back_zhanshangxq = (TextView) liuYanDialog.findViewById(R.id.back_zhanshanxq);
			back_zhanshangxq.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LiuYanActivity.this.finish();
				}
			});
			TextView select_liuyan = (TextView) liuYanDialog.findViewById(R.id.select_liuyan);
			select_liuyan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LiuYanActivity.this, LiuYanParticularsActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("isExhibitors", 0);
					intent.putExtra("type", "2");
					startActivity(intent);
					LiuYanActivity.this.finish();
					liuYanDialog.dismiss();
				}
			});
		}
	}
}
