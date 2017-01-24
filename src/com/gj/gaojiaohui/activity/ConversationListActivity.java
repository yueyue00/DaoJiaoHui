package com.gj.gaojiaohui.activity;

import java.net.URI;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

import com.gheng.exhibit.application.DemoContext;
import com.gheng.exhibit.application.MyApplication;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.abconstant.RongCloudEvent;
import com.gj.gaojiaohui.bean.DiaoDuBean;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 会话列表界面
 * 
 * @author zhangt
 * 
 */
public class ConversationListActivity extends FragmentActivity {

	private TextView custom_title_tv;
	private ImageView custom_back_img;
    /** 融云的会话列表界面 */
    ConversationListFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation_list);
		RongCloudEvent.init(this);
		initView();
		enterFragment();
		isReconnect();
		enterFragment();
	}

	private void initView() {
		custom_title_tv = (TextView) findViewById(R.id.custom_title_tv);
		custom_back_img = (ImageView) findViewById(R.id.custom_back_img);

		custom_title_tv.setText(getResources().getString(R.string.message));
		custom_back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 加载 会话列表 ConversationListFragment
	 */
	private void enterFragment() {

		 fragment = new ConversationListFragment();

	        Uri uri = Uri.parse("rong://" + GloableConfig.CURRENT_PKGNAME).buildUpon().appendPath("conversationlist")
	                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") // 设置私聊会话非聚合显示
	                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")// 设置群组会话非聚合显示
	                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")// 设置讨论组会话非聚合显示
	                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")// 设置系统会话非聚合显示
	                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")// 公共服务号
	                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")// 订阅号
	                .build();

	        fragment.setUri(uri);

	        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	        transaction.add(R.id.conversationlist, fragment);
	        transaction.commit();

	}

	/**
	 * 判断消息是否是 push 消息
	 * 
	 */
	private void isReconnect() {

		Intent intent = getIntent();
		String token = null;

		if (DemoContext.getInstance() != null) {

			token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
		}

		// push，通知或新消息过来
		if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

			// 通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
			if (intent.getData().getQueryParameter("push") != null && intent.getData().getQueryParameter("push").equals("true")) {

				reconnect(token);
			} else {
				// 程序切到后台，收到消息后点击进入,会执行这里
				if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

					reconnect(token);
				} else {
					enterFragment();
				}
			}
		}
	}

	/**
	 * 重连
	 * 
	 * @param token
	 */
	private void reconnect(String token) {

		if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

			RongIM.connect(token, new RongIMClient.ConnectCallback() {
				@Override
				public void onTokenIncorrect() {

				}

				@Override
				public void onSuccess(String s) {
					RongCloudEvent.getInstance().setOtherListener();
					enterFragment();
				}

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

				}
			});
		}
	}

}
