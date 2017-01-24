package com.gj.gaojiaohui.utils;

import com.smartdot.wenbo.huiyi.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class QingLiuYanDialog extends AlertDialog{

	public QingLiuYanDialog(Context context, int theme) {
	    super(context, theme);
	}

	public QingLiuYanDialog(Context context) {
	    super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.guanzhu_dialog);
	}
}
