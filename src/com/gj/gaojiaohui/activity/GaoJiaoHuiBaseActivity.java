package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.gj.gaojiaohui.manager.AppManager;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.smartdot.wenbo.huiyi.R;

public class GaoJiaoHuiBaseActivity extends FragmentActivity {

	public static List<Activity> actList = null;
	private long exitTime = 0;
	// 防止重复点击设置的标志，涉及到点击打开其他Activity时，将该标志设置为false，在onResume事件中设置为true
	private boolean clickable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gao_jiao_hui_base);

		if (actList == null)
			actList = new ArrayList<Activity>();
		actList.add(this);
		AppManager.getAppManager().addActivity(this);
		//初始化SharePreference
		SharePreferenceUtils.getAppConfig(this);
		//根据上次的语言设置，重新设置语言
	    switchLanguage((String) SharePreferenceUtils.getParam("language", "zh"));
	}

	/** 退出所有的activity */
	public static void exitAllAct() {
		for (Activity act : actList)
			act.finish();
	}

	@Override
	protected void onDestroy() {
		actList.remove(this);
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();
	}

	/**
	 * 当前是否可以点击
	 * 
	 * @return
	 */
	protected boolean isClickable() {
		return clickable;
	}

	/**
	 * 锁定点击
	 */
	protected void lockClick() {
		clickable = false;
	}

	/** 触摸空白区域关闭软键盘 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {

				String strClassName = this.getClass().getName();
				if (strClassName.equals("com.gj.gaojiaohui.activity.GaojiaoMainActivity")) {
					CustomToast.showToast(this, "再按一次退出程序");
					exitTime = System.currentTimeMillis();
				} else {
					return super.onKeyDown(keyCode, event);
				}

			} else {
				exitAllAct();
//				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void clickleft(View v) {
		finish();
	}

	public void clickright(View v) {
		CustomToast.showToast(this, "设置");
	}
	public void switchLanguage(String language) {
		//设置应用语言类型
		Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
       if (language.equals("en")) {
            config.locale = Locale.ENGLISH;

        } else {
        	 config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);
        
        //保存设置语言的类型
        SharePreferenceUtils.setParam("language", language);
    }
}
