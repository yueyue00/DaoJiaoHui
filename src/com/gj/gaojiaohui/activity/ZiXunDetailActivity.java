package com.gj.gaojiaohui.activity;

import java.io.File;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.gj.gaojiaohui.utils.CustomToast;
import com.smartdot.wenbo.huiyi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 资讯详情界面-使用webview
 * 
 * @author zhangt
 * 
 */
public class ZiXunDetailActivity extends GaoJiaoHuiBaseActivity {

	private ImageView backImageView;
	private TextView title_tv;
	private ImageView share_img;
	private WebView mWebView;
	private ProgressBar progressBar;
	private boolean firstLoad = true;
	private String shareTitle;
	private String shareContent;
	private String shareimgurl;
	private String webviewUrl;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zi_xun_detail);
		mContext = this;
		Intent intent = getIntent();
		shareTitle = intent.getStringExtra("title");
		shareContent = intent.getStringExtra("content");
		shareimgurl = intent.getStringExtra("imgurl");
		webviewUrl = intent.getStringExtra("webviewUrl");
		initView();

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		backImageView = (ImageView) findViewById(R.id.custom_back_img);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText("");
		share_img = (ImageView) findViewById(R.id.share_img);
		share_img.setVisibility(View.VISIBLE);
		progressBar = (ProgressBar) findViewById(R.id.webapp_progress_bar);
		mWebView = (WebView) findViewById(R.id.zixun_webview);

		if (!TextUtils.isEmpty(webviewUrl)) {// 对传递回来的webViewurl做为空判断
			mWebView.loadUrl(webviewUrl);
		} else {
			Toast.makeText(mContext,
					getResources().getString(R.string.empty_toast),
					Toast.LENGTH_SHORT).show();
			// mWebView.loadUrl("http://www.baidu.com");
		}
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.requestFocus();
		initWebview();

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});
		share_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 分享
				showShare();
			}
		});
	}

	/** shareSDK分享 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(shareTitle);// shareTitle
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(webviewUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareContent);// shareContent
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		if (!TextUtils.isEmpty(shareimgurl)) {// 对传递的图片url进行为空判断
			// 图片url不为空
			oks.setImageUrl(shareimgurl);
		} else {
			// 图片url为空
			oks.setImageUrl("http://b371.photo.store.qq.com/psb?/V124uKwa4WjeaQ/6xv7VO5gsiWUjnritePAG0twU*VdMbBw9dgRKGHKoxA!/b/dHMBAAAAAAAA&bo=rQOAAkAGQAQFCAU!&rf=viewer_4");// shareimgUrl
		}
		// oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");//shareimgUrl
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数/sdcard/test.jpg
		// oks.setImagePath(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + File.separator + "cat.jpg");//确保SDcard下面存在此张图片

		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(webviewUrl);// webviewUrl
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");
		// 启动分享GUI
		oks.show(this);
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
				CustomToast.showToast(ZiXunDetailActivity.this, getResources().getString(R.string.load_error)
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
}
