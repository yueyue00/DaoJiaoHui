package com.hebg3.wl.net;

import java.lang.reflect.Type;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.gheng.exhibit.utils.Constant;

/**
 * @author ghost
 * @version 创建时间：2013-7-11 上午10:40:35
 * @param <T>
 * 
 */
@SuppressWarnings("rawtypes")
public class zNetLoginTask<T> extends AsyncTask<Void, Void, Void> {
	// Message是在线程之间传递信息，它可以在内部携带少量的信息，用于在不同线程之间交换数据
	private Message msg;
	// params 是指在执行Task时需要传入的参数，可用于在后台任务中使用
	private ClientParams params;
	private Class clazz;
	private ResponseBody<T> body;
	private Type typeToken;
	public Object obj;
	private int isNull = 0;
	private Context context;

	/**
	 * 解析info中为对象的构造方法
	 * 
	 * @param msg
	 * @param params
	 * @param clazz
	 * @param obj
	 */
	public zNetLoginTask(Message msg, ClientParams params, Class clazz, Object obj, Context context) {// 解析json对象调用的构造方法
		this.msg = msg;
		this.params = params;
		this.clazz = clazz;
		this.obj = obj;
		this.context = context;
	}

	/**
	 * 解析info中为数组对象的构造方法
	 * 
	 * @param msg
	 * @param params
	 * @param typeToken
	 */
	public zNetLoginTask(Message msg, ClientParams params, Type typeToken) {// 解析数组json字符串调用的构造方法
		this.params = params;
		this.msg = msg;
		this.typeToken = typeToken;
	}

	/**
	 * 调用这个构造方法用于返回成功值 不需要解析 只需要判断是否返回成功值
	 * 
	 * @param msg
	 * @param params
	 * @param isNull
	 */
	public zNetLoginTask(Message msg, ClientParams params, int isNull) {
		this.msg = msg;
		this.isNull = isNull;
		this.params = params;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		zHttpUrlConnectionLoginLoader con = new zHttpUrlConnectionLoginLoader();
		// System.out.println(params.domain + params.url);
		// System.out.println(params.params);
		con.postDataFromSelf(params, listener, context);
		return null;
	}

	private zOnEntityLoadCompleteListener<Object> listener = new zOnEntityLoadCompleteListener<Object>() {

		@Override
		public void onError() {
			msg.what = 1;
			msg.sendToTarget();
		}

		@Override
		public void onEntityLoadComplete(Object base) {
			try {
				String content = (String) base;
				body = new ResponseBody<T>();
				if (typeToken != null) {
					body.list = Constant.gson.fromJson(content, typeToken);
					if (body.list.size() == 0) {
						msg.what = 3;
						msg.sendToTarget();
					} else {
						msg.what = 0;
						msg.obj = body;
						msg.sendToTarget();
					}
				} else {
					obj = (Object) Constant.gson.fromJson(content, clazz);
					msg.what = 0;
					msg.obj = obj;
					msg.sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(Object entity) {
			msg.what = 1;
			msg.sendToTarget();
		}

	};
}
