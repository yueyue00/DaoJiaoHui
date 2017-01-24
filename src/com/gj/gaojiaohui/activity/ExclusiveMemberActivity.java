package com.gj.gaojiaohui.activity;

import io.rong.imlib.model.Conversation.ConversationType;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.a.a.b;

import com.gheng.exhibit.http.body.response.InitData;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.ExcluSiveMemberBean;
import com.gj.gaojiaohui.bean.HotelInformationBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.RongUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * 专属服务人员--嘉宾服务
 * 
 * @author lixiaoming
 * 
 */
public class ExclusiveMemberActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {
	private Context mContext;
	private ImageView back_img;
	private TextView title_tv;
	private ImageView icon_img;
	private TextView name_tv;
	private TextView mobile_tv;
	private TextView position_tv;
	private Button call_btn;
	private Button sms_btn;
	private Button sendInfo_btn;
	private DisplayImageOptions options;
	private ExcluSiveMemberBean bean;
	private ExcluSiveMemberBean data;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				// 不管数据请求成功与否都要把进度加载条关闭
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exclusive_member);
		mContext = this;
		initView();
		InitData();
	}

	/** 初始化数据 */
	private void InitData() {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		String url = String.format(GloableConfig.EXCLUSIVE_MEMBER_URL, GloableConfig.userid);
		volleyUtil1.stringRequest(handler, url, map, 1001);// 1001是msg.what
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		back_img = (ImageView) findViewById(R.id.custom_back_img);
		back_img.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.custom_title_tv);
		title_tv.setText("");
		icon_img = (ImageView) findViewById(R.id.excluisive_icon_img);
		name_tv = (TextView) findViewById(R.id.exclusive_name_tv);
		mobile_tv = (TextView) findViewById(R.id.exclusive_mobile_tv);
		position_tv = (TextView) findViewById(R.id.exclusive_position_tv);
		call_btn = (Button) findViewById(R.id.excluisive_call_btn);
		call_btn.setOnClickListener(this);
		sms_btn = (Button) findViewById(R.id.excluisive_sms_btn);
		sms_btn.setOnClickListener(this);
		sendInfo_btn = (Button) findViewById(R.id.excluisive_sendinfo_btn);
		sendInfo_btn.setOnClickListener(this);
	}

	/**
	 * 使用ImageLoader加载图片
	 * 
	 * @param imageView
	 *            控件
	 * @param imgUrl
	 *            图片url
	 */
	public void getPic(ImageView imageView, String imgUrl) {
		options = ImageLoaderUtils.initOptionsForCirclePic(R.drawable.pic_vip, 40);
		ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		data = CommonUtil.gson.fromJson(json, ExcluSiveMemberBean.class);// 这段崩溃有3种原因
																			// 1/字段名缺失,2/格式类型不对,3/字段null了
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			getPic(icon_img, data.pic);// 设置头像
			name_tv.setText(data.name);
			mobile_tv.setText(data.tel);
			position_tv.setText(data.position);
		} else {
			/** 请求失败的处理 */
			CustomToast.showToast(mContext, getResources().getString(R.string.getdata_error));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;
		case R.id.excluisive_call_btn:// 电话号码要做为空判断
			CommonUtils.dialPhoneNumber(mContext, data.tel);
			break;
		case R.id.excluisive_sms_btn:// 电话号码要做为空判断
			CommonUtils.sendSms(mContext, data.tel);
			break;
		case R.id.excluisive_sendinfo_btn:
			RongUtil.startChat(mContext, ConversationType.PRIVATE, data.id, data.name);
			break;
		}
	}
}
