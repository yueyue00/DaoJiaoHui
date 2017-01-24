package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.PersonalDataBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 个人资料
 * 
 * @author Administrator
 * 
 */
public class PersonalDataActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private TextView title_tv;
	private ImageView personal_data_fanhui;
	private EditText editname;
	private TextView person_save;
	private EditText editemail;
	private EditText editpost;
	private EditText editcompany;
	private TextView contact_information;
	private PersonalDataBean personalDataBean;
	String name, email, company, post, invitecode;
	Context mContext;
	/** 是否正在编辑 */
	private Boolean isEditing = false;

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
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						GloableConfig.username = name;
						SharePreferenceUtils.setParam("user_name", Constant.encode(Constant.key, name));
						isEditing = false;
						person_save.setText(getResources().getString(R.string.edit));
						CustomToast.showToast(mContext, getResources().getString(R.string.setSuccess));
						person_save.setVisibility(View.GONE);
						editname.setFocusableInTouchMode(false);
						editname.clearFocus();
						editemail.setFocusableInTouchMode(false);
						editemail.clearFocus();
						editcompany.setFocusableInTouchMode(false);
						editcompany.clearFocus();
						editpost.setFocusableInTouchMode(false);
						editpost.clearFocus();
					} else {
						CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
					e.printStackTrace();
				}

				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_data);
		mContext = this;
		SharePreferenceUtils.getAppConfig(this);
		initView();
		getData();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.personal_data));
		contact_information = (TextView) findViewById(R.id.contact_information);
		personal_data_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		person_save = (TextView) findViewById(R.id.person_save);

		person_save.setText(getResources().getString(R.string.edit));
		person_save.setVisibility(View.VISIBLE);

		editname = (EditText) findViewById(R.id.editname);
		editemail = (EditText) findViewById(R.id.editemail);
		editcompany = (EditText) findViewById(R.id.editcompany);
		editpost = (EditText) findViewById(R.id.editpost);

		personal_data_fanhui.setOnClickListener(this);
		person_save.setOnClickListener(this);

		CommonUtils.EmojiFilter(editname, 20);
		CommonUtils.EmojiFilter(editemail, 40);
		CommonUtils.EmojiFilter(editcompany, 20);
		CommonUtils.EmojiFilter(editpost, 20);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		personalDataBean = CommonUtils.gson.fromJson(msg.obj.toString(), PersonalDataBean.class);
		if (personalDataBean != null && personalDataBean.resultCode == 200) {
			editname.setText(personalDataBean.name);
			editemail.setText(personalDataBean.email);
			editcompany.setText(personalDataBean.company);
			editpost.setText(personalDataBean.position);
			contact_information.setText(personalDataBean.tel);
		} else {
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	private void getData() {
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.USER_INFO_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/**
	 * 保存数据
	 */
	private void saveData() {
		name = editname.getText().toString().trim();
		email = editemail.getText().toString().trim();
		company = editcompany.getText().toString().trim();
		post = editpost.getText().toString().trim();

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		}

		if (StringUtils.isNull(name)) {
			CustomToast.showToast(mContext, getResources().getString(R.string.nameIsNull));
			return;
		} else if (!StringUtils.isEmail(email) && !StringUtils.isNull(email)) {
			CustomToast.showToast(mContext, getResources().getString(R.string.emailIsError));
			return;
		}

		ProgressUtil.showPregressDialog(mContext);

		String userid = Constant.encode(Constant.key, GloableConfig.userid);

		name = Constant.encode(Constant.key, name);
		email = Constant.encode(Constant.key, email);
		company = Constant.encode(Constant.key, company);
		post = Constant.encode(Constant.key, post);

		try {
			name = URLEncoder.encode(name, "UTF-8");
			email = URLEncoder.encode(email, "UTF-8");
			company = URLEncoder.encode(company, "UTF-8");
			post = URLEncoder.encode(post, "UTF-8");
			userid = URLEncoder.encode(userid, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.SAVE_PERSON_INFO_URL, userid, name, email, company, post);
		volleyUtil.stringRequest(handler, url, map, 1002);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.custom_back_img:
			finish();
			break;
		// 保存设置
		case R.id.person_save:
			if (isEditing) {
				saveData();
			} else {
				isEditing = true;
				editname.setFocusableInTouchMode(true);
				editemail.setFocusableInTouchMode(true);
				editcompany.setFocusableInTouchMode(true);
				editpost.setFocusableInTouchMode(true);
				person_save.setText(getResources().getString(R.string.save_contacts));
			}
			break;
		default:
			break;
		}
	}

	/** 触摸空白区域关闭软键盘 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		}
		return false;
	}

}
