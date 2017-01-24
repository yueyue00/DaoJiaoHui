package com.gj.gaojiaohui.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.gj.gaojiaohui.bean.MenuBean;
import com.gj.gaojiaohui.fragment.FragmentColumn;
import com.gj.gaojiaohui.fragment.FragmentExhibit;
import com.gj.gaojiaohui.fragment.FragmentExhibitForOffline;
import com.gj.gaojiaohui.fragment.FragmentMain;
import com.gj.gaojiaohui.fragment.FragmentSchedule;
import com.gj.gaojiaohui.fragment.FragmentScheduleForOffline;
import com.gj.gaojiaohui.fragment.FragmentSetting;
import com.gj.gaojiaohui.utils.CheckVersionUpdateUtils;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.smartdot.wenbo.huiyi.R;

/**
 * 主Activity 搭载4个导航模块
 */
public class GaojiaoMainActivity extends GaoJiaoHuiBaseActivity implements OnCheckedChangeListener {

	private Context mContext;
	private RadioGroup rg;
	private Map<String, Fragment> fragmentMap;
	private final String MAIN_TAG = "mian";
	private final String SCHEDULE_TAG = "schedule";
	private final String COLUMN_TAG = "column";
	private final String EXHIBIT_TAG = "exhibit";
	private final String SETTING_TAG = "setting";
	private Button column_img;
	private static final int MSG_SET_ALIAS = 1001;
	FragmentExhibit fragmentExhibit = new FragmentExhibit();
	FragmentSchedule fragmentSchedule = new FragmentSchedule();

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1001:
				parseMenu(msg);
				break;
			case 1002:
				parseUpdata(msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gaojiao_main);
		mContext = this;

