package com.gj.gaojiaohui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.smartdot.wenbo.huiyi.R;

/**
 * 从网络上下载文件的工具类
 * 
 * @author zhangt
 * 
 */
public class DownLoadUtil {

	private String TAG = "fate";
	/** 超时时间 */
	private static final int TIME_OUT = 10 * 1000;
	/** 编码方式 */
	private static final String CHARSET = "utf-8";
	/** Context */
	private Context mContext;
	/** 文件的下载地址 */
	private String downloadUrl;
	/** 文件名 */
	private String fileName;
	/** 下载进度条 */
	private ProgressBar mProgressBar;
	/** 是否终止下载 */
	private boolean isInterceptDownload;
	/** 进度条显示的数值 */
	private int propressNum;
	/** 下载时候的对话框 */
	private AlertDialog mAlertDialog;
	/** 文件的总长度 */
	private int fileLength;

	/**
	 * 构造方法
	 * 
	 * @param mContext
	 * @param url
	 */
	public DownLoadUtil(Context mContext, String downloadUrl) {
		this.mContext = mContext;
		this.downloadUrl = downloadUrl;
	}

	/**
	 * 显示下载框并启动下载
	 */
	public void showDownload() {
		Log.i(TAG, "文件的下载地址是--------" + downloadUrl);
		String[] fileMessage = downloadUrl.split("/");
		fileName = fileMessage[fileMessage.length - 1];

		Builder builder = new Builder(mContext);
		builder.setTitle(mContext.getResources().getString(R.string.download));
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View v = layoutInflater.inflate(R.layout.update_progress, null);
		mProgressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
		builder.setView(v);
		builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isInterceptDownload = true; // 终止下载
			}
		});
		mAlertDialog = builder.create();
		mAlertDialog.setCanceledOnTouchOutside(false);
		mAlertDialog.setCancelable(false); // 返回键不响应
		// 监听返回键，不响应但要有提示
		mAlertDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
					CustomToast.showToast(mContext, "下载中...  点击home键进入后台下载！");
					return true;
				}
				return false;
			}
		});
		mAlertDialog.show();
		startDownLoad();
	}

	private void startDownLoad() {
		// 开启另一个线程下载
		Thread downloadThread = new Thread(downloadRunnable);
		downloadThread.start();
	}

	private Runnable downloadRunnable = new Runnable() {

		@Override
		public void run() {
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				// 没有sd卡
				Toast.makeText(mContext, "当前设备没有储存卡，无法下载", Toast.LENGTH_SHORT).show();
				mAlertDialog.cancel();
			} else {
				try {
					URL contentUrl = new URL(downloadUrl);
					HttpURLConnection conn = (HttpURLConnection) contentUrl.openConnection();
					conn.setReadTimeout(TIME_OUT);
					conn.setConnectTimeout(TIME_OUT);
					conn.connect();
					fileLength = conn.getContentLength();
					InputStream is = conn.getInputStream();

					// File file = new
					// File(mContext.getApplicationContext().getFilesDir().getAbsolutePath()+
					// "/download");
					// if (!file.exists()) {
					// file.mkdir();
					// }
					// String fileString =
					// mContext.getApplicationContext().getFilesDir().getAbsolutePath()
					// + "/download/" + fileName;
					File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "files");
					if (!file.exists()) {
						file.mkdir();
					}
					String fileString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "files/" + fileName;

					Log.i(TAG, "文件的存储路径是--------" + fileString);
					File file1 = new File(fileString);
					FileOutputStream fos = new FileOutputStream(file1);
					int count = 0;
					byte buff[] = new byte[1024];
					do {
						int numRead = is.read(buff);
						count += numRead;
						// 更新进度条
						propressNum = (int) (((float) count / fileLength) * 100);
						handler.sendEmptyMessage(0);
						if (numRead <= 0) {
							// 下载完成
							if (fileName.contains(".pdf")) {
								handler.sendEmptyMessage(2);
							} else {
								handler.sendEmptyMessage(1);
							}

							break;
						}
						fos.write(buff, 0, numRead);
					} while (!isInterceptDownload);
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mProgressBar.setProgress(propressNum);
				break;
			case 1:
				mProgressBar.setVisibility(View.INVISIBLE);
				Log.i(TAG, "--------文件下载完成");
				mAlertDialog.dismiss();
				break;
			case 2:
				mProgressBar.setVisibility(View.INVISIBLE);
				Log.i(TAG, "--------文件下载完成");
				String fileString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "files/" + fileName;
				Intent intent = getPdfFileIntent(fileString);
				mContext.startActivity(intent);
				mAlertDialog.dismiss();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 分享文件
	 * 
	 * @param filePath
	 */
	public void shareFile(String filePath) {
		// 获取文件
		Uri uri = Uri.fromFile(new File(filePath));
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.setType("application/*");
		mContext.startActivity(Intent.createChooser(shareIntent, "分享到"));
	}

	/**
	 * 打开pdf文件
	 */
	public Intent getPdfFileIntent(String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}
}
