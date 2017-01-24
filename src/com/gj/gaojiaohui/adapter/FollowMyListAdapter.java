package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.gj.gaojiaohui.bean.FollowMyBean;
import com.gj.gaojiaohui.port.OnChangeFollowMyListener;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.smartdot.wenbo.huiyi.R;

/**
 * 关注我的列表的适配器
 */
public class FollowMyListAdapter extends CommonAdapter<FollowMyBean> {
	private List<FollowMyBean> items = new ArrayList<FollowMyBean>();

	private DisplayImageOptions options;

	private Context mContext;

	public FollowMyListAdapter(Context context, List<FollowMyBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		this.items = items;
		this.mContext = context;
		options = ImageLoaderUtils.initOptions();
	}

	@Override
	public void convert(ViewHolder viewHolder, FollowMyBean bean) {
		int position = viewHolder.getPosition();
		viewHolder.setText(R.id.follow_name_tv, bean.name);
		// 多选框监听
		checkListener(viewHolder, position);
	}

	private void checkListener(ViewHolder viewHolder, final int position) {
		CheckBox checkbox = (CheckBox) viewHolder.getView(R.id.follow_item_checkbox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					/** 关注 防止重复操作 如果已经选中了 不继续执行 */
					if (!((FollowMyBean) getItem(position)).follow) {
						changeListener.OnRefresh(((FollowMyBean) getItem(position)).id, true);
					}
				} else {
					/** 取消关注 防止重复操作 如果已经取消选中了 不继续执行 */
					if (((FollowMyBean) getItem(position)).follow) {
						changeListener.OnRefresh(((FollowMyBean) getItem(position)).id, false);
					}
				}
			}
		});
		// 根据关注状态进行赋值
		if (((FollowMyBean) getItem(position)).follow) {
			checkbox.setChecked(true);
		} else {
			checkbox.setChecked(false);
		}
	}

	OnChangeFollowMyListener changeListener;

	/** 取消关注/关注后的回调监听,让外围界面刷新 传的id是观众的id */
	public void setOnShopAppRefreshListener(OnChangeFollowMyListener changeListener) {
		this.changeListener = changeListener;
	}
}
