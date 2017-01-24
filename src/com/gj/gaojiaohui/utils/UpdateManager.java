package com.gj.gaojiaohui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.GaojiaoMainActivity;
import com.smartdot.wenbo.huiyi.R;

/**
 * 下载更新
 * 
 * @author hjh
 */
public class UpdateManager {
	private String downloadUrl = "";
	// 下载进度条
	private ProgressBar progressBar;
	// 是否终止下载
	private boolean isInterceptDownload = false;
	// 进度条显示数值
	private int progress = 0;
	private Context context;
	private Activity act;
	AlertDialog d;// 显示下载app时的进度条对话框
	/** 操作下载app时的进度条对话框 */
	Handler h = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:// 显示下载app时的进度条对话框
				d = (AlertDialog) msg.obj;
				d.show();
				break;
			case 3:// 销毁下载app时的进度条对话框
				if (d != null)
					d.dismiss();
				break;
			}
		}

	};

	public UpdateManager(Context context, Activity act) {
		this.context = context;
		this.act = act;
	}

	/**
	 * 弹出下载框
	 */
	public void showDownloadDialog(String downloadUrl) {
		this.downloadUrl = downloadUrl;
		Builder builder = new Builder(context);
		builder.setTitle(context.getResources().getString(R.string.Version_update));
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.update_progress, null);
		progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
		builder.setView(v);
		builder.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface download_dialog, int which) {
				if (!GloableConfig.permission.equals("0") && !GloableConfig.permission.equals("1")) {
					download_dialog.dismiss();
					// 终止下载
					isInterceptDownload = true;
				} else {
					// 嘉宾权限点击取消更新就关闭界面
					((GaojiaoMainActivity) context).exitAllAct();
				}
			}
		});
		d = builder.create();
		d.setCanceledOnTouchOutside(false);
		d.setCancelable(false);// 设置返回键不响应
		// 监听返回键 不响应 但是有提示信息
		d.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// 不响应按键抬起时的动作
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
					CustomToast.showToast(context, "下载中...  点击home键进入后台下载！");
					return true;
				} else
					return false;
			}
		});
		Message msg = new Message();
		msg.what = 2;
		msg.obj = d;
		h.sendMessage(msg);
		// 下载apk
		downloadApk();
	}

	/**
	 * 下载apk
	 */
	private void downloadApk() {
		// 开启另一线程下载
		Thread downLoadThread = new Thread(downApkRunnable);
		downLoadThread.start();
	}

	/** 操作提示“当前设备无SD卡，数据无法下载”的对话框 */
	Handler h2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Builder b = (Builder) msg.obj;
			b.show();// 显示提示“当前设备无SD卡，数据无法下载”的对话框
		}
	};
	/**
	 * 从服务器下载新版apk的线程
	 */
	private Runnable downApkRunnable = new Runnable() {
		@Override
		public void run() {
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				// 如果没有SD卡
				Builder builder = new Builder(context);
				builder.setTitle("提示");
				builder.setMessage("当前设备无SD卡，数据无法下载");
				builder.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						h.sendEmptyMessage(3);
						dialog.dismiss();
					}
				});
				Message msg = new Message();
				msg.what = 1;
				msg.obj = builder;
				h2.sendMessage(msg);
				return;
			} else {
				try {
					// 服务器上新版apk地址
					// String
					// url="http://172.20.90.88:9091/f/926/687/51/2c94dad84dfb1f23014dfb3816020002.apk";
					URL content_url = new URL(downloadUrl);
					HttpURLConnection conn = (HttpURLConnection) content_url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/");
					if (!file.exists()) {
						// 如果文件夹不存在,则创建
						file.mkdir();
					}
					// 下载服务器中新版本软件（写文件）
					String apkFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "MinistryOfWater.apk";
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numRead = is.read(buf);
						count += numRead;
						// 更新进度条
						progress = (int) (((float) count / length) * 100);
						handler.sendEmptyMessage(1);
						if (numRead <= 0) {
							// 下载完成通知安装
							handler.sendEmptyMessage(0);
							break;
						}
						fos.write(buf, 0, numRead);
						// 当点击取消时，则停止下载
					} while (!isInterceptDownload);
					fos.close();// lyy
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	/**
	 * 声明一个handler来更新进度条
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 更新进度情况
				progressBar.setProgress(progress);
				break;
			case 0:
				progressBar.setVisibility(View.INVISIBLE);
				CustomToast.showToast(context, "下载完成，已保存到" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/MinistryOfWater.apk");
				// 安装apk文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 安装apk
	 */
	private void installApk() {
		// 获取当前sdcard存储路径
		File apkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "MinistryOfWater.apk");
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		// 安装，如果签名不一致，可能出现程序未安装提示
		i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())), "application/vnd.android.package-archive");
		context.startActivity(i);
		d.dismiss();
		/* act.finish(); */
	}

}
