/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gj.gaojiaohui.qrcode.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.AudienceDetailBySaoMaActivity;
import com.gj.gaojiaohui.activity.DaHuiNaviActivity;
import com.gj.gaojiaohui.activity.MyTracksActivity;
import com.gj.gaojiaohui.activity.ZhanShangParticularsActivity;
import com.gj.gaojiaohui.bean.ExhibitorsBeanForOffline;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;
import com.gj.gaojiaohui.db.DBHelper;
import com.gj.gaojiaohui.db.SQLdm;
import com.gj.gaojiaohui.qrcode.camera.CameraManager;
import com.gj.gaojiaohui.qrcode.decode.DecodeThread;
import com.gj.gaojiaohui.qrcode.decode.RGBLuminanceSource;
import com.gj.gaojiaohui.qrcode.utils.BeepManager;
import com.gj.gaojiaohui.qrcode.utils.CaptureActivityHandler;
import com.gj.gaojiaohui.qrcode.utils.InactivityTimer;
import com.gj.gaojiaohui.qrcode.utils.ZxingPhotoUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.smartdot.wenbo.huiyi.R;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

	private Context mContext;

	private Uri selectUri;

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;

	private CaptureActivityHandler handler;

	private InactivityTimer inactivityTimer;

	private BeepManager beepManager;

	private SurfaceView scanPreview = null;

	private RelativeLayout scanContainer;

	private RelativeLayout scanCropView;

	private ImageView scanLine;

	/** 从相册中选取图片 */
	private ImageView scanPicImg;

	/** 闪光灯控制 */
	private ImageView LightImg;

	/** 闪光灯是否开启 */
	private Boolean isLightOn = false;

	/** 显示扫描结果的布局 */
	private RelativeLayout scanResult_rl;

	/** 显示扫描结果 */
	private TextView scanResult_tv;

	/** 取消显示结果 */
	private Button scanResultCancel_Btn;

	private ZxingPhotoUtils photoUtils;

	private String photo_path;

	private Bitmap scanBitmap;

	private Rect mCropRect = null;

	private boolean isHasSurface = false;

	private String exhibitorId;

	/** 展位号 */
	private String boothNumber;

	private Boolean isToMap = false;

	public static CaptureActivity instance = null;

	private DBHelper dbHelper;

	private SQLiteDatabase db;

	private SQLdm exhibitorlistDb;

	private List<ExhibitorsBeanForOffline> list;

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1001) {
				scanCropView.setVisibility(View.GONE);
				scanResult_tv.setText(msg.obj.toString());
				scanResult_rl.setVisibility(View.VISIBLE);

			} else if (msg.what == 1002) {
				ProgressUtil.dismissProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						Intent intent = new Intent(mContext, ZhanShangParticularsActivity.class);
						exhibitorId = jsonObject.getString("id");
						intent.putExtra("exhibitorId", exhibitorId);
						startActivity(intent);
						finish();

					} else {
						CustomToast.showToast(mContext, getString(R.string.getdata_error));
						finish();
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getString(R.string.getdata_error));
					finish();
					e.printStackTrace();
				}
			} else if (msg.what == 1003) {

				ProgressUtil.dismissProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						Intent intent = new Intent(mContext, DaHuiNaviActivity.class);
						boothNumber = jsonObject.getString("id");
						DaHuiNaviActivity.instance.finish();
						intent.putExtra("mapType", "indoor");
						intent.putExtra("endfid", "");
						intent.putExtra("startid", boothNumber);
						startActivity(intent);
						finish();
					} else {
						CustomToast.showToast(mContext, getString(R.string.getdata_error));
						finish();
					}
				} catch (JSONException e) {
					CustomToast.showToast(mContext, getString(R.string.getdata_error));
					finish();
					e.printStackTrace();
				}

			}
		}
	};

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		instance = this;
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);
		Intent intent = getIntent();
		isToMap = intent.getBooleanExtra("isToMap", false);
		initView();
		setPortraitChangeListener();
		exhibitorlistDb = new SQLdm("exhibitorlist.db");// 展商列表是exhibitorlist.db
		db = exhibitorlistDb.openDatabase(mContext);
	}

	private void initView() {
		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		scanPicImg = (ImageView) findViewById(R.id.scanPicImg);
		scanResult_rl = (RelativeLayout) findViewById(R.id.scanResult_rl);
		scanResult_tv = (TextView) findViewById(R.id.scanResult_tv);
		scanResultCancel_Btn = (Button) findViewById(R.id.scanResult_Btn);
		LightImg = (ImageView) findViewById(R.id.LightImg);

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);

		scanPicImg.setOnClickListener(new View.OnClickListener() {
			// TODO: 2016/8/18 扫描图片中的二维码
			@Override
			public void onClick(View v) {
				// 选取相册中的图片
				photoUtils.selectPicture(CaptureActivity.this);
			}
		});

		LightImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLightOn) {
					cameraManager.closeLight();
					isLightOn = false;
				} else {
					cameraManager.openLight();
					isLightOn = true;
				}
			}
		});
		scanResultCancel_Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scanPreview.getHolder().addCallback(CaptureActivity.this);
				scanCropView.setVisibility(View.VISIBLE);
				scanResult_rl.setVisibility(View.GONE);
				handler = null;
				if (isHasSurface) {
					initCamera(scanPreview.getHolder());
				} else {
					scanPreview.getHolder().addCallback(CaptureActivity.this);
				}
			}
		});
	}

	private void setPortraitChangeListener() {
		photoUtils = new ZxingPhotoUtils(mContext, new ZxingPhotoUtils.OnPhotoResultListener() {
			@Override
			public void onPhotoResult(Uri uri) {
				if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
					selectUri = uri;
					if (TextUtils.isEmpty(selectUri.toString())) {
						System.out.println("图片为空");
					}
				}
			}

			@Override
			public void onPhotoCancel() {
			}
		});
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 * 有效的条形码被发现,所以提示成功并显示结果。
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtra("qr_result", rawResult.getText());
		String result = rawResult.getText();
		if (result.contains("BEGIN:VCARD")) {
			int lastIndex = result.lastIndexOf("\n");
			String before = result.substring(0, lastIndex);
			result = "{\"" + before.replace("\n", "\",\"").replace("\t", "").replace("\r", "").replace(":", "\":\"") + "\"}";
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.has("NOTE")) {
					String ExhibitCode = jsonObject.getString("NOTE");
					String ORG = jsonObject.getString("ORG");
					String tmpORG = ORG;
					try {
						ORG = URLEncoder.encode(ORG, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ExhibitCode = ExhibitCode.substring(3, ExhibitCode.length());
					exhibitorId = ExhibitCode;
					if (!isToMap) {
						// 请求“提交我的足迹”接口数据
						ProgressUtil.showPregressDialog(mContext);
						VolleyUtil volleyUtil = new VolleyUtil(mContext);
						Map<String, String> map = new HashMap<>();
						String url = String.format(GloableConfig.COMMINT_MY_TRACKS_URL, GloableConfig.userid, ExhibitCode, ORG);
						volleyUtil.stringRequest(mHandler, url, map, 1002);
					} else {
						// // 请求“展商id和展位号”接口数据
						// ProgressUtil.showPregressDialog(mContext);
						// VolleyUtil volleyUtil = new VolleyUtil(mContext);
						// Map<String, String> map = new HashMap<>();
						// String url =
						// String.format(GloableConfig.GET_EXHIBITID_BY_QRCODE_URL,
						// GloableConfig.userid, ExhibitCode, ORG);
						// volleyUtil.stringRequest(mHandler, url, map, 1003);
						dbHelper = new DBHelper();
						list = new ArrayList<>();
						try {
							list.addAll(dbHelper.fuzzySearchForExhibitors(db, "name", tmpORG));
							if (list.size() > 0) {
								Intent intent1 = new Intent(mContext, DaHuiNaviActivity.class);
								if (DaHuiNaviActivity.instance != null) {
									DaHuiNaviActivity.instance.finish();
								}
								intent1.putExtra("mapType", "indoor");
								intent1.putExtra("endfid", "");
								intent1.putExtra("startid", list.get(0).position);
								startActivity(intent1);
								finish();
							} else {
								CustomToast.showToast(mContext, "无法定位到此展位");
								finish();
							}
						} catch (SQLiteException e) {
							CustomToast.showToast(mContext, "无法定位到此展位");
							finish();
						}

					}

				} else {
					Intent intent2 = new Intent(mContext, AudienceDetailBySaoMaActivity.class);
					intent2.putExtra("ScanValue", result);
					startActivity(intent2);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// 非vcard
			setResult(RESULT_OK, intent);
			finish();
		}

	}

	/**
	 * 初始化照相机
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
			}
			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("Camera error");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 比可视区域四周各大100 */
		mCropRect = new Rect(x - 100, y - 100, width + x + 100, height + y + 100);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取返回的图片二维码信息
	 * 
	 * @param uri
	 */
	private void getInfo(Uri uri) {
		photo_path = ZxingPhotoUtils.getPath(getApplicationContext(), uri);
		Log.i("fate", photo_path);

		new Thread(new Runnable() {

			@Override
			public void run() {

				Result result = scanningImage(photo_path);
				if (result == null) {
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "无法识别", Toast.LENGTH_SHORT).show();
					Looper.loop();
				} else {
					Log.i("fate", result.toString());
					// 数据返回
					String recode = recode(result.toString());
					if (recode.contains("请打开移动门户来扫描本二维码")) {
						// 如果扫描出来是群名片，就直接关闭页面，交由移动门户处理
						Intent resultIntent = new Intent();
						Bundle bundle = null;
						bundle.putInt("width", mCropRect.width());
						bundle.putInt("height", mCropRect.height());
						bundle.putString("result", recode);
						resultIntent.putExtras(bundle);
						CaptureActivity.this.setResult(RESULT_OK, resultIntent);
						CaptureActivity.this.finish();
					} else {
						Message msg = new Message();
						msg.obj = result;
						msg.what = 1001;
						mHandler.sendMessage(msg);
					}

				}
			}
		}).start();
	}

	/**
	 * 扫描图片二维码
	 * 
	 * @param path
	 * @return
	 */
	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (com.google.zxing.NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (com.google.zxing.FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private String recode(String str) {
		String formart = "";
		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
			if (ISO) {
				formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
				Log.i("fate-ISO8859-1", formart);
			} else {
				formart = str;
				Log.i("fate-stringExtra", str);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formart;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ZxingPhotoUtils.INTENT_SELECT:
				getInfo(data.getData());
				break;
			default:
				break;
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

}