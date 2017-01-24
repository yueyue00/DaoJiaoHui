package com.gj.gaojiaohui.activity;

import com.gj.gaojiaohui.utils.CustomToast;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TranslateActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private WebView mWebView;
	private TextView title_tv;
	private ImageView back_img;
	private ProgressBar progressBar;

	private Context mContext;
	private boolean firstLoad = true;
	private String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate);
		mContext = this;
		Intent intent = getIntent();
		value = intent.getStringExtra("value");
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText("");
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		mWebView = (WebView) findViewById(R.id.translate_webview);

		back_img.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.translate_progress_bar);

		mWebView.loadUrl("http://fanyi.baidu.com/#zh/en/" + value);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.requestFocus();
		initWebview();
	}

	private void initWebview() {
		mWebView.setWebChromeClient(new WebChromeClient() {
			// 网页加载进度更新
			@Override
			public void onProgressChanged(WebView view, int progress) {
				if (firstLoad) {
					progressBar.setVisibility(progressBar.VISIBLE);
					progressBar.setProgress(progress);
					if (progress == 100) {
						progressBar.setVisibility(progressBar.GONE);
					}

				}
				super.onProgressChanged(view, progress);
			}

		});

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 设置点击网页里面的链接还是在当前的webview里跳转，而不是跳到浏览器里边。
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mWebView.getSettings().setBlockNetworkImage(true);

				super.onPageStarted(view, url, favicon);
			}

			// 网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				if (firstLoad) {
					firstLoad = false;
					progressBar.setVisibility(progressBar.GONE);
				}
				mWebView.getSettings().setBlockNetworkImage(false);

				super.onPageFinished(view, url);
			}

			// @Override
			// public void onReceivedSslError(WebView view,
			// SslErrorHandler handler, android.net.http.SslError error) {
			// //设置webview处理https请求
			// handler.proceed();
			// }

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// 加载页面报错时的处理
				CustomToast.showToast(mContext, getResources().getString(R.string.load_error) + description);
			}
		});
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	// mWebView.goBack();
	// return true;
	// }
	//
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			// if (mWebView.canGoBack()) {
			// mWebView.goBack();
			// } else {
			// finish();
			// }
			finish();
			break;

		default:
			break;
		}
	}
}
