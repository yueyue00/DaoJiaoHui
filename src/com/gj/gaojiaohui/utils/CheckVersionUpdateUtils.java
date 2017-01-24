package com.gj.gaojiaohui.utils;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.smartdot.wenbo.huiyi.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/** 检测版本更新的工具类 */
public class CheckVersionUpdateUtils {
	public static CheckVersionUpdateUtils updateUtils = null;

	static Activity context;

	private CheckVersionUpdateUtils() {
	}

	public static CheckVersionUpdateUtils createCheckVersionUpdateUtils(Activity tcontext) {
		context = tcontext;
		if (updateUtils == null)
			updateUtils = new CheckVersionUpdateUtils();
		return updateUtils;
	}

	/** 获取最新版本信息进行比较并下载 */
	public void getVersionInfoCompare() {
		String versionName = getCurrentAppVersion(context);
		int a = compareToVersion(GloableConfig.VERSION, versionName);
		if (a == 1) {
			downloadLastedApp(GloableConfig.DOWNLOAD_APK);
		} else {
			CustomToast.showToast(context, context.getResources().getString(R.string.latest_version));
		}
	}

	/** 获取最新版本信息进行比较并下载 没有toast提示 */
	public void getVersionInfoCompareNoToast() {
		String versionName = getCurrentAppVersion(context);
		int a = compareToVersion(GloableConfig.VERSION, versionName);
		if (a == 1) {
			if (GloableConfig.permission.equals("0") || GloableConfig.permission.equals("1")) {
				// 是嘉宾权限就弹强制更新
				downloadLastedAppForce(GloableConfig.DOWNLOAD_APK);
			} else {
				downloadLastedApp(GloableConfig.DOWNLOAD_APK);
			}
		}
	};

	/** 下载新的版本 */
	public void downloadLastedApp(final String downloadUrl) {
		new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.updata_remind))
				.setMessage(context.getResources().getString(R.string.haveNewVersion))
				.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						UpdateManager manager = new UpdateManager(context, context);
						manager.showDownloadDialog(downloadUrl);
						dialog.dismiss();
					}
				}).setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/** 强制下载新的版本 不可关闭 */
	public void downloadLastedAppForce(final String downloadUrl) {
		new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.updata_remind))
				.setMessage(context.getResources().getString(R.string.haveNewVersion)).setCancelable(false)// 设置是否可关闭
				.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						UpdateManager manager = new UpdateManager(context, context);
						manager.showDownloadDialog(downloadUrl);
						dialog.dismiss();
					}
				}).show();
	}

	/** 获取系统当前的版本号 */
	public String getCurrentAppVersion(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info.versionName;
	}

	/***
	 * 版本号比较
	 * 
	 * @param serverVersion
	 *            是从服务器返回回来的版本号
	 * @param localVersion
	 *            是从本地Me获取的版本号
	 * @return 1表示大于；0表示相等；-1表示小于。
	 */
	public int compareToVersion(String serverVersion, String localVersion) {
		try {
			float server = Float.parseFloat(serverVersion.trim());
			float local = Float.parseFloat(localVersion.trim());
			if (server > local)
				return 1;
			if (server < local)
				return -1;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
