package com.gj.gaojiaohui.fragment;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.GaoJiaoHuiBaseActivity;
import com.gj.gaojiaohui.activity.GaojiaoMainActivity;
import com.gj.gaojiaohui.activity.LoginActivity;
import com.gj.gaojiaohui.activity.MyTicketsActivity;
import com.gj.gaojiaohui.activity.PersonalDataActivity;
import com.gj.gaojiaohui.activity.QrCodeActivity;
import com.gj.gaojiaohui.activity.QrCodeGiveActivity;
import com.gj.gaojiaohui.activity.SetActivity;
import com.gj.gaojiaohui.bean.MyLoginSuccessBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 个人页面
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class FragmentSetting extends Fragment implements OnClickListener {

	private View view;
	private Context mContext;
	private MyLoginSuccessBean data;
	DisplayImageOptions options;
	private ImageView user_qr_img;
	private TextView free_ticket;
	private String qrCodeString;
	private TextView login_name;
	private RelativeLayout please_login_rl;
	private RelativeLayout is_logined_rl;
	private RelativeLayout language_rl;
	private RelativeLayout qr_code_rl;
	private RelativeLayout set_rl;
	private RelativeLayout free_ticket_rl;
	private RelativeLayout offline_mode_rl;
	private RelativeLayout eticket_rl;
	private TextView click_person_message;
	// private ImageView personal_offline_img;
	private TextView offline_operate_tv;
	private TextView offline_mode_tv;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 第一页数据
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			}
		};
	};
	private RelativeLayout supplementary_rl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting, null);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		initView();
		getData();
		options = ImageLoaderUtils.initOptions();
	}

	private void initView() {

		please_login_rl = (RelativeLayout) view.findViewById(R.id.please_login_rl);
		is_logined_rl = (RelativeLayout) view.findViewById(R.id.is_logined_rl);
		language_rl = (RelativeLayout) view.findViewById(R.id.language_rl);
		qr_code_rl = (RelativeLayout) view.findViewById(R.id.qr_code_rl);
		eticket_rl = (RelativeLayout) view.findViewById(R.id.eticket_rl);
		set_rl = (RelativeLayout) view.findViewById(R.id.set_rl);
		free_ticket_rl = (RelativeLayout) view.findViewById(R.id.free_ticket_rl);
		login_name = (TextView) view.findViewById(R.id.login_names);
		click_person_message = (TextView) view.findViewById(R.id.click_person_messages);
		free_ticket = (TextView) view.findViewById(R.id.free_tickets);
		user_qr_img = (ImageView) view.findViewById(R.id.user_qrs);
		offline_mode_rl = (RelativeLayout) view.findViewById(R.id.offline_mode_rl);
		offline_operate_tv = (TextView) view.findViewById(R.id.offline_operate_tv);
		offline_mode_tv = (TextView) view.findViewById(R.id.offline_mode_tv);
		supplementary_rl = (RelativeLayout) view.findViewById(R.id.supplementary_rl);
		if (GloableConfig.IS_OFFLINE) {// 离线
			offline_mode_tv.setText(getResources().getString(R.string.offline_mode));
		} else {// 有线
			offline_mode_tv.setText(getResources().getString(R.string.onLine_mode));
		}

		please_login_rl.setOnClickListener(this);
		language_rl.setOnClickListener(this);
		qr_code_rl.setOnClickListener(this);
		set_rl.setOnClickListener(this);
		eticket_rl.setOnClickListener(this);
		free_ticket_rl.setOnClickListener(this);
		click_person_message.setOnClickListener(this);
		user_qr_img.setOnClickListener(this);
		offline_mode_rl.setOnClickListener(this);
		supplementary_rl.setOnClickListener(this);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		data = CommonUtil.gson.fromJson(json, MyLoginSuccessBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			setData();
		}
	}

	// 判断我的赠票
	private void setData() {
//		if (data.coupon.equals("")) {
//			free_ticket_rl.setVisibility(View.GONE);
//		} else {
//			free_ticket_rl.setVisibility(View.VISIBLE);
//		}

		if (GloableConfig.permission.equals("3")) {
			eticket_rl.setVisibility(View.VISIBLE);
		} else {
			eticket_rl.setVisibility(View.GONE);
		}
		qrCodeString = String.format(GloableConfig.PERSON_QRCODE, data.user_qr.name, data.user_qr.email, data.user_qr.tel, data.user_qr.ID,
				data.user_qr.company);
		Bitmap bitmap = CommonUtils.create2DCode(qrCodeString);
		user_qr_img.setImageBitmap(bitmap);
	}

	private void getData() {
		String url = String.format(GloableConfig.MY_LOGIN_SUCCESS, GloableConfig.userid, GloableConfig.LANGUAGE_TYPE);
		ProgressUtil.showPregressDialog(mContext);
		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, 0);
	}

	private void judgePermission() {
		// 观众身份
		if (GloableConfig.permission.equals("3")) {
			please_login_rl.setVisibility(View.GONE);
			is_logined_rl.setVisibility(View.VISIBLE);
			click_person_message.setVisibility(View.VISIBLE);
			login_name.setVisibility(View.GONE);
			supplementary_rl.setVisibility(View.GONE);
			// 判断是否补充完整个人资料
			if (GloableConfig.username.equals("")) {
				click_person_message.setVisibility(View.GONE);
				supplementary_rl.setVisibility(View.VISIBLE);
			}
			// 游客身份
		} else if (GloableConfig.userid.equals("")) {
			please_login_rl.setVisibility(View.VISIBLE);
			is_logined_rl.setVisibility(View.GONE);
			please_login_rl.setOnClickListener(this);
			// 其他身份
		} else {
			please_login_rl.setVisibility(View.GONE);
			is_logined_rl.setVisibility(View.VISIBLE);
			please_login_rl.setOnClickListener(null);
			login_name.setVisibility(View.VISIBLE);
			login_name.setText(GloableConfig.username);
			click_person_message.setVisibility(View.GONE);
			supplementary_rl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		judgePermission();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 语言
		case R.id.language_rl:
			String languageString = (String) SharePreferenceUtils.getParam("language", "zh");
			GaoJiaoHuiBaseActivity activity = (GaoJiaoHuiBaseActivity) getActivity();
			if (languageString.equals("zh")) {
				activity.switchLanguage("en");
				GloableConfig.LANGUAGE_TYPE = "2";
			} else if (languageString.equals("en")) {
				activity.switchLanguage("zh");
				GloableConfig.LANGUAGE_TYPE = "1";
			}
			// 更新语言后，destroy当前页面，重新绘制
			getActivity().finish();
			Intent it = new Intent(getActivity(), GaojiaoMainActivity.class);
			startActivity(it);
			break;
		// 二维码
		case R.id.qr_code_rl:
			Intent intent_qr = new Intent(getActivity(), QrCodeActivity.class);
			intent_qr.putExtra("qrCodeType", "2");
			startActivity(intent_qr);
			break;
		// 设置
		case R.id.set_rl:
			getActivity().startActivity(new Intent(getActivity(), SetActivity.class));
			break;
		// 登录
		case R.id.please_login_rl:
			getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		// 游客补充个人资料
		case R.id.supplementary_rl:
			getActivity().startActivity(new Intent(getActivity(), PersonalDataActivity.class));
			break;
		// 观众修改个人资料
		case R.id.click_person_messages:
			getActivity().startActivity(new Intent(getActivity(), PersonalDataActivity.class));
			break;
		// 我的赠票
		case R.id.free_ticket_rl:
			Intent intent_free_person = new Intent(getActivity(), QrCodeGiveActivity.class);
			if(data != null){
				intent_free_person.putExtra("coupon", data.coupon);
				intent_free_person.putExtra("qrCodeType", "3");
				startActivity(intent_free_person);
			}
			break;
		// 绑定电子票
		case R.id.eticket_rl:
			Intent intent = new Intent(getActivity(), MyTicketsActivity.class);
			startActivity(intent);
			break;
		// 用户二维码
		case R.id.user_qrs:
			Intent intent_pserson = new Intent(getActivity(), QrCodeActivity.class);
			intent_pserson.putExtra("qrCodeString", qrCodeString);
			intent_pserson.putExtra("qrCodeType", "1");
			startActivity(intent_pserson);
			break;
		// 离线模式
		case R.id.offline_mode_rl:
			changeNetWork();
			break;
		}
	}

	/** 修改网络状态 加载离线/在线数据 */
	public void changeNetWork() {
		if (GloableConfig.IS_OFFLINE) {// 离线
			GloableConfig.IS_OFFLINE = false;
			// offline_operate_tv.setText(getResources().getString(R.string.start_offline_mode));
			refreshUI();
		} else {// 在线
			GloableConfig.IS_OFFLINE = true;
			// offline_operate_tv.setText(getResources().getString(R.string.close_offline_mode));
			refreshUI();
		}
	}

	/** 重新启动界面 */
	private void refreshUI() {
		getActivity().finish();
		Intent intent = new Intent(getActivity(), GaojiaoMainActivity.class);
		startActivity(intent);
	}
}
