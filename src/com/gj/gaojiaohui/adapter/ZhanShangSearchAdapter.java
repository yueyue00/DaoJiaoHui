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
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.activity.LoginActivity;
import com.gj.gaojiaohui.activity.PersonalDataActivity;
import com.gj.gaojiaohui.bean.ExhibitorsListChildBean;
import com.gj.gaojiaohui.bean.ExhibitorsSearchChildBean;
import com.gj.gaojiaohui.bean.FollowMyBean;
import com.gj.gaojiaohui.bean.MeetingChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;

import com.gj.gaojiaohui.port.OnChangeFollowListener;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 搜索展商
 */
public class ZhanShangSearchAdapter extends CommonAdapter<ExhibitorsSearchChildBean> {
	private List<ExhibitorsSearchChildBean> items = new ArrayList<ExhibitorsSearchChildBean>();

	private DisplayImageOptions options;

	private Context mContext;

	/** 请求回的数据进行解析 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				// 关注
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 1002:
				// 取消关注
				// ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			}
		}
	};

	public ZhanShangSearchAdapter(Context context, List<ExhibitorsSearchChildBean> list, int itemLayoutResId) {
		super(context, list, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, ExhibitorsSearchChildBean bean) {
		final int position = viewHolder.getPosition();
		viewHolder.setText(R.id.three_title, bean.title);
		viewHolder.setText(R.id.three_neirong, bean.value);
		// 多选框监听
		checkListener(viewHolder, position);
	}

	private void checkListener(ViewHolder viewHolder, final int position) {
		CheckBox checkbox = (CheckBox) viewHolder.getView(R.id.three_guanzhu);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {// 点击关注
					/** 关注 防止重复操作 如果已经关注了 不继续执行 */
					if (!((ExhibitorsSearchChildBean) getItem(position)).follow) {
						if (!TextUtils.isEmpty(GloableConfig.userid)) {
							if (!TextUtils.isEmpty(GloableConfig.username)) {
								// userid不为空证明已经登录
								// VolleyUtil volleyUtil = new
								// VolleyUtil(mContext);
								// volleyUtil.onStop();
								String url = String.format(GloableConfig.FOLLOW_ZHANSHANG_URL, GloableConfig.userid,
										((ExhibitorsSearchChildBean) getItem(position)).id);
								loadData(url, "展商搜索-关注 的url:", 1001);
							} else {
								CustomToast.showToast(mContext, mContext.getResources().getString(R.string.finish_you_personal_info));
								// username为空跳补充个人信息界面
								Intent intent = new Intent(context, PersonalDataActivity.class);
								context.startActivity(intent);
							}
						} else {
							CustomToast.showToast(mContext, mContext.getResources().getString(R.string.not_login));
							// 没有登录直接跳登录界面
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);

						}
					}
				} else {// 点击取消关注
					if (((ExhibitorsSearchChildBean) getItem(position)).follow) {
						// VolleyUtil volleyUtil = new VolleyUtil(mContext);
						// volleyUtil.onStop();
						String url = String.format(GloableConfig.CANCLE_FOLLOW_ZHANSHANG_URL, GloableConfig.userid,
								((ExhibitorsSearchChildBean) getItem(position)).id);
						loadData(url, "展商搜索-取消关注 的url:", 1002);
					}
				}
			}
		});
		if (getItem(position).follow) {
			checkbox.setChecked(true);
			checkbox.setEnabled(false);// 为关注状态的时候 不可以取关（关注不可逆）
		} else {
			checkbox.setChecked(false);
			checkbox.setEnabled(true);// 为未关注的时候 可以点击关注（关注不可逆）
		}
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		PublicResultCodeBean data = CommonUtil.gson.fromJson(json, PublicResultCodeBean.class);
		if (data.resultCode == 200) {
			/** 请求成功的处理,在此做当前界面的数据填充 */
			L.v("lixm", "成功");
		} else {
			/** 请求失败的处理 */
			L.v("lixm", "失败");
		}
	}

	/** 请求网络数据 */
	private void loadData(String url, String text, int msgWhat) {
		ProgressUtil.showPregressDialog(mContext, R.layout.custom_progress);

		VolleyUtil volleyUtil1 = new VolleyUtil(mContext);
		// volleyUtil.onStop();
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, msgWhat);// 1001是msg.what
	}

	OnChangeFollowListener changeListener;
}
