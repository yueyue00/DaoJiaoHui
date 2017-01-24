package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.LoginActivity;
import com.gj.gaojiaohui.activity.PersonalDataActivity;
import com.gj.gaojiaohui.bean.ExhibitorsBeanForOffline;
import com.gj.gaojiaohui.bean.ExhibitorsListChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

public class ExbihitorListForOfflineAdapter extends CommonAdapter<ExhibitorsBeanForOffline> {
	private List<ExhibitorsBeanForOffline> items = new ArrayList<ExhibitorsBeanForOffline>();

	private DisplayImageOptions options;

	private Context mContext;

	public ExbihitorListForOfflineAdapter(Context context, List<ExhibitorsBeanForOffline> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, ExhibitorsBeanForOffline bean) {
		int position = viewHolder.getPosition();
		switch (GloableConfig.LANGUAGE_TYPE) {
		case "1":
			viewHolder.setText(R.id.three_title, bean.name);
			viewHolder.setText(R.id.three_neirong, context.getResources().getString(R.string.booth_number)+bean.position);
			break;

		case "2":
			viewHolder.setText(R.id.three_title, bean.name_en);
			viewHolder.setText(R.id.three_neirong, context.getResources().getString(R.string.booth_number)+bean.position);
			break;
		}
	}

}
