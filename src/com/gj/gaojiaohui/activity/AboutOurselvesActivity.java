package com.gj.gaojiaohui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.CustomToast;
import com.smartdot.wenbo.huiyi.R;

/**
 * 关于我们
 * 
 * @author Administrator
 * 
 */
public class AboutOurselvesActivity extends GaoJiaoHuiBaseActivity implements
		OnClickListener {

	private TextView title_tv;
	private ImageView about_us_fanhui;
	private WebView mWebView;
	private ProgressBar progressBar;
	private boolean firstLoad = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText(getResources().getString(R.string.about_us_title));
		about_us_fanhui = (ImageView) findViewById(R.id.custom_back_img);
		progressBar = (ProgressBar) findViewById(R.id.about_us_progress_bar);
		mWebView = (WebView) findViewById(R.id.about_us_webview);
		about_us_fanhui.setOnClickListener(this);

		if (GloableConfig.LANGUAGE_TYPE.equals("1")) {
			mWebView.loadUrl(GloableConfig.BASE_URL + "InfoPublish.do?method=viewInfo&language=1&infoid=62850");
		}else {
			mWebView.loadUrl(GloableConfig.BASE_URL + "InfoPublish.do?method=viewInfo&language=1&infoid=62852");
		}
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

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// 加载页面报错时的处理
				CustomToast.showToast(AboutOurselvesActivity.this, getResources().getString(R.string.load_error)
						+ description);
				// Toast.makeText(MainActivity.this,
				// "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				finish();
			}
			break;

		default:
			break;
		}
	}
}
