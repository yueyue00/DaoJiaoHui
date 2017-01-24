package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的赠票界面
 * 
 * @author zhangt
 * 
 */
public class MyTicketsActivity extends Activity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView commitTv;

	private EditText name_et;
	private EditText cert6_et;
	private EditText ticket_et;
	private TextView cert6_tv;

	private int position;
	private String[] types = { "内地身份证", "回乡证", "港澳身份证", "台胞证", "外国人护照", "军官证", "军官离休证", "外交人员身份证" };
	/** 是否已经选择了证件类型 */
	private Boolean hasSelected = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				ProgressUtil.dismissProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						if (jsonObject.getBoolean("success")) {
							CustomToast.showToast(mContext, getString(R.string.eticket_bing_success));
							finish();
						} else {
							CustomToast.showToast(mContext, jsonObject.getString("msg"));
						}

					} else {
						CustomToast.showToast(mContext, getString(R.string.getdata_error));
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getString(R.string.getdata_error));
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_tickets);
		mContext = this;
		initView();
	}

	private void initView() {
		backImageView = (ImageView) findViewById(R.id.custom_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_tv);
		commitTv = (TextView) findViewById(R.id.person_save);
		name_et = (EditText) findViewById(R.id.name_et);
		cert6_et = (EditText) findViewById(R.id.cert6_et);
		ticket_et = (EditText) findViewById(R.id.ticket_et);
		cert6_tv = (TextView) findViewById(R.id.cert6_tv);

		CommonUtils.EmojiFilter(name_et, 30);
		CommonUtils.EmojiFilter(ticket_et, 30);

		commitTv.setVisibility(View.VISIBLE);

		titleTextView.setText(R.string.Eticket_binding);
		commitTv.setText(getString(R.string.ticket_commit));

		backImageView.setOnClickListener(this);
		cert6_et.setOnClickListener(this);
		commitTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.cert6_et:
			if (!hasSelected) {
				showDialog();
			}
			break;
		case R.id.person_save:
			// TODO 提交电子票
			String name = name_et.getText().toString().trim();
			String cert6 = cert6_et.getText().toString().trim();
			String ticket = ticket_et.getText().toString().trim();
			try {
				name = URLEncoder.encode(name, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (StringUtils.isNull(name)) {
				CustomToast.showToast(mContext, getString(R.string.name_cant_null));
			} else if (StringUtils.isNull(cert6)) {
				CustomToast.showToast(mContext, getString(R.string.cert6_cant_null));
			} else if (cert6.length() != 6) {
				CustomToast.showToast(mContext, getString(R.string.cert6_should_complete));
			} else if (StringUtils.isNull(ticket)) {
				CustomToast.showToast(mContext, getString(R.string.ticketid_cant_null));
			} else if (!StringUtils.isUserName(ticket)) {
				CustomToast.showToast(mContext, getString(R.string.ticket_has_special));
			} else {
				commit(name, cert6, ticket);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 提交赠票
	 * 
	 * @param name
	 * @param cert6
	 * @param ticket
	 */
	private void commit(String name, String cert6, String ticket) {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.COMMIT_MYTICKET_URL, GloableConfig.userid, name, cert6, ticket, position + 1);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.choose_document_type);
		builder.setSingleChoiceItems(types, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				position = which;

			}
		});
		builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				hasSelected = true;
				cert6_et.setFocusable(true);
				cert6_et.setFocusableInTouchMode(true);
				cert6_et.setClickable(false);
				cert6_tv.setText(types[position] + getString(R.string.last6));
				cert6_et.requestFocus();
				InputMethodManager imm = (InputMethodManager) cert6_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.dialog_cancle), null);
		builder.create().show();
	}

}
