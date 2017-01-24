package com.gj.gaojiaohui.activity;

import io.rong.imkit.RongIM;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.CheckVersionUpdateUtils;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.ReSetUrl;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 设置
 * 
 * @author Administrator
 * 
 */
public class SetActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	int i = 0;
	private TextView title_tv;
	private ImageView set_fanhui;
	private CheckBox remind_button;
	private RelativeLayout contact_rl;
	private RelativeLayout about_us_rl;
	private RelativeLayout test_edition_rl;
	private RelativeLayout cancellation_rl;
	private TextView edition_num;
	private Context mContext;
	private static final int MSG_SET_ALIAS = 1001;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				L.d("lixm", "Set alias in handler.");
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;

			default:
				L.i("lixm", "Unhandled msg - " + msg.what);
			}
		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				ProgressUtil.dismissProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						GloableConfig.VERSION = jsonObject.getString("versions");
						GloableConfig.DOWNLOAD_APK = jsonObject.getString("down_url");
						CheckVersionUpdateUtils utils = CheckVersionUpdateUtils.createCheckVersionUpdateUtils(SetActivity.this);
						utils.getVersionInfoCompare();
					} else {
						CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		mContext = this;
		SharePreferenceUtils.getAppConfig(this);
		initView();
	}

	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.set_title));
		set_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		remind_button = (CheckBox) findViewById(R.id.remind_button);
		edition_num = (TextView) findViewById(R.id.edition_num);
		L.v("lixm", "setActivity新消息提醒:" + GloableConfig.NEW_MESSAGE_STATE);
		if (GloableConfig.NEW_MESSAGE_STATE) {
			remind_button.setChecked(true);
			JPushInterface.resumePush(getApplicationContext());
		} else {
			remind_button.setChecked(false);
			JPushInterface.stopPush(getApplicationContext());
		}
		remind_button.setSelected(true);
		contact_rl = (RelativeLayout) findViewById(R.id.contact_rl);
		about_us_rl = (RelativeLayout) findViewById(R.id.about_us_rl);
		test_edition_rl = (RelativeLayout) findViewById(R.id.test_edition_rl);
		cancellation_rl = (RelativeLayout) findViewById(R.id.cancellation_rl);
		// 未登录状态没有注销
		if (!TextUtils.isEmpty(GloableConfig.userid)) {
			cancellation_rl.setVisibility(View.VISIBLE);
		} else {
			cancellation_rl.setVisibility(View.GONE);
		}

		cancellation_rl.setOnClickListener(this);
		test_edition_rl.setOnClickListener(this);
		contact_rl.setOnClickListener(this);
		about_us_rl.setOnClickListener(this);
		set_fanhui.setOnClickListener(this);
		remind_button.setOnClickListener(this);

		edition_num.setText(CommonUtils.getCurrentAppVersion(mContext));

	}

	/**
	 * 检查更新
	 */
	private void ChackUpdata() {
		GloableConfig.VERSION = CommonUtils.getVersion(mContext);
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		volleyUtil.stringRequest(handler, GloableConfig.CHACK_UPDATA, map, 1001);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.custom_back_img:
			finish();
			break;
		// 新消息提醒
		case R.id.remind_button:
			if (GloableConfig.NEW_MESSAGE_STATE) {
				remind_button.setChecked(false);
				L.v("lixm", "新消息提醒-状态为未选中,停止推送服务");
				JPushInterface.stopPush(getApplicationContext());
				// 设置新消息提醒状态为true
				GloableConfig.NEW_MESSAGE_STATE = false;
			} else {
				remind_button.setChecked(true);
				L.v("lixm", "新消息提醒-状态为选中，恢复推送服务");
				if (JPushInterface.isPushStopped(getApplicationContext())) {// 如果推送服务被停止过，需要用resumePush来恢复推送服务
					JPushInterface.resumePush(getApplicationContext());
				}
				// 设置新消息提醒状态为true
				GloableConfig.NEW_MESSAGE_STATE = true;
			}
			L.v("lixm", "新消息提醒:" + GloableConfig.NEW_MESSAGE_STATE);
			break;
		// 关于我们
		case R.id.about_us_rl:
			startActivity(new Intent(this, AboutOurselvesActivity.class));
			break;
		// 联系我们
		case R.id.contact_rl:
			startActivity(new Intent(this, ContactUsActivity.class));
			break;
		// 版本更新
		case R.id.test_edition_rl:
			ChackUpdata();
			break;
		// 注销
		case R.id.cancellation_rl:
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle(R.string.isLogout).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					logout();
				}

			}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}

			});

			builder.create();
			builder.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 注销
	 */
	private void logout() {
		if (StringUtils.isNull(GloableConfig.userid)) {
			CustomToast.showToast(mContext, mContext.getResources().getString(R.string.not_login));
			return;
		}
		SharePreferenceUtils.setParam("user_id", "");
		SharePreferenceUtils.setParam("user_permission", "");
		SharePreferenceUtils.setParam("user_name", "");
		GloableConfig.userid = "";
		GloableConfig.username = "";
		GloableConfig.permission = "";
		Constant.removeCookie(mContext);
		ReSetUrl.resetUrl(true);
		// TODO 退出融云/退出极光
		// 设置极光别名为空字符串--该设置不会增量设置，会覆盖之前的设置
		RongIM.getInstance().logout();
		try {
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPushInterface.resumePush(getApplicationContext());// 在注销的时候将极光推送功能恢复
		// 设置新消息提醒状态为true
		SharePreferenceUtils.setParam("newMessageAlert", true);
		Intent intent1 = new Intent(mContext, GaojiaoMainActivity.class);
		Intent intent2 = new Intent(mContext, LoginActivity.class);
		GaoJiaoHuiBaseActivity.exitAllAct();
		startActivities(new Intent[] { intent1, intent2 });
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				L.i("lixm", logs);
				SharePreferenceUtils.setParam("aliasState", true);
				// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				L.i("lixm", logs);
				// 延迟 60 秒来调用 Handler 设置别名
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;
			default:
				logs = "Failed with errorCode = " + code;
				L.e("lixm", logs);
			}
			// ExampleUtil.showToast(logs, getApplicationContext());
		}
	};
}
