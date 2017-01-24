package com.gj.gaojiaohui.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import java.io.File;
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
import cn.jpush.android.api.JPushInterface;

import com.baidu.mobstat.StatService;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.abconstant.RongCloudEvent;
import com.gj.gaojiaohui.bean.AllUserBean;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.gj.gaojiaohui.bean.PersonalDataBean;
import com.gj.gaojiaohui.db.FileOperationUtil;
import com.gj.gaojiaohui.utils.CheckVersionUpdateUtils;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.ReSetUrl;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.smartdot.wenbo.huiyi.R;

public class StartActivity extends GaoJiaoHuiBaseActivity {

	Context mContext;
	String muser_id = "";
	String language = "";
	private String TOKEN = "";
	/** 个人信息 */
	private PersonalDataBean personalDataBean;
	/** 全部人员 */
	private List<AllUserBean> allUserBeans = new ArrayList<>();
	/** 全部群组 */
	private List<DiaoDuBean> groupBeans = new ArrayList<>();

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1001:
				parseUpdata(msg);
				break;
			case 1002:
				parsePersonInfo(msg);
				break;
			case 1003:
				parseUserList(msg);
				break;
			case 1004:
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
		setContentView(R.layout.activity_zstart);

		// 每次启动删除展商/互动的离线数据库
		FileOperationUtil.delete(new File(GloableConfig.DB_PATH));

		mContext = this;
		SharePreferenceUtils.getAppConfig(this);
		registerBaiDuYun();
		autoLogoin();
	}

	/** 注册百度云 */
	private void registerBaiDuYun() {
		// 开启百度统计
		StatService.start(this);
		// 开启百度统计的log日志
		StatService.setDebugOn(true);
	}

	/** 自动登录 */
	private void autoLogoin() {
		try {
			muser_id = Constant.decode(Constant.key, (String) SharePreferenceUtils.getParam("user_id", ""));
			language = (String) SharePreferenceUtils.getParam("language", "");
			if (language.equals("en")) {
				language = "2";
			} else {
				language = "1";
			}
			GloableConfig.userid = muser_id;
			GloableConfig.permission = (String) SharePreferenceUtils.getParam("user_permission", "");
			GloableConfig.username = (String) SharePreferenceUtils.getParam("user_name", "");
			GloableConfig.username = Constant.decode(Constant.key, GloableConfig.username);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (GloableConfig.userid.equals("")) {// 用户未登录
			new Thread() {
				public void run() {
					try {
						GloableConfig.IS_OFFLINE = true;
						// 默认进入主界面
						Thread.sleep(2000);
						Intent intent = new Intent(StartActivity.this, GaojiaoMainActivity.class);
						startActivity(intent);
						finish();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} else {// 用户登录过
			if (!IsWebCanBeUse.isWebCanBeUse(getApplicationContext())) {// 没联网直接跳转主界面
				new Thread() {
					public void run() {
						try {
							GloableConfig.IS_OFFLINE = false;
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						GloableConfig.IS_OFFLINE = false;
						// 默认进入主界面
						Intent intent = new Intent(StartActivity.this, GaojiaoMainActivity.class);
						startActivity(intent);
						StartActivity.this.finish();
					}
				}.start();
				return;
			}
			// 启动线程更新个人信息
			String murl = String.format(GloableConfig.USER_INFO_URL, muser_id, language);
			Constant.getDataByVolley(mContext, murl, handler, 1002);
		}
	}

	/**
	 * 获取所有用户列表
	 */
	private void getUserList() {
		if (GloableConfig.permission.equals("3")) {
			// 如果是普通游客就不需要执行这个请求
			// Intent intent = new Intent(mContext, GaojiaoMainActivity.class);
			// startActivity(intent);
			// finish();
			connect(TOKEN);
			return;
		}
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.GET_USER_LIST_URL, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1003);
	}

	/**
	 * 获取所有群组列表
	 */
	private void getGroupList() {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.DIAODU_URL, GloableConfig.userid);
		volleyUtil.stringRequest(handler, url, map, 1004);
	}

	// 融云 - 建立与融云服务器的连接
	private void connect(String token) {
		if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onTokenIncorrect() {
					Intent intent = new Intent(StartActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}

				/**
				 * 连接融云成功
				 */
				@Override
				public void onSuccess(final String userid) {
					RongCloudEvent.init(mContext);
					RongCloudEvent.getInstance().setOtherListener();
					RongIM.getInstance().enableNewComingMessageIcon(true);// 显示新消息提醒
					RongIM.getInstance().enableUnreadMessageIcon(true);// 显示未读消息数目
					String username = (String) SharePreferenceUtils.getParam("user_name", "");
					try {
						final String decodeName = Constant.decode(Constant.key, username);
						// 设置当前用户信息
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
						// 设置消息体内是否携带用户信息
						RongIM.getInstance().setMessageAttachedUserInfo(true);
						Intent intent = new Intent(StartActivity.this, GaojiaoMainActivity.class);
						// 如果是vip权限就更换对应的服务器
						if (GloableConfig.permission.equals("0") || GloableConfig.permission.equals("1")) {
							ReSetUrl.resetUrl(false);
						}
						startActivity(intent);
						finish();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

				/**
				 * 连接融云失败
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
					Intent intent = new Intent(StartActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
	}

	/**
	 * 检查更新的数据解析
	 */
	private void parseUpdata(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {

				GloableConfig.VERSION = jsonObject.getString("versions");
				GloableConfig.DOWNLOAD_APK = jsonObject.getString("down_url");
				if (GloableConfig.VERSION.compareTo(CommonUtils.getVersion(mContext)) == 1) {
					CheckVersionUpdateUtils utils = CheckVersionUpdateUtils.createCheckVersionUpdateUtils(StartActivity.this);
					utils.getVersionInfoCompareNoToast();
				} else {
					autoLogoin();
				}

			} else {
				autoLogoin();
			}
		} catch (JSONException e) {
			autoLogoin();
			e.printStackTrace();
		}
	}

	/**
	 * 解析个人信息
	 */
	private void parsePersonInfo(android.os.Message msg) {
		personalDataBean = CommonUtils.gson.fromJson(msg.obj.toString(), PersonalDataBean.class);
		if (personalDataBean.resultCode == 200) {
			TOKEN = personalDataBean.token;
			GloableConfig.userid = muser_id;
			GloableConfig.username = personalDataBean.name;
			// TODO 在连接融云之前要先把群组信息列表和人员信息列表全都请求下来
			GloableConfig.permission = (String) SharePreferenceUtils.getParam("user_permission", "3");
			getUserList();
			// connect(TOKEN);
		} else {
			Intent intent = new Intent(StartActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 解析全部用户数据
	 * 
	 * @param msg
	 */
	private void parseUserList(android.os.Message msg) {
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
								GloableConfig.allUserMap.put(allUserBeans.get(i).user_id, allUserBeans.get(i));
							}
						} catch (JsonSyntaxException | JSONException e) {
							Intent intent = new Intent(StartActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
							e.printStackTrace();
						}
						return 1;
					}
				};

				asyncTask.execute();

			} else {
				Intent intent = new Intent(StartActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		} catch (JSONException e) {
			Intent intent = new Intent(StartActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
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

	@Override
	protected void onResume() {
		JPushInterface.onResume(mContext); // 极光统计
		StatService.onResume(mContext);// 百度统计
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(mContext); // 极光统计
		StatService.onPause(mContext);// 百度统计
		super.onPause();
	}

}