		setupView();
		switchFragment(MAIN_TAG);
		getMenuList();// 获取中间导航页的数据
		registerJPush();// 注册极光相关
		registerRongIM();// 注册融云相关
		checkVersion();// 检查更新
	}

	/** 注册极光相关 */
	private void registerJPush() {
		if (!TextUtils.isEmpty(GloableConfig.userid)) {
			try {
				mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, GloableConfig.userid));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 游客身份
		}
	}

	/** 注册融云相关关 */
	private void registerRongIM() {
		// 注册融云刷新
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
			}

		}, 500);
		// 融云群组
		for (String key : GloableConfig.allGroupMap.keySet()) {
			DiaoDuBean groupBean = GloableConfig.allGroupMap.get(key);
			Group group = new Group(groupBean.id, groupBean.name, Uri.parse(""));
			RongIM.getInstance().refreshGroupInfoCache(group);
		}
	}

	private void setupView() {
		SharePreferenceUtils.getAppConfig(GaojiaoMainActivity.this);
		// 在进入app的时候要从SharedPreference中把当前应用的语言状态取出来赋值给了GloableConfig.LANGUAGE_TYPE做数据请求使用
		String languageStr = (String) SharePreferenceUtils.getParam("language", "zh");
		if (languageStr.equals("zh")) {// 中文
			GloableConfig.LANGUAGE_TYPE = "1";
		} else if (languageStr.equals("en")) {// 英文
			GloableConfig.LANGUAGE_TYPE = "2";
		}
		rg = (RadioGroup) findViewById(R.id.tab_menu);
		column_img = (Button) findViewById(R.id.column_img);

		init();

		rg.setOnCheckedChangeListener(this);
		column_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MenuActivity.class);
				startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.push_bottom_in, 0);
			}
		});
	}

	private void init() {
		fragmentMap = new HashMap<String, Fragment>();
		fragmentMap.put(MAIN_TAG, new FragmentMain());// 首页
		if (GloableConfig.IS_OFFLINE) {
			fragmentMap.put(SCHEDULE_TAG, new FragmentScheduleForOffline());// 离线模式的日程
			fragmentMap.put(EXHIBIT_TAG, new FragmentExhibitForOffline());// 离线模式的展商
		} else {
			fragmentMap.put(SCHEDULE_TAG, fragmentSchedule);// 日程
			fragmentMap.put(EXHIBIT_TAG, fragmentExhibit);// 展商
		}
		fragmentMap.put(COLUMN_TAG, new FragmentColumn());// 中间的menu
		fragmentMap.put(SETTING_TAG, new FragmentSetting());// 个人
	}

	/** showTag为要现实的fragment对应的标签 */
	private void switchFragment(String showTag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		@SuppressWarnings("rawtypes")
		Iterator iter = fragmentMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			Fragment f = (Fragment) entry.getValue();
			if (f == fragmentMap.get(showTag)) {
				if (!f.isAdded()) {// 未被添加
					transaction.add(R.id.fragment_view, fragmentMap.get(showTag));
				} else if (f.isHidden()) {// 添加了但被隐藏
					transaction.show(f);
				}
			} else if (f.isAdded() && !f.isHidden()) {// 被添加但未被隐藏
				transaction.hide(f);
			}
		}
		transaction.commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.tab_main:
			switchFragment(MAIN_TAG);
			break;

		case R.id.tab_schedule:
			switchFragment(SCHEDULE_TAG);
			break;

		case R.id.tab_column:
			switchFragment(COLUMN_TAG);
			break;

		case R.id.tab_exhibit:
			switchFragment(EXHIBIT_TAG);
			break;

		case R.id.tab_setting:
			switchFragment(SETTING_TAG);
			break;

		default:
			break;
		}
	}

	/** 角标监听 */
	RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			// 消息数改变
			if (count > 0) {
				// mainfragment显示角标
				FragmentMain.showPoint(true);
			} else {
				FragmentMain.showPoint(false);
			}
		}
	};

	/** 来信息时的角标 */
	Conversation.ConversationType[] conversationTypes = { Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
			Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.PUBLIC_SERVICE,
			Conversation.ConversationType.APP_PUBLIC_SERVICE };

	/** 获取中间导航页的数据 */
	private void getMenuList() {
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.ALL_MENU_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}

	/** 解析中间导航页的数据 */
	private void parseMenu(android.os.Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				GloableConfig.menuBeans = CommonUtils.gson.fromJson(jsonObject.getString("company_list"), new TypeToken<List<MenuBean>>() {
				}.getType());
			} else {
				GloableConfig.menuBeans = new ArrayList<>();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	/** 检查更新 */
	private void checkVersion() {
		GloableConfig.VERSION = CommonUtils.getVersion(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		volleyUtil.stringRequest(handler, GloableConfig.CHACK_UPDATA, map, 1002);
	}

	/** 解析检查更新的结果 */
	private void parseUpdata(Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());
			if (jsonObject.getInt("resultCode") == 200) {
				GloableConfig.VERSION = jsonObject.getString("versions");
				GloableConfig.DOWNLOAD_APK = jsonObject.getString("down_url");
				if (GloableConfig.VERSION.compareTo(CommonUtils.getVersion(mContext)) == 1) {
					CheckVersionUpdateUtils utils = CheckVersionUpdateUtils.createCheckVersionUpdateUtils(GaojiaoMainActivity.this);
					utils.getVersionInfoCompareNoToast();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(mContext); // 极光统计
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(mContext); // 极光统计
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeAllFragmentsFromMap();
	}

	/** 移除所有的fragment */
	private void removeAllFragmentsFromMap() {
		fragmentMap.clear();
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				L.v("lixm=======极光设置别名成功");
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

	/** 自定义title布局中的 离线模式点击事件 */
	public void offlineModeClick(View view) {
		changeNetWork();
	}

	/** 无网界面 的离线模式 */
	public void netlessOfflineClick(View view) {
		changeNetWork();
	}

	/** 修改网络状态 加载离线/在线数据 */
	public void changeNetWork() {
		if (GloableConfig.IS_OFFLINE) {// 离线
			GloableConfig.IS_OFFLINE = false;
			refreshUI();
		} else {// 在线
			GloableConfig.IS_OFFLINE = true;
			refreshUI();
		}
	}

	/** 重新启动界面 */
	private void refreshUI() {
		finish();
		Intent intent = new Intent(this, GaojiaoMainActivity.class);
		startActivity(intent);
	}

	/** 返回展商模块的显示状态 */
	public boolean getHidden() {
		return fragmentExhibit.isHidden();
	}

	/** 如果录入日程成功将会返回1 并重新请求当前界面数据 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (resultCode == 0) {
				int addSuccess = data.getIntExtra("addSuccess", -1);
				if (addSuccess == 1 && fragmentSchedule.getType() == 1) {
					fragmentSchedule.getData("", 1);
				}
			}
		}
	}

}
