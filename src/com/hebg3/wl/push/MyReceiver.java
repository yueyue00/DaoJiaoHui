package com.hebg3.wl.push;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.gheng.exhibit.model.databases.User;
import com.gj.gaojiaohui.activity.DaHuiEmergencyNoticeActivity;
import com.gj.gaojiaohui.activity.NoticeCenterActivity;
import com.gj.gaojiaohui.bean.GuestBean;
import com.gj.gaojiaohui.bean.NoticeCenterBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public Context context;
	public int noticinumber = 10000;
	User user;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Bundle bundle = intent.getExtras();
		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {//收到推送的自定义消息---lixm
			L.v(TAG, "接收到推送来的自定义消息");
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
            L.v(TAG, "推送接口数据:"+bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String languString = (String) SharePreferenceUtils.getParam("language", "zh");
            L.v(TAG, "当前应用语言:"+languString);
//			NoticeCenterBean bean = CommonUtil.gson.fromJson(bundle.getString(JPushInterface.EXTRA_MESSAGE), NoticeCenterBean.class);
			JSONObject customJson = null;
			String serviceid = null;
			String zhTitle = null;
			String enTitle = null;
			String date = null;
			String zhValue = null;
			String enValue = null;
			String messagetype = null;
			try {
				customJson = new JSONObject(
						bundle.getString(JPushInterface.EXTRA_MESSAGE));
				L.v(TAG, "自定义消息:"+customJson);
				serviceid = customJson.getString("type");// 解析返回的推送类型字段
				zhTitle = customJson.getString("zhTitle");
				enTitle = customJson.getString("enTitle");
				date = customJson.getString("date");
				zhValue = customJson.getString("zhValue");
				enValue = customJson.getString("enValue");
				messagetype = customJson.getString("type");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

					// 弹出通知栏 没有点击事件 因为页面覆盖切换是个问题； 并检查SharedPreferences数值
				NotificationManager nm = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification.Builder notice = new Notification.Builder(context);
				SharePreferenceUtils.getAppConfig(context);
				if (languString.equals("zh")) {
					notice.setContentTitle(zhTitle);
					notice.setContentText(zhValue);// 通知栏显示的内容
					notice.setTicker(zhValue);// 私信内容 发送过来滚动的消息
					notice.setContentIntent(getPendingIntent(date,
							zhTitle, zhValue));
				}else {
					notice.setContentTitle(enTitle);
					notice.setContentText(enValue);// 通知栏显示的内容
					notice.setTicker(enValue);// 私信内容 发送过来滚动的消息
					notice.setContentIntent(getPendingIntent(date,
							enTitle, enValue));
				}
				notice.setSmallIcon(R.drawable.ic_launcher);
				notice.setDefaults(Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE);
				notice.setWhen(System.currentTimeMillis());
				notice.setAutoCancel(true);
				Notification shownotice = notice.getNotification();
				nm.notify(10086, shownotice);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {//==========接受推送通知消息  lixm
			
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			L.v(TAG, "接收到推送下来的通知");
			L.v(TAG, "接收到推送下来的通知的ID:"+notifactionId);
			L.v(TAG, "推送内容-EXTRA_NOTIFICATION_TITLE："+bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
			L.v(TAG, "推送内容-EXTRA_ALERT："+bundle.getString(JPushInterface.EXTRA_ALERT));
			L.v(TAG, "推送内容-EXTRA_EXTRA："+bundle.getString(JPushInterface.EXTRA_EXTRA));
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
		}//=========================================== 
		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {//点击通知打开对应的界面--只对普通推送通知有效  lixm
			  Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			  L.v(TAG, "用户点击了通知");
			  SharePreferenceUtils.getAppConfig(context);
			  String languString = (String) SharePreferenceUtils.getParam("language", "zh");
			  JSONObject customJson = null;
				String serviceid = null;
				String zhTitle = null;
				String enTitle = null;
				String date = null;
				String zhValue = null;
				String enValue = null;
				String messagetype = null;
				try {
					customJson = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					L.v(TAG, "customJson===通知:"+customJson);
					serviceid = customJson.getString("type");// 解析返回的推送类型字段
					zhTitle = customJson.getString("zhTitle");
					enTitle = customJson.getString("enTitle");
					date = customJson.getString("date");
					zhValue = customJson.getString("zhValue");
					enValue = customJson.getString("enValue");
					messagetype = customJson.getString("type");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (languString.equals("zh")) {
					Intent intent2 = new Intent(context,NoticeCenterActivity.class);
		              intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		              intent2.putExtra("notifiContent", zhValue);
		              intent2.putExtra("notifiTitle", zhTitle);
		      		  intent2.putExtra("notifiDate", date);
		              Intent intent3 = new Intent(context,DaHuiEmergencyNoticeActivity.class);
		              intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		              intent3.putExtra("notifiContent", zhValue);
		              intent3.putExtra("notifiTitle", zhTitle);
		      		  intent3.putExtra("notifiDate", date);
		              context.startActivities(new Intent [] {intent2,intent3});
				}else {
					Intent intent2 = new Intent(context,NoticeCenterActivity.class);
		              intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		              intent2.putExtra("notifiContent", enValue);
		              intent2.putExtra("notifiTitle", enTitle);
		      		  intent2.putExtra("notifiDate", date);
		              Intent intent3 = new Intent(context,DaHuiEmergencyNoticeActivity.class);
		              intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		              intent3.putExtra("notifiContent", enValue);
		              intent3.putExtra("notifiTitle", enTitle);
		      		  intent3.putExtra("notifiDate", date);
		              context.startActivities(new Intent [] {intent2,intent3});
				}
              
              
              
              
              
			// 打开自定义的Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// Intent.FLAG_ACTIVITY_CLEAR_TOP );
			// context.startActivity(i);
			// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
              
//			String key = printBundle(bundle);
//			String type = null;
//			String keystr = bundle.getString(key);
//			if (!TextUtils.isEmpty(keystr)) {
//				JSONObject customJson = null;
//				try {
//					customJson = new JSONObject(keystr);
//					System.out.println("lixm-aaa:MyReceiver:customJson"
//							+ customJson.toString());
//					type = customJson.getString("messagetype");// 解析返回的推送类型字段
//
//					// 以下判断是通过messagetype判断的推送类型对不同的类型做不同的解析和跳转操作
//					if (type.equals("0")) {// 解析 会议变更 / 会议召开中
//											// 处判断推送类型的messagetype之外的json、跳转到相应的会议变更
//											// / 会议召开界面
//
//						String messageid = customJson.getString("messageid");
//						// String serviceid = customJson.getString("serviceid");
//						// String messageurl =
//						// customJson.getString("messageurl");
//						String suijima = customJson.getString("suijima");
//						// 打开自定义的Activity
//						Intent i = new Intent(context, HuiYiXiangQingActivity.class);
//						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						i.putExtra("pchi_id", messageid);
//						i.putExtra("suijima", suijima);
//						context.startActivity(i);
//					} else if (type.equals("1")) {// 解析 私信
//													// 除messagetype参数外的json、并跳转到私信界面
//						String serviceid = customJson.getString("serviceid");
//						String servicename = customJson
//								.getString("servicename");
//						Intent i = new Intent();
//						if (user.getUserjuese().equals("1")) {
//							i.setClass(context, SiXinActivity.class);
//						} else {
//							i.setClass(context, FuWuSiXingActivity.class);
//						}
//						// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//						// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						i.putExtra("serviceid", serviceid);
//						i.putExtra("servicename", servicename);
//						System.out.println("serviceid = " + serviceid);
//						System.out.println("/n servicename = " + servicename);
//						context.startActivity(i);
//					} else if (type.equals("2")) {// 解析 vip行程 跳转贵宾行程传tripid
//													// 除messagetype参数外的json、并跳转到vip行程界面
//						String messageid = customJson.getString("messageid");
//						Intent i = new Intent(context,
//								XingChengAnPaiActivity.class);
//						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						i.putExtra("tripid", messageid);
//						context.startActivity(i);
//
//					} else if (type.equals("3")) {// 解析 其他类型 showwebview 传url地址
//													// 除messagetype参数外的json、并跳转到相应界面
//													// String messageid =
//													// customJson.getString("messageid");
//						// String serviceid = customJson.getString("serviceid");
//						String messageurl = customJson.getString("messageurl");
//						System.out.println("aaa:MyReceiver:messageurl"
//								+ messageurl);
//						// 打开自定义的Activity
//						Intent i = new Intent(context,
//								ShowWebViewActivity.class);
//						i.putExtras(bundle);
//						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						i.putExtra("url", messageurl);
//						context.startActivity(i);
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}//=========================================

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	public PendingIntent getPendingIntent(String servicedate, String servicename,
			String content) {
		
		
//		if (user.getUserjuese().equals("1")) {
//			i.setClass(context, SiXinActivity.class);
//		} else {
//			i.setClass(context, FuWuSiXingActivity.class);
//		}
		
//		i.putExtra("servicename", servicename);
//		i.putExtra("content", content);
		Intent i1 = new Intent(context,NoticeCenterActivity.class);
		Intent i2 = new Intent(context,DaHuiEmergencyNoticeActivity.class);
		i1.putExtra("notifiContent", content);
		i2.putExtra("notifiContent", content);
		i1.putExtra("notifiTitle", servicename);
		i1.putExtra("notifiDate", servicedate);
		i2.putExtra("notifiTitle", servicename);
		i2.putExtra("notifiDate", servicedate);
		i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		//notification（通知）--》pendingIntent--延迟意图：当用户出发某个操作才会出发的intent
		PendingIntent pendingIntent = PendingIntent.getActivities(context,
				noticinumber++, new Intent[]{i1,i2}, PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				sb.append(key);
			}
		}
		System.out.println("aaa:MyReceiver:" + sb.toString());
		return sb.toString();
	}

	// send msg to MainActivity
}
