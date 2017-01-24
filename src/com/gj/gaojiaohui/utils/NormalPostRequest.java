package com.gj.gaojiaohui.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 解决volley中文乱码问题
 */
public class NormalPostRequest extends StringRequest {

	public NormalPostRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	/**
	 * 重写以解决乱码问题
	 */
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String str = null;
		try {
			Map<String, String> responseHeaders = response.headers;
			String rawCookies = responseHeaders.get("Set-Cookie");
			str = new String(response.data, "utf-8");
			GloableConfig.COOKIE = rawCookies;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
	}

}