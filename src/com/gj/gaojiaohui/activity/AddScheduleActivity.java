package com.gj.gaojiaohui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.ScreenInfo;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.MyAlertDialog;
import com.gj.gaojiaohui.view.WheelMain;
import com.smartdot.wenbo.huiyi.R;

/**
 * 添加日程
 * 
 * @author zhangt
 * 
 */
public class AddScheduleActivity extends Activity implements OnClickListener {

	private Context mContext;
	private ImageView backView;
	private TextView titleTextView;
	private TextView saveTextView;
	private LinearLayout schedule_bg;
	private EditText addschedule_title_et;
	private EditText addschedule_place_et;
	private TextView add_date_tv;
	private TextView add_starttime_tv;
	private TextView add_endtime_tv;
	private EditText addschedule_Introduction_et;
	private String titleString, placeString, dateString, startString, endString, introductionString;
	private Boolean hasDate = false; // 是否选择了日期
	/** 时间选择器 */
	private WheelMain wheelMain;
	SimpleDateFormat sdf;

	String tmpCurrentDate;
	String tmpCurrentTime;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				ProgressUtil.dismissProgressDialog();
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						CustomToast.showToast(mContext, getString(R.string.add_schedule_success));
						// 返回成功结果给日程界面
						Intent it = new Intent();
						it.putExtra("addSuccess", 1);
						setResult(0, it);
						finish();
					} else {
						CustomToast.showToast(mContext, getResources().getString(R.string.network));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_schedule);
		mContext = this;
		initView();
	}

	private void initView() {
		backView = (ImageView) findViewById(R.id.custom_back_img);
		titleTextView = (TextView) findViewById(R.id.custom_title_tv);
		saveTextView = (TextView) findViewById(R.id.person_save);
		schedule_bg = (LinearLayout) findViewById(R.id.schedule_bg);

		addschedule_title_et = (EditText) findViewById(R.id.addschedule_title_et);
		addschedule_place_et = (EditText) findViewById(R.id.addschedule_place_et);
		add_date_tv = (TextView) findViewById(R.id.add_data_tv);
		add_starttime_tv = (TextView) findViewById(R.id.add_starttime_tv);
		add_endtime_tv = (TextView) findViewById(R.id.add_endtime_tv);
		addschedule_Introduction_et = (EditText) findViewById(R.id.addschedule_Introduction_et);

		CommonUtils.EmojiFilter(addschedule_title_et, 20);
		CommonUtils.EmojiFilter(addschedule_place_et, 20);
		CommonUtils.EmojiFilter(addschedule_Introduction_et, 200);

		titleTextView.setText(getResources().getString(R.string.add_schedule));
		saveTextView.setText(getResources().getString(R.string.submit));
		saveTextView.setVisibility(View.VISIBLE);

		backView.setOnClickListener(this);
		schedule_bg.setOnClickListener(this);
		add_date_tv.setOnClickListener(this);
		add_starttime_tv.setOnClickListener(this);
		add_endtime_tv.setOnClickListener(this);
		saveTextView.setOnClickListener(this);

		schedule_bg.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		});
	}

	/**
	 * 显示时间选择器
	 */
	@SuppressLint("SimpleDateFormat")
	private void showTimeSelector(final TextView textView, final Boolean isDate) {
		LayoutInflater inflater1 = LayoutInflater.from(mContext);
		final View timepickerview1 = inflater1.inflate(R.layout.timepicker, null);
		TextView curDate = (TextView) timepickerview1.findViewById(R.id.timePickerTextTv);

		ScreenInfo screenInfo1 = new ScreenInfo(AddScheduleActivity.this);
		if (isDate) {
			wheelMain = new WheelMain(mContext, timepickerview1, false, true);
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			wheelMain = new WheelMain(mContext, timepickerview1, true, false);
			sdf = new SimpleDateFormat("HH:mm");
		}
		wheelMain.screenheight = screenInfo1.getHeight();
		Calendar calendar1 = Calendar.getInstance();

		int year1 = calendar1.get(Calendar.YEAR);
		int month1 = calendar1.get(Calendar.MONTH);
		int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
		int hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int minutes1 = calendar1.get(Calendar.MINUTE);

		final String dateStr = sdf.format(calendar1.getTime());
		curDate.setText(dateStr);
		wheelMain.initDateTimePicker(year1, month1, day1, hour1, minutes1);

		final MyAlertDialog dialog = new MyAlertDialog(mContext).builder().setTitle(getString(R.string.choose_date)).setView(timepickerview1)
				.setNegativeButton(getString(R.string.cancel), new OnClickListener() {
					@Override
					public void onClick(View v) {
						MyAlertDialog.dismiss();
					}
				});
		dialog.setPositiveButton(getString(R.string.save_contacts), new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mTimeString = "";
				Date date;
				try {
					date = sdf.parse(wheelMain.getTime());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					mTimeString = sdf.format(calendar.getTime());

				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (isDate) {
					tmpCurrentDate = dateStr;
				} else {
					tmpCurrentTime = dateStr;
				}
				if (mTimeString.compareTo(dateStr) < 0 && isDate) {
					CustomToast.showToast(mContext, getString(R.string.initact_check_time));
					return;
				} else {
					textView.setText(mTimeString);
					MyAlertDialog.dismiss();
				}

			}
		});
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.add_data_tv:
			showTimeSelector(add_date_tv, true);
			break;
		case R.id.add_starttime_tv:
			showTimeSelector(add_starttime_tv, false);
			break;
		case R.id.add_endtime_tv:
			showTimeSelector(add_endtime_tv, false);
			break;
		case R.id.person_save:
			saveSchedule();
			break;
		default:
			break;
		}

	}

	/**
	 * 提交数据
	 */
	private void saveSchedule() {
		titleString = addschedule_title_et.getText().toString().trim();
		placeString = addschedule_place_et.getText().toString().trim();
		dateString = add_date_tv.getText().toString();
		startString = add_starttime_tv.getText().toString();
		endString = add_endtime_tv.getText().toString();
		introductionString = addschedule_Introduction_et.getText().toString();

		if (StringUtils.isNull(titleString) || StringUtils.isNull(placeString) || StringUtils.isNull(dateString) || StringUtils.isNull(startString)
				|| StringUtils.isNull(endString)) {
			CustomToast.showToast(mContext, getString(R.string.complete_information));
			return;
		}

		Boolean isToday = false;
		if (dateString.compareTo(tmpCurrentDate) == 0) {
			isToday = true;
		} else {
			isToday = false;
		}

		if (isToday) {
			// TODO 是今天的日程
			if (startString.compareTo(tmpCurrentTime) < 0) {
				CustomToast.showToast(mContext, getString(R.string.initact_check_time));
				return;
			}
		}

		if (endString.compareTo(startString) <= 0) {
			CustomToast.showToast(mContext, getString(R.string.check_endtime));
			return;
		}

		try {
			titleString = URLEncoder.encode(titleString, "UTF-8");
			placeString = URLEncoder.encode(placeString, "UTF-8");
			dateString = URLEncoder.encode(dateString, "UTF-8");
			startString = URLEncoder.encode(startString, "UTF-8");
			endString = URLEncoder.encode(endString, "UTF-8");
			introductionString = URLEncoder.encode(introductionString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();
		String url = String.format(GloableConfig.ADD_SCHEDULE_URL, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE, titleString, placeString, dateString,
				startString, endString, introductionString);
		volleyUtil.stringRequest(handler, url, map, 1001);
	}
}
