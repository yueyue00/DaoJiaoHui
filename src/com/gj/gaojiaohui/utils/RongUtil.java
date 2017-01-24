package com.gj.gaojiaohui.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.abconstant.RongCloudEvent;
import com.gj.gaojiaohui.activity.ConversationListActivity;
import com.smartdot.wenbo.huiyi.R;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 融云工具类
 * 
 * @author zhangt
 */
public class RongUtil {

	/**
	 * 连接融云服务器
	 */
	public static void RongConnect(final Context mContext, String token) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);
		RongIM.connect(token, new RongIMClient.ConnectCallback() {
			@Override
			public void onTokenIncorrect() {
				L.v("连接融云服务器失败，token失效");
				ProgressUtil.dismissProgressDialog();
			}

			@Override
			public void onSuccess(String s) {
				L.v("连接融云服务器成功，融云返回的id--------" + s);
				CustomToast.showToast(mContext, "连接融云成功");
				RongCloudEvent.getInstance().setOtherListener();
				RongIM.getInstance().enableNewComingMessageIcon(true);// 显示新消息提醒
				RongIM.getInstance().enableUnreadMessageIcon(true);// 显示未读消息数目
				// 设置当前用户信息
				if (RongIM.getInstance() != null) {
					RongIM.getInstance().setCurrentUserInfo(new UserInfo(s, GloableConfig.username, null));
					// 设置消息体内是否携带用户信息
					RongIM.getInstance().setMessageAttachedUserInfo(true);
				}
				ProgressUtil.dismissProgressDialog();
				Intent intent = new Intent(mContext, ConversationListActivity.class);
				mContext.startActivity(intent);
			}

			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				L.v("onError errorcode:" + errorCode.getValue());
			}
		});
	}

	/**
	 * 发起聊天
	 * 
	 * @param mContext
	 * @param type
	 * @param targetId
	 * @param title
	 */
	public static void startChat(Context mContext, Conversation.ConversationType type, String targetId, String title) {
		if (mContext != null && !StringUtils.isNull(targetId)) {
			if (RongContext.getInstance() == null) {
				throw new ExceptionInInitializerError("RongCloud SDK not init");
			} else {
				Uri uri = Uri.parse("rong://" + GloableConfig.CURRENT_PKGNAME).buildUpon().appendPath("conversation").appendPath(type.getName().toLowerCase())
						.appendQueryParameter("targetId", targetId).appendQueryParameter("title", title).build();
				mContext.startActivity(new Intent("android.intent.action.VIEW", uri));
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

}
