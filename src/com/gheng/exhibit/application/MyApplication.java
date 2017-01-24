package com.gheng.exhibit.application;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.fengmap.android.FMMapSDK;
import com.gj.gaojiaohui.abconstant.RongCloudEvent;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.view.LocalImageHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 自定义Application，在里面进行Activity的管理和对全局异常的处理
 * 
 * @author Administrator
 * 
 */
public class MyApplication extends Application {

	private static MyApplication application = null;
	private Display display;

	static {
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		JPushInterface.setDebugMode(true); // 正式发版时此处应设置为false
		JPushInterface.init(this); // 初始化极光推送
		final File imgCacheDir = new File(Environment.getExternalStorageDirectory() + "/hebg3Tools/cache", "images");

		// ImageLoader初始化
		ImageLoaderUtils.initConfiguration(getApplicationContext());
		// 本地图片辅助类初始化
		LocalImageHelper.init(this);
		if (display == null) {
			WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			display = windowManager.getDefaultDisplay();
		}

		SDKInitializer.initialize(this);
		if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);

			if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
				RongCloudEvent.init(this);
				DemoContext.init(this);
			}
		}
		JPushInterface.init(getApplicationContext()); // 初始化 JPush
		FMMapSDK.init(this); // 初始化室内导航SDK
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY);
		config.denyCacheImageMultipleSizesInMemory();
		config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		// 修改连接超时时间5秒，下载超时时间5秒
		config.imageDownloader(new BaseImageDownloader(application, 5 * 1000, 5 * 1000));
		// config.writeDebugLogs(); // Remove for release app
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	public static MyApplication getInstance() {
		return application;
	}

	/** 用于销毁所有被创建的acitivty */
	public static List<Activity> activityList = new ArrayList<Activity>();

	/** 销毁list中存储的对应activity */
	public static void remove(Activity activity) {
		activityList.remove(activity);
	}

	/** 向list中添加activity对象 */
	public static void add(Activity activity) {
		activityList.add(activity);
	}

	/** 销毁所有被启用的activity */
	public static void finishProgram() {
		int n = activityList.size() - 1;
		for (int i = n; i > -1; i--) {
			activityList.get(i).finish();
		}
	}

	/**
	 * 获得当前进程的名字
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {

				return appProcess.processName;
			}
		}
		return null;
	}

	private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

		/**
		 * 收到消息的处理。
		 * 
		 * @param message
		 *            收到的消息实体。
		 * @param left
		 *            剩余未拉取消息数目。
		 * @return
		 */
		@Override
		public boolean onReceived(Message message, int left) {
			// 开发者根据自己需求自行处理
			return false;
		}
	}

	public String getCachePath() {
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = getExternalCacheDir();
		else
			cacheDir = getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir.getAbsolutePath();
	}

	/**
	 * @return
	 * @Description： 获取当前屏幕1/4宽度
	 */
	public int getQuarterWidth() {
		return display.getWidth() / 4;
	}
}
