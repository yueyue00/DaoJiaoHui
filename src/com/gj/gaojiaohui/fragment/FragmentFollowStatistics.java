package com.gj.gaojiaohui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.smartdot.wenbo.huiyi.R;

/**
 * 观众互动 - 关注用户统计 add wb 此界面为webView展示
 */
public class FragmentFollowStatistics extends Fragment {
	private View view;
	private Context mContext;
	private WebView webView;
	private String myUrl = "http://fanyi.baidu.com/#zh/en/输入中文直接翻译";

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_follow_statistics, null);
		mContext = getActivity();

		initView();
		return view;
	}

	private void initView() {
		webView = (WebView) view.findViewById(R.id.webapp_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.requestFocus();
		webView.loadUrl(myUrl);
	}
}
