package com.gj.gaojiaohui.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.qrcode.activity.CaptureActivity;
import com.smartdot.wenbo.huiyi.R;

/**
 * volley 请求工具类
 */
public class VolleyUtil {

	private String TAG = "fate";

	private Context mContext;

	/**
	 * 请求队列
	 */
	RequestQueue mQueue;

	public VolleyUtil(Context mContext) {
		this.mContext = mContext;
		mQueue = Volley.newRequestQueue(mContext);
	}

	/**
	 * post 请求
	 * 
	 * @param handler
	 * @param url
	 *            请求地址
	 * @param paramMap
	 *            请求param
	 * @param what
	 *            msg.what值
	 */
	public void stringRequest(final Handler handler, String url, final Map<String, String> paramMap, final int what) {
		if (!NetUtils.isConnected(mContext)) {
			CustomToast.showToast(mContext, mContext.getString(R.string.net_error), 400);
			ProgressUtil.dismissProgressDialog();
			return;
		}
		NormalPostRequest normalPostRequest = new NormalPostRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Log.d(TAG, response);
//				L.v(response);
				Message message = new Message();
				message.obj = response;
				message.what = what;
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ProgressUtil.dismissProgressDialog();
				if (CaptureActivity.instance != null) {
					CaptureActivity.instance.finish();
				}
				CustomToast.showToast(mContext, VolleyErrorHelper.getMessage(error, mContext));
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Cookie", Constant.getCookie(mContext, GloableConfig.domain)); // 此处是cookie
				return headers;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return paramMap;
			}

		};
		// 设置超时时间 现在是10s
		normalPostRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(normalPostRequest);

	}

	/**
	 * volley string 请求，自己填写请求方法 这个方法没有加header ，请求融云的数据时候用·
	 * 
	 * @param handler
	 * @param method
	 * @param url
	 * @param what
	 */
	public void stringRequest(final Handler handler, int method, String url, final int what) {
		if (!NetUtils.isConnected(mContext)) {
			CustomToast.showToast(mContext, mContext.getString(R.string.net_error), 400);
			ProgressUtil.dismissProgressDialog();
			return;
		}
		NormalPostRequest normalPostRequest = new NormalPostRequest(method, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Log.d(TAG, response);
				Message message = new Message();
				message.obj = response;
				message.what = what;
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e(TAG, error.getMessage(), error);
				VolleyErrorHelper.getMessage(error, mContext);
			}
		});
		// 设置超时时间 现在是10s
		normalPostRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(normalPostRequest);
	}

	/**
	 * volley jsonObjectRequest 请求方法 自定义方式
	 * 
	 * @param handler
	 * @param url
	 * @param what
	 */
	public void jsonObjectRequest(final Handler handler, String url, final int what) {
		if (!NetUtils.isConnected(mContext)) {
			CustomToast.showToast(mContext, mContext.getString(R.string.net_error), 400);
			ProgressUtil.dismissProgressDialog();
			return;
		}
		NormalPostJsonRequest normalPostJsonRequest = new NormalPostJsonRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// Log.d("TAG", response.toString());
				Message message = new Message();
				message.obj = response;
				message.what = what;
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e("TAG", error.getMessage(), error);
				VolleyErrorHelper.getMessage(error, mContext);
			}
		});
		normalPostJsonRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(normalPostJsonRequest);
	}

	/**
	 * volley jsonObjectRequest 请求方法 自定义方式
	 * 
	 * @param handler
	 * @param method
	 * @param url
	 * @param what
	 */
	public void jsonObjectRequest(final Handler handler, int method, String url, final int what) {
		if (!NetUtils.isConnected(mContext)) {
			CustomToast.showToast(mContext, mContext.getString(R.string.net_error), 400);
			ProgressUtil.dismissProgressDialog();
			return;
		}
		NormalPostJsonRequest normalPostJsonRequest = new NormalPostJsonRequest(method, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// Log.d("TAG", response.toString());
				Message message = new Message();
				message.obj = response;
				message.what = what;
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e("TAG", error.getMessage(), error);
				VolleyErrorHelper.getMessage(error, mContext);
			}
		});
		normalPostJsonRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(normalPostJsonRequest);
	}

	/**
	 * 取消所有请求
	 */
	public void onStop() {
		mQueue.cancelAll(null);// 取消请求队列里面所有的方法
		ProgressUtil.dismissProgressDialog();
	}

	// 加header的使用方法
	// VolleyUtil volleyUtil = new VolleyUtil(mContext);
	// Map<String, String> map = new HashMap<>();
	// map.put("userId", userId);
	// volleyUtil.stringRequest(handler, GloableConfig.UserinfoUrl, map, 1001);
	//
	// 不加header的使用方法
	// VolleyUtil volleyUtil = new VolleyUtil(mContext);
	// String url = String.format(GloableConfig.RongCloud.getGroupListUrl,
	// GloableConfig.myUserInfo.userId);
	// volleyUtil.stringRequest(handler, Request.Method.POST, url, 1001);
}
