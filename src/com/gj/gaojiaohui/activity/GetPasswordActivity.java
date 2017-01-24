package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gj.gaojiaohui.bean.CommonResultBean;
import com.gj.gaojiaohui.bean.YanZMLoginBean;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.zNetLoginTask;
import com.smartdot.wenbo.huiyi.R;

/**
 * 忘记密码的界面
 * 
 * @author zhangt
 * 
 */
public class GetPasswordActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private EditText getpassword_phone_et;
	private EditText getpassword_validation_et;
	private EditText getpassword_password_et;
	private TextView getpassword_send_code_tv;
	private Button getpassword_btn;

	private String phoneString;
	private String validationString;
	private String passwordString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_password);
		initView();
	}

	private void initView() {
		mContext = this;
		backImageView = (ImageView) findViewById(R.id.custom_title_back_img);
		getpassword_phone_et = (EditText) findViewById(R.id.getpassword_phone_et);
		getpassword_validation_et = (EditText) findViewById(R.id.getpassword_validation_et);
		getpassword_password_et = (EditText) findViewById(R.id.getpassword_password_et);
		getpassword_send_code_tv = (TextView) findViewById(R.id.getpassword_send_code_tv);
		getpassword_btn = (Button) findViewById(R.id.getpassword_btn);

		backImageView.setOnClickListener(this);
		getpassword_btn.setOnClickListener(this);
		getpassword_send_code_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_title_back_img:
			finish();
			break;
		case R.id.getpassword_send_code_tv:
			phoneString = getpassword_phone_et.getText().toString().trim();
			if (StringUtils.isMobile(phoneString)) {
				// CustomToast.showToast(mContext, "发送验证码");
				getYanZhengMa();
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.edit_phonenum_toast)

				);
			}
			break;
		case R.id.getpassword_btn:
			phoneString = getpassword_phone_et.getText().toString().trim();
			validationString = getpassword_validation_et.getText().toString().trim();
			passwordString = getpassword_password_et.getText().toString().trim();
			if (StringUtils.isAsNull(phoneString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.register_phone_toast));
			} else if (StringUtils.isAsNull(validationString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.register_validation_toast));
			} else if (StringUtils.isAsNull(passwordString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.login_password_toast));
			} else {
				retrievePassword();
			}
			break;

		default:
			break;
		}

	}

	// 重置密码请求
	public void retrievePassword() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		ClientParams client = new ClientParams();
		client.url = "hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=retrievePassword&tel=");
		try {
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, phoneString), "UTF-8"));
			strbuf.append("&password=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, validationString), "UTF-8"));
			strbuf.append("&new_password=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, passwordString), "UTF-8"));
			strbuf.append("&user_id=");
			if (yanzm != null)
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, yanzm.user_id), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		zNetLoginTask<CommonResultBean> net = new zNetLoginTask<CommonResultBean>(handler_retrievepassword.obtainMessage(), client, CommonResultBean.class,
				new CommonResultBean(), mContext);
		net.execute();
	}

	// 获取验证码请求
	public void getYanZhengMa() {
		// 加载网络时等待对话框
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		ClientParams client = new ClientParams();
		client.url = "hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=yzCode&tel=");
		try {
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, phoneString), "UTF-8"));
			strbuf.append("&lg=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, "2"), "UTF-8")); // 获取是中英文
				// 2是英文 1是中文
			} else {
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, "1"), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		Constant.removeCookie(mContext);// 发送验证码接口会生成新cookie，所以先删除旧cookie，等待新cookie
		zNetLoginTask<YanZMLoginBean> net = new zNetLoginTask<YanZMLoginBean>(hand_yzm.obtainMessage(), client, YanZMLoginBean.class, new YanZMLoginBean(),
				mContext);
		net.execute();
	}

	/**
	 * 发送验证码handler
	 */
	YanZMLoginBean yanzm;
	Handler hand_yzm = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				// time.start();
				yanzm = (YanZMLoginBean) msg.obj;
				switch (yanzm.resultCode) {
				case "200":
					Toast.makeText(mContext, getResources().getString(R.string.send_validation_toast), 1000).show();
					break;
				case "400":
					Toast.makeText(mContext, getResources().getString(R.string.phonenum_unexist_toast), 1000).show();
					break;
				case "500":
					Toast.makeText(mContext, getResources().getString(R.string.network), 1000).show();
					break;
				default:
					break;
				}
			}
			ProgressUtil.dismissProgressDialog();
		}
	};
	Handler handler_retrievepassword = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				// time.start();
				CommonResultBean response = (CommonResultBean) msg.obj;
				switch (response.resultCode) {
				case "200":
					Toast.makeText(mContext, getResources().getString(R.string.getpassword_success_toast), 1000).show();
					break;
				case "2":
					Toast.makeText(mContext, getResources().getString(R.string.validation_overtime_toast), 1000).show();
					break;
				case "3":
					Toast.makeText(mContext, getResources().getString(R.string.phonenum_error_toast), 1000).show();
					break;
				case "4":
					Toast.makeText(mContext, getResources().getString(R.string.validation_error_toast), 1000).show();
					break;
				case "5":
					Toast.makeText(mContext, getResources().getString(R.string.password_lesserror_toast), 1000).show();
					break;
				case "500":
					Toast.makeText(mContext, getResources().getString(R.string.getpassword_failed_toast), 1000).show();
					break;
				}
			}
			ProgressUtil.dismissProgressDialog();
		}
	};
}
