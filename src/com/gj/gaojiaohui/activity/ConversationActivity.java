package com.gj.gaojiaohui.activity;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.application.DemoContext;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.GroupBean;
import com.gheng.exhibit.rongyun.activity.PhotoActivity;
import com.gj.gaojiaohui.abconstant.RongCloudEvent;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.smartdot.wenbo.huiyi.R;

import de.greenrobot.event.EventBus;

/**
 * 会话界面
 * 
 * @author Administrator
 * 
 */
public class ConversationActivity extends FragmentActivity {

	private Context mContext;

	private TextView mTitle;
	private ImageView mBack;
	private Button unReadButton;

	private String mTargetId;
	private String mTargetName;

	/** 群组信息 */
	private List<GroupBean> mList;

	/**
	 * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	 */
	private String mTargetIds;

	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_conversation);
		mContext = this;
		RongCloudEvent.init(this);
		Intent intent = getIntent();
		setActionBar();
		getIntentDate(intent);
		isReconnect(intent);
	}

	/**
	 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
	 */
	@SuppressWarnings("static-access")
	private void getIntentDate(Intent intent) {
		mTargetId = intent.getData().getQueryParameter("targetId");
		mTargetIds = intent.getData().getQueryParameter("targetIds");
		mTargetName = intent.getData().getQueryParameter("title");
		unReadButton = (Button) findViewById(R.id.rc_unread_message_count);

		// intent.getData().getLastPathSegment();//获得当前会话类型
		mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
		if (RongIM.getInstance() != null) {
			RongIM.getInstance().setConversationBehaviorListener(new ConversationBehaviorListener() {

				@Override
				public boolean onUserPortraitLongClick(Context arg0, ConversationType arg1, UserInfo arg2) {
					return false;
				}

				@Override
				public boolean onUserPortraitClick(Context arg0, ConversationType arg1, UserInfo arg2) {
					return false;
				}

				@Override
				public boolean onMessageLongClick(Context arg0, View arg1, Message arg2) {
					return false;
				}

				@Override
				public boolean onMessageLinkClick(Context arg0, String arg1) {
					return false;
				}

				@Override
				public boolean onMessageClick(Context arg0, View arg1, Message message) {
					if (message.getContent() instanceof ImageMessage) {
						ImageMessage imageMessage = (ImageMessage) message.getContent();
						Intent intent = new Intent(ConversationActivity.this, PhotoActivity.class);

						intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
						if (imageMessage.getThumUri() != null)
							intent.putExtra("thumbnail", imageMessage.getThumUri());

						ConversationActivity.this.startActivity(intent);
						L.d("当点击照片后执行");
					}
					return false;
				}
			});
		}
		enterFragment(mConversationType, mTargetId);
		setActionBarTitle(mTargetName);
		UserInfo userInfo = RongContext.getInstance().getUserInfoCache().get(mTargetId);

	}

	/**
	 * 加载会话页面 ConversationFragment
	 * 
	 * @param mConversationType
	 * @param mTargetId
	 */
	@SuppressLint("NewApi")
	private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

		ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase()).appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
	}

	/**
	 * 设置 actionbar 事件
	 */
	private void setActionBar() {

		mTitle = (TextView) findViewById(R.id.custom_title_tv);
		mBack = (ImageView) findViewById(R.id.custom_back_img);

		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				finish();
			}
		});

	}

	/**
	 * 设置 actionbar title
	 */
	private void setActionBarTitle(String targetid) {

		mTitle.setText(targetid);
	}

	/**
	 * 判断消息是否是 push 消息
	 */
	private void isReconnect(Intent intent) {

		String token = null;

		if (DemoContext.getInstance() != null) {

			token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
		}

		// push或通知过来
		if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

			// 通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
			if (intent.getData().getQueryParameter("push") != null && intent.getData().getQueryParameter("push").equals("true")) {

				reconnect(token);
			} else {
				// 程序切到后台，收到消息后点击进入,会执行这里
				if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
					if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus()
							.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
						enterActivity();
					}
				} else {
					enterFragment(mConversationType, mTargetId);
				}
			}
		}
	}

	/**
	 * 收到 push 消息后，选择进入哪个 Activity 如果程序缓存未被清理，进入 MainActivity 程序缓存被清理，进入
	 * LoginActivity，重新获取token
	 * <p/>
	 * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity
	 * 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。 以跳到 MainActivity 为例： 在
	 * ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity
	 * 激活了，当你读完收到的消息点击 返回键 时，程序会退到 MainActivity 页面，而不是直接退回到 桌面。
	 */
	private void enterActivity() {

		String token = (String) SharePreferenceUtils.getParam("TOKEN", "default");
		assert token != null;
		if (token.equals("default")) {
			Intent intent = ConversationActivity.this.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startActivity(intent);
			finish();
		} else {
			reconnect(token);
		}
	}

	/**
	 * 重连
	 * 
	 * @param token
	 */
	@SuppressLint("NewApi")
	private void reconnect(String token) {

		if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

			RongIM.connect(token, new RongIMClient.ConnectCallback() {
				@Override
				public void onTokenIncorrect() {

				}

				@Override
				public void onSuccess(String s) {
					RongCloudEvent.getInstance().setOtherListener();
					enterFragment(mConversationType, mTargetId);
				}

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (resultCode == 0) {
				// 如果 解散/退出 退出群成功 将关闭界面
				int bindSuccess = data.getIntExtra("exitSuccess", -1);
				if (bindSuccess == 1) {
					finish();
				}
			}
			if (resultCode == 101) {
				// 改群名
				mTargetName = data.getStringExtra("groupTitle");
				mTitle.setText(mTargetName);
			}
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void sendMessage(int what) {
		android.os.Message message = new android.os.Message();
		message.what = what;
		EventBus.getDefault().post(message);
	}

}
