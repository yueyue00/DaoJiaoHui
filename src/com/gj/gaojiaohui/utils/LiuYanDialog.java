package com.gj.gaojiaohui.utils;

import com.smartdot.wenbo.huiyi.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;


public class LiuYanDialog extends AlertDialog{

	public LiuYanDialog(Context context, int theme) {
	    super(context, theme);
	}

	public LiuYanDialog(Context context) {
	    super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.liuyan_dialog);
	}
}
