package com.gj.gaojiaohui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.VipRegisterBtnBean;
import com.gj.gaojiaohui.bean.VipRegisterListBean;
import com.gj.gaojiaohui.port.OnChangeVipRegisterListener;
import com.gj.gaojiaohui.utils.FirstLetterUtil;
import com.smartdot.wenbo.huiyi.R;

/**
 * 嘉宾签到列表的适配器
 */
@SuppressLint("DefaultLocale") public class VipRegisterAdapter extends CommonAdapter<VipRegisterListBean> implements SectionIndexer {
	private List<VipRegisterListBean> items = new ArrayList<VipRegisterListBean>();
	private Context mContext;
	/** 当前用户权限是否允许签到 true为允许 */
	private boolean permission;

	/** 存放每个position对应的签到按钮id */
	private String[] ids = new String[64];
	/** 存放每个position对应的签到按钮的选中状态 */
	private boolean[] states = new boolean[64];

	public VipRegisterAdapter(Context context, List<VipRegisterListBean> list, int itemLayoutResId) {
		super(context, list, itemLayoutResId);
		this.items = list;
		this.mContext = context;
	}

	@Override
	public void convert(ViewHolder viewHolder, VipRegisterListBean bean) {
		int position = viewHolder.getPosition();
		final String userId = bean.vipId;
		// 显示嘉宾姓名
		viewHolder.setText(R.id.user_name, bean.name);

		// 通过解析的数据设置不同的签到按钮选中状态,并将对应的按钮id和对应的选中状态进行保存
		loadData(viewHolder, bean);

		if (permission) {
			addListener(viewHolder, userId, position);
		}
	}

	/** 签到数据加载 */
	private void loadData(ViewHolder viewHolder, VipRegisterListBean bean) {
		List<VipRegisterBtnBean> list = bean.btn_list;
		for (int i = 0; i < list.size(); i++) {

			switch (i) {
			case 0:// 签到
				if (list.get(i).state) {
					((TextView) viewHolder.getView(R.id.qian_dao_tv)).setSelected(true);
					((ImageView) viewHolder.getView(R.id.qian_dao_img)).setSelected(true);
				} else {
					((TextView) viewHolder.getView(R.id.qian_dao_tv)).setSelected(false);
					((ImageView) viewHolder.getView(R.id.qian_dao_img)).setSelected(false);
				}
				ids[i] = list.get(i).btn_id;
				states[i] = list.get(i).state;
				break;
			case 1:// 接机
				if (list.get(i).state) {
					((TextView) viewHolder.getView(R.id.jie_ji_tv)).setSelected(true);
					((ImageView) viewHolder.getView(R.id.jie_ji_img)).setSelected(true);
				} else {
					((TextView) viewHolder.getView(R.id.jie_ji_tv)).setSelected(false);
					((ImageView) viewHolder.getView(R.id.jie_ji_img)).setSelected(false);
				}
				ids[i] = list.get(i).btn_id;
				states[i] = list.get(i).state;
				break;
			case 2:// 入住
				if (list.get(i).state) {
					((TextView) viewHolder.getView(R.id.ru_zhu_tv)).setSelected(true);
					((ImageView) viewHolder.getView(R.id.ru_zhu_img)).setSelected(true);
				} else {
					((TextView) viewHolder.getView(R.id.ru_zhu_tv)).setSelected(false);
					((ImageView) viewHolder.getView(R.id.ru_zhu_img)).setSelected(false);
				}
				ids[i] = list.get(i).btn_id;
				states[i] = list.get(i).state;
				break;
			case 3:// 看展览
				if (list.get(i).state) {
					((TextView) viewHolder.getView(R.id.zhan_lan_tv)).setSelected(true);
					((ImageView) viewHolder.getView(R.id.zhan_lan_img)).setSelected(true);
				} else {
					((TextView) viewHolder.getView(R.id.zhan_lan_tv)).setSelected(false);
					((ImageView) viewHolder.getView(R.id.zhan_lan_img)).setSelected(false);
				}
				ids[i] = list.get(i).btn_id;
				states[i] = list.get(i).state;
				break;
			case 4:// 送机
				if (list.get(i).state) {
					((TextView) viewHolder.getView(R.id.song_ji_tv)).setSelected(true);
					((ImageView) viewHolder.getView(R.id.song_ji_img)).setSelected(true);
				} else {
					((TextView) viewHolder.getView(R.id.song_ji_tv)).setSelected(false);
					((ImageView) viewHolder.getView(R.id.song_ji_img)).setSelected(false);
				}
				ids[i] = list.get(i).btn_id;
				states[i] = list.get(i).state;
				break;
			}
		}
	}

	/**
	 * 签到按钮操作加载
	 * 
	 * @param position
	 */
	private void addListener(ViewHolder viewHolder, final String userId, final int position) {
		// TODO 判断为没有签到过 在这里执行网络请求 将签到按钮id和嘉宾id传过去
		/** 签到 */
		((LinearLayout) viewHolder.getView(R.id.qian_dao_layout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getItem(position).btn_list.get(0).state) {
					changeListener.OnRegisterData(userId, ids[0]);
				}
			}
		});
		/** 接机 */
		((LinearLayout) viewHolder.getView(R.id.jie_ji_layout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getItem(position).btn_list.get(1).state) {
					changeListener.OnRegisterData(userId, ids[1]);
				}
			}
		});
		/** 入住 */
		((LinearLayout) viewHolder.getView(R.id.ru_zhu_layout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getItem(position).btn_list.get(2).state) {
					changeListener.OnRegisterData(userId, ids[2]);
				}
			}
		});
		/** 看展览 */
		((LinearLayout) viewHolder.getView(R.id.zhan_lan_layout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getItem(position).btn_list.get(3).state) {
					changeListener.OnRegisterData(userId, ids[3]);
				}
			}
		});
		/** 送机 */
		((LinearLayout) viewHolder.getView(R.id.song_ji_layout)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!getItem(position).btn_list.get(4).state) {
					changeListener.OnRegisterData(userId, ids[4]);
				}
			}
		});
	}

	OnChangeVipRegisterListener changeListener;

	/** 取消关注后的回调监听,让外围界面刷新 */
	public void setOnShopAppRefreshListener(OnChangeVipRegisterListener changeListener) {
		this.changeListener = changeListener;
	}

	/** 刷新数据 并将当前的用户权限传递过去 */
	public void changeData(List<VipRegisterListBean> list, boolean permission) {
		this.items = list;
		this.permission = permission;
		notifyDataSetChanged();
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		VipRegisterListBean bean;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				bean = (VipRegisterListBean) items.get(i);
				// 取首字母
				l = FirstLetterUtil.getFirstLetter(bean.name);
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		}
		bean = null;
		l = null;
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
