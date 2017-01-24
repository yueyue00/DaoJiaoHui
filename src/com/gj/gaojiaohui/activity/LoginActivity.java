package com.gj.gaojiaohui.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Base64;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.EncodEncryUtil;
import com.gheng.exhibit.utils.SharedData;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.abconstant.RongCloudEvent;
import com.gj.gaojiaohui.bean.AllUserBean;
import com.gj.gaojiaohui.bean.CommonResultBean;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.gj.gaojiaohui.bean.UserNameLoginBean;
import com.gj.gaojiaohui.bean.YanZMLoginBean;
import com.gj.gaojiaohui.bean.YanZhiBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.ReSetUrl;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.zNetLoginTask;
import com.smartdot.wenbo.huiyi.R;

/**
 * 登录界面
 * 
 * @author zhangt
 * 
 */
public class LoginActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private Context mContext;
	private ImageView backImageView;
	private TextView registerTextView;
	private TextView loginTextView;

	// 登陆界面-手机号登录
	private View loginLine;
	private View phoneView;
	private TextView change_to_username_login;
	private Boolean usePhoneLogin;
	private Button phone_login_btn;
	private EditText phone_et;
	private EditText validation_et;
	private TextView login_send_code_tv;
	private String phoneString;
	private String validationString;
	private TextView phone_getpassword_tv;

	// 登陆界面-用户名密码登录
	private View usernameView;
	private TextView change_to_phone_login;
	private Button username_login_btn;
	private EditText login_username_et;
	private EditText login_password_et;
	private TextView username_getpassword_tv;
	private String usernameString;
	private String passwordString;

	// 注册界面
	private View registerLine;
	private View registerView;
	private Button register_btn;
	private EditText register_phone_et;
	private TextView register_send_code_tv;
	private EditText register_validation_et;
	private EditText register_password_et;
	private String register_phoneString;
	private String register_validationString;
	private String register_passwordString;

	private Boolean isPhoneLogin = false; // 登录方式 是否是手机登录

	private String TOKEN = "";
	/** 全部人员 */
	private List<AllUserBean> allUserBeans = new ArrayList<>();
	/** 全部群组 */
	private List<DiaoDuBean> groupBeans = new ArrayList<>();

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1001:
				parseUserList(msg);
				break;
			case 1002:
				parseGroupInfo(msg);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SharePreferenceUtils.getAppConfig(this);
		initView();
		changeView(1);
	}

	private void initView() {
		mContext = this;
		backImageView = (ImageView) findViewById(R.id.login_back_img);
		registerTextView = (TextView) findViewById(R.id.register_tv);
		loginTextView = (TextView) findViewById(R.id.login_tv);
		registerLine = findViewById(R.id.register_line);
		loginLine = findViewById(R.id.login_line);
		registerView = findViewById(R.id.register_part);
		phoneView = findViewById(R.id.phone_part);
		usernameView = findViewById(R.id.username_part);
		change_to_phone_login = (TextView) findViewById(R.id.change_to_phone_login);
		change_to_username_login = (TextView) findViewById(R.id.change_to_username_login);
		phone_login_btn = (Button) findViewById(R.id.phone_login_btn);
		username_login_btn = (Button) findViewById(R.id.username_login_btn);
		register_btn = (Button) findViewById(R.id.register_btn);
		register_phone_et = (EditText) findViewById(R.id.register_phone_et);
		register_send_code_tv = (TextView) findViewById(R.id.register_send_code_tv);
		register_validation_et = (EditText) findViewById(R.id.register_validation_et);
		register_password_et = (EditText) findViewById(R.id.register_password_et);
		login_username_et = (EditText) findViewById(R.id.login_username_et);
		login_password_et = (EditText) findViewById(R.id.login_password_et);
		username_getpassword_tv = (TextView) findViewById(R.id.username_getpassword_tv);
		phone_et = (EditText) findViewById(R.id.phone_et);
		validation_et = (EditText) findViewById(R.id.validation_et);
		login_send_code_tv = (TextView) findViewById(R.id.login_send_code_tv);
		phone_getpassword_tv = (TextView) findViewById(R.id.phone_getpassword_tv);

		backImageView.setOnClickListener(this);
		registerTextView.setOnClickListener(this);
		loginTextView.setOnClickListener(this);
		change_to_phone_login.setOnClickListener(this);
		change_to_username_login.setOnClickListener(this);
		phone_login_btn.setOnClickListener(this);
		username_login_btn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		register_send_code_tv.setOnClickListener(this);
		username_getpassword_tv.setOnClickListener(this);
		login_send_code_tv.setOnClickListener(this);
		phone_getpassword_tv.setOnClickListener(this);

	}

	/**
	 * 切换登录界面
	 * 
	 * @param value
	 *            1:注册 2：电话登录 3：用户名、密码登录
	 */
	private void changeView(int value) {
		switch (value) {
		case 1:
			registerTextView.setTextColor(getResources().getColor(R.color.login_title_sel));
			loginTextView.setTextColor(getResources().getColor(R.color.login_title_nor));
			registerLine.setVisibility(View.VISIBLE);
			loginLine.setVisibility(View.INVISIBLE);
			registerView.setVisibility(View.VISIBLE);
			phoneView.setVisibility(View.GONE);
			usernameView.setVisibility(View.GONE);
			break;
		case 2:
			registerTextView.setTextColor(getResources().getColor(R.color.login_title_nor));
			loginTextView.setTextColor(getResources().getColor(R.color.login_title_sel));
			registerLine.setVisibility(View.INVISIBLE);
			loginLine.setVisibility(View.VISIBLE);
			registerView.setVisibility(View.GONE);
			phoneView.setVisibility(View.VISIBLE);
			usernameView.setVisibility(View.GONE);
			break;
		case 3:
			registerTextView.setTextColor(getResources().getColor(R.color.login_title_nor));
			loginTextView.setTextColor(getResources().getColor(R.color.login_title_sel));
			registerLine.setVisibility(View.INVISIBLE);
			loginLine.setVisibility(View.VISIBLE);
			registerView.setVisibility(View.GONE);
			phoneView.setVisibility(View.GONE);
			usernameView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back_img:
			finish();
			break;
		case R.id.register_tv:
			changeView(1);
			break;
		case R.id.login_tv:
			if (isPhoneLogin) {
				changeView(2);
			} else {
				changeView(3);
			}
			break;
		case R.id.change_to_phone_login:
			isPhoneLogin = true;
			changeView(2);
			break;
		case R.id.change_to_username_login:
			isPhoneLogin = false;
			changeView(3);
			break;
		case R.id.phone_login_btn:
			phoneString = phone_et.getText().toString().trim();
			validationString = validation_et.getText().toString().trim();
			if (!StringUtils.isMobile(phoneString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.edit_phonenum_toast));
			} else if (StringUtils.isAsNull(validationString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.edit_validation_toast));
			} else {
				// CustomToast.showToast(mContext, "使用手机号登录");
				loginByYanZM();
			}
			break;
		case R.id.username_login_btn:
			usernameString = login_username_et.getText().toString().trim();
			passwordString = login_password_et.getText().toString().trim();
			if (StringUtils.isAsNull(usernameString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.login_username_toast));
			} else if (StringUtils.isAsNull(passwordString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.login_password_toast));
			} else if (!StringUtils.isUserName(usernameString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.login_not_username));
			} else {
				// CustomToast.showToast(mContext, "使用用户名密码登录");
				getSalt();
			}
			break;
		case R.id.register_btn:
			register_phoneString = register_phone_et.getText().toString().trim();
			register_validationString = register_validation_et.getText().toString().trim();
			register_passwordString = register_password_et.getText().toString().trim();
			if (StringUtils.isAsNull(register_phoneString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.register_phone_toast));
			} else if (StringUtils.isAsNull(register_validationString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.register_validation_toast));
			} else if (StringUtils.isAsNull(register_passwordString)) {
				CustomToast.showToast(mContext, getResources().getString(R.string.login_password_toast));
			} else if (register_phoneString.length() < 6) {
				CustomToast.showToast(mContext, getResources().getString(R.string.password_lesserror_toast));
			} else {
				// CustomToast.showToast(mContext, "注册");
				registerByPhone();
			}
			break;
		case R.id.register_send_code_tv:
			register_phoneString = register_phone_et.getText().toString().trim();
			if (StringUtils.isMobile(register_phoneString)) {
				// CustomToast.showToast(mContext, "发送验证码");
				getYanZhengMa("register");
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.edit_phonenum_toast));
			}
			break;
		case R.id.username_getpassword_tv:
			Intent intent = new Intent(mContext, GetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.login_send_code_tv:
			phoneString = phone_et.getText().toString().trim();
			if (StringUtils.isMobile(phoneString)) {
				// CustomToast.showToast(mContext, "发送验证码");
				getYanZhengMa("login");
			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.edit_phonenum_toast));
			}
			break;
		case R.id.phone_getpassword_tv:
			Intent intent1 = new Intent(mContext, GetPasswordActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}

	}

	/**
	 * 融云 - 建立与融云服务器的连接
	 * 
	 * @param token
	 */
	private void connect(String token) {

		if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onTokenIncorrect() {
					ProgressUtil.dismissProgressDialog();
				}

				/**
				 * 连接融云成功
				 */
				@Override
				public void onSuccess(final String userid) {
					GloableConfig.IS_OFFLINE = false;
					RongCloudEvent.getInstance().setOtherListener();
					RongIM.getInstance().enableNewComingMessageIcon(true);// 显示新消息提醒
					RongIM.getInstance().enableUnreadMessageIcon(true);// 显示未读消息数目
					// 设置当前用户信息
					String username = (String) SharePreferenceUtils.getParam("user_name", "");
					try {
						final String decodeName = Constant.decode(Constant.key, username);
						if (RongIM.getInstance() != null) {
							try {
								RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, decodeName, null));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						for (int i = 0; i < groupBeans.size(); i++) {
							Group group;
							try {
								group = new Group(groupBeans.get(i).id, groupBeans.get(i).name, null);
							} catch (RuntimeException e) {
								group = new Group(groupBeans.get(i).id, "默认", null);
							}
							RongIM.getInstance().refreshGroupInfoCache(group);
						}
						ProgressUtil.dismissProgressDialog();
						// 设置消息体内是否携带用户信息
						RongIM.getInstance().setMessageAttachedUserInfo(true);
						reStartMainActivity();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

				/**
				 * 连接融云失败
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
				}
			});
		}
	}

	/**
	 * 通过手机号注册
	 */
	public void registerByPhone() {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
			return;
		}
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		ClientParams client = new ClientParams();
		client.url = "hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=phoneRegister&tel=");
		try {
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, register_phoneString), "UTF-8"));
			strbuf.append("&number=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, register_validationString), "UTF-8"));
			strbuf.append("&password=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, register_passwordString), "UTF-8"));
			strbuf.append("&user_id=");
			if (yanzm != null)
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, yanzm.user_id), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		zNetLoginTask<CommonResultBean> net = new zNetLoginTask<CommonResultBean>(handler_registerbyphone.obtainMessage(), client, CommonResultBean.class,
				new CommonResultBean(), mContext);
		net.execute();
	}

	/**
	 * 获取验证码请求
	 * 
	 * @param request_type
	 */
	public void getYanZhengMa(String request_type) {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
			return;
		}
		// 加载网络时等待对话框
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		ClientParams client = new ClientParams();
		client.url = "hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=yzCode&tel=");
		try {
			switch (request_type) {
			case "register":
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, register_phoneString), "UTF-8"));
				break;
			case "login":
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, phoneString), "UTF-8"));
				break;
			default:
				break;
			}
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
	 * 使用手机号+验证码登陆
	 */
	public void loginByYanZM() {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
			return;
		}
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		ClientParams client = new ClientParams();
		client.url = "hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=phoneLogin&tel=");
		try {
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, phoneString), "UTF-8"));
			strbuf.append("&password=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, validationString), "UTF-8"));
			strbuf.append("&userid=");
			if (yanzm != null)
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, yanzm.user_id), "UTF-8"));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		zNetLoginTask<UserNameLoginBean> net = new zNetLoginTask<UserNameLoginBean>(loginbyuser_hand.obtainMessage(), client, UserNameLoginBean.class,
				new UserNameLoginBean(), mContext);
		net.execute();
	}

	/**
	 * 获取盐值请求
	 */
	public void getSalt() {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
			return;
		}
		// 加载网络时等待对话框
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		ClientParams client = new ClientParams();
		client.url = "saltaction.do";
		StringBuffer strbuf = new StringBuffer();
		// des加密后
		strbuf.append("method=getSalt&userid=");
		try {
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, usernameString), "UTF-8"));
			strbuf.append("&password=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, passwordString), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		Constant.removeCookie(mContext);// 发送验证码接口会生成新cookie，所以先删除旧cookie，等待新cookie
		zNetLoginTask<YanZhiBean> net = new zNetLoginTask<YanZhiBean>(handyz.obtainMessage(), client, YanZhiBean.class, new YanZhiBean(), mContext);
		net.execute();

	}

	/**
	 * 使用用户名密码登陆请求
	 * 
	 * @param sal
	 */
	public void loginByUserName(String sal) {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
			return;
		}
		ClientParams client = new ClientParams();
		client.url = "hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		// des加密后
		strbuf.append("method=passwordLogin&userName=");
		try {
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, usernameString), "UTF-8"));
			strbuf.append("&password=");
			strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, EncodEncryUtil.Salt(passwordString, sal)), "UTF-8"));
			strbuf.append("&lg=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, "2"), "UTF-8")); // 获取是中英文
				// 2是英文 1是中文
			} else {
				strbuf.append(URLEncoder.encode(Constant.encode(Constant.key, "1"), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String str = strbuf.toString();
		client.params = str;
		zNetLoginTask<UserNameLoginBean> net = new zNetLoginTask<UserNameLoginBean>(loginbyuser_hand.obtainMessage(), client, UserNameLoginBean.class,
				new UserNameLoginBean(), mContext);
		net.execute();
	}

	/**
	 * 获取盐值访问接口返回的handler
	 */
	Handler handyz = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				YanZhiBean yanzhi = (YanZhiBean) msg.obj;
				switch (yanzhi.resultCode) {
				case "200":
					String sal = new String(Base64.decode(yanzhi.salt));
					loginByUserName(sal);
					break;
				case "300":
					ProgressUtil.dismissProgressDialog();
					Toast.makeText(mContext, getResources().getString(R.string.user_isexist_toast), 1000).show();
					break;
				case "500":
					ProgressUtil.dismissProgressDialog();
					Toast.makeText(mContext, getResources().getString(R.string.login_error_hint), 1000).show();
					break;
				default:
					ProgressUtil.dismissProgressDialog();
					break;
				}
			}
		}
	};
	/**
	 * 输入用户名密码登陆访问接口返回的handler（手机号+验证码也会走到这里）
	 */
	Handler loginbyuser_hand = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				UserNameLoginBean denglu = (UserNameLoginBean) msg.obj;
				switch (denglu.resultCode) {
				case "200":
					if (denglu.user_id != null) {
						SharePreferenceUtils.setParam("user_id", Constant.encode(Constant.key, denglu.user_id));
						GloableConfig.userid = denglu.user_id;
					}
					if (denglu.user_permission != null) {
						SharePreferenceUtils.setParam("user_permission", denglu.user_permission);
						GloableConfig.permission = denglu.user_permission;
					}
					if (denglu.name != null) {
						SharePreferenceUtils.setParam("user_name", Constant.encode(Constant.key, denglu.name));
						GloableConfig.username = denglu.name;
					}
					if (denglu.token != null) {
						TOKEN = denglu.token;
						getUserList();
					}
					break;
				case "0":
					Toast.makeText(mContext, getResources().getString(R.string.login_error_hint), 1000).show();
					break;
				case "500":
					ProgressUtil.dismissProgressDialog();
					Toast.makeText(mContext, getResources().getString(R.string.login_error_hint), 1000).show();
					break;
				default:
					break;
				}
			}
		}
	};
	/**
	 * 发送验证码handler
	 */
	YanZMLoginBean yanzm;
	Handler hand_yzm = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				ProgressUtil.dismissProgressDialog();
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
		}
	};
	/** 手机/验证码 注册 */
	Handler handler_registerbyphone = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				// time.start();
				ProgressUtil.dismissProgressDialog();
				CommonResultBean response = (CommonResultBean) msg.obj;
				switch (response.resultCode) {
				case "200":
					Toast.makeText(mContext, getResources().getString(R.string.register_success_toast), 1000).show();
					changeView(3);
					register_phone_et.setText("");
					register_validation_et.setText("");
					register_password_et.setText("");
					phone_et.setText(register_phoneString);
					login_username_et.setText(register_phoneString);
					break;
				case "1":
					Toast.makeText(mContext, getResources().getString(R.string.network), 1000).show();
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
				case "300":
					Toast.makeText(mContext, getResources().getString(R.string.user_isexist_toast), 1000).show();
					break;
				case "500":
					Toast.makeText(mContext, getResources().getString(R.string.network), 1000).show();
					break;
				default:
					break;
				}
			}
		}
	};

	/**
	 * 获取所有用户列表
	 */
	private void getUserList() {
		if (GloableConfig.permission.equals("3")) {
			// 如果是普通游客就不需要执行这个请求
			// Intent intent = new Intent(mContext, GaojiaoMainActivity.class);
			// startActivity(intent);
			// finish();
			// 普通用户也登录到融云上，为了实现账号之间的互踢
			connect(TOKEN);
			return;
		}
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.GET_USER_LIST_URL, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/**
	 * 获取所有群组列表
	 */
	private void getGroupList() {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.DIAODU_URL, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1002);
	}

	/**
	 * 解析全部用户数据
	 * 
	 * @param msg
	 */
	private void parseUserList(final android.os.Message msg) {
		try {
			final JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				AsyncTask asyncTask = new AsyncTask() {

					@Override
					protected void onPostExecute(Object result) {
						getGroupList();
					}

					@Override
					protected Object doInBackground(Object... params) {
						try {
							allUserBeans = CommonUtils.gson.fromJson(jsonObject.getString("rong_lsit"), new TypeToken<List<AllUserBean>>() {
							}.getType());
							for (int i = 0; i < allUserBeans.size(); i++) {
								GloableConfig.allUserMap.put(allUserBeans.get(i).id, allUserBeans.get(i));
							}
						} catch (JsonSyntaxException | JSONException e) {
							CustomToast.showToast(mContext, getResources().getString(R.string.login_error_hint));
							e.printStackTrace();
						}
						return 1;
					}
				};

				asyncTask.execute();

			} else {
				CustomToast.showToast(mContext, getResources().getString(R.string.login_error_hint));
			}
		} catch (JSONException e) {
			CustomToast.showToast(mContext, getResources().getString(R.string.login_error_hint));
			e.printStackTrace();
		}

	}

	/**
	 * 解析群组信息
	 */
	private void parseGroupInfo(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				JSONObject infoJsonObject = jsonObject.getJSONObject("info");
				groupBeans = CommonUtils.gson.fromJson(infoJsonObject.getString("qunzu"), new TypeToken<List<DiaoDuBean>>() {
				}.getType());
				for (int i = 0; i < groupBeans.size(); i++) {
					GloableConfig.allGroupMap.put(groupBeans.get(i).group_id, groupBeans.get(i));
				}
				connect(TOKEN);
			} else {
				connect(TOKEN);
			}

		} catch (JSONException e) {
			connect(TOKEN);
			e.printStackTrace();
		}
	}

	/**
	 * 重新加载主页面
	 */
	private void reStartMainActivity() {
		if (GloableConfig.permission.equals("0") || GloableConfig.permission.equals("1")) {
			// 如果是vip权限就更换对应的服务器
			ReSetUrl.resetUrl(false);
		}
		Intent intent = new Intent(mContext, GaojiaoMainActivity.class);
		GaoJiaoHuiBaseActivity.exitAllAct();
		startActivity(intent);
	}
}
