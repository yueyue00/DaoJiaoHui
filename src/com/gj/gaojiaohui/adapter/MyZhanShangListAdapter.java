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
import com.gj.gaojiaohui.bean.ExhibitorsListChildBean;
import com.gj.gaojiaohui.bean.PublicResultCodeBean;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

public class MyZhanShangListAdapter extends CommonAdapter<ExhibitorsListChildBean> {

	private List<ExhibitorsListChildBean> items = new ArrayList<ExhibitorsListChildBean>();

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

	public MyZhanShangListAdapter(Context context, List<ExhibitorsListChildBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, ExhibitorsListChildBean bean) {
		int position = viewHolder.getPosition();
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
					if (!((ExhibitorsListChildBean) getItem(position)).follow) {
						if (!TextUtils.isEmpty(GloableConfig.userid)) {
							if (!TextUtils.isEmpty(GloableConfig.username)) {
								// userid不为空证明已经登录
								String url = String.format(GloableConfig.FOLLOW_ZHANSHANG_URL, GloableConfig.userid,
										((ExhibitorsListChildBean) getItem(position)).id);
								loadData(url, "adapter展商-关注 的url:", 1001);
								getItem(position).follow = true;// 修改本地数据
							} else {
								// username为空跳补充个人信息界面
								CustomToast.showToast(mContext, mContext.getResources().getString(R.string.finish_you_personal_info));
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
				} else {
					notifyDataSetChanged();
				}
			}
		});
		if (getItem(position).follow) {
			checkbox.setChecked(true);
			checkbox.setEnabled(false);
		} else {
			checkbox.setChecked(false);
			checkbox.setEnabled(true);
		}
	}

	/** 解析json数据 */
	private void parseData(Message msg) {

		String json = msg.obj.toString();
		L.v("lixm", "关注:"+json);
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
		// volleyUtil1.onStop();
		Map<String, String> map = new HashMap<>();// Map为空，必须要有
		volleyUtil1.stringRequest(handler, url, map, msgWhat);// 1001是msg.what
		L.v("lixm", text+url);
	}
}
