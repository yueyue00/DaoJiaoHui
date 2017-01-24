package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.activity.AddressGroupActivity;
import com.gj.gaojiaohui.activity.AudienceInteractionActivity;
import com.gj.gaojiaohui.activity.BusinessInformationActivity;
import com.gj.gaojiaohui.activity.ConferenceServiceActivity;
import com.gj.gaojiaohui.activity.DaHuiNaviActivity;
import com.gj.gaojiaohui.activity.DianPingActivity;
import com.gj.gaojiaohui.activity.DiaoDuActivity;
import com.gj.gaojiaohui.activity.FollowMyActivity;
import com.gj.gaojiaohui.activity.GeneralStatisticsActivity;
import com.gj.gaojiaohui.activity.GuestCommunicationActivity;
import com.gj.gaojiaohui.activity.GuestListActivity;
import com.gj.gaojiaohui.activity.GuestServiceActivity;
import com.gj.gaojiaohui.activity.InterViewAppointmentActivity;
import com.gj.gaojiaohui.activity.LiangdianActivity;
import com.gj.gaojiaohui.activity.MediaInteractionActivity;
import com.gj.gaojiaohui.activity.MeetingDatasActivity;
import com.gj.gaojiaohui.activity.MeetingNewsActivity;
import com.gj.gaojiaohui.activity.MyRegistrationActivity;
import com.gj.gaojiaohui.activity.MyTracksActivity;
import com.gj.gaojiaohui.activity.NoticeCenterActivity;
import com.gj.gaojiaohui.activity.VipRegisterActivity;
import com.gj.gaojiaohui.bean.MenuBean;
import com.gj.gaojiaohui.qrcode.activity.CaptureActivity;
import com.smartdot.wenbo.huiyi.R;

public class MenuGridViewAdapter extends CommonAdapter<MenuBean> {

	private Context mContext;

	private TextView nameTextView;
	private ImageView imageView;

	public MenuGridViewAdapter(Context context, List<MenuBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		mContext = context;
	}

	@Override
	public void convert(ViewHolder viewHolder, MenuBean bean) {
		nameTextView = (TextView) viewHolder.getView(R.id.item_menu_girdview_title);
		imageView = (ImageView) viewHolder.getView(R.id.item_menu_girdview_img);
		switch (bean.id) {
		case "1":
			setText(mContext.getString(R.string.notice_center));
			setImage(R.drawable.home_btn_tongzhizhongxin_nor);
			setClickListener(NoticeCenterActivity.class);
			break;
		case "2":
			setText(mContext.getString(R.string.scan_qrcode));
			setImage(R.drawable.home_btn_qcode_nor);
			setClickListener(CaptureActivity.class);
			break;
		case "3":
			setText(mContext.getString(R.string.guide));
			setImage(R.drawable.home_btn_dahuidaohang_nor);
			setClickListener(DaHuiNaviActivity.class);
			break;
		case "4":
			setText(mContext.getString(R.string.conference_info));
			setImage(R.drawable.home_btn_dahuizixun_nor);
			setClickListener(MeetingNewsActivity.class);
			break;
		case "5":
			setText(mContext.getString(R.string.highlights_recommendations));
			setImage(R.drawable.home_btn_liangdian_nor);
			setClickListener(LiangdianActivity.class);
			break;
		case "6":
			setText(mContext.getString(R.string.my_audience));
			setImage(R.drawable.home_btn_guanzhonghudong_nor);
			setClickListener(AudienceInteractionActivity.class);
			break;
		case "7":
			setText(mContext.getString(R.string.conference_service));
			setImage(R.drawable.home_btn_huiyifuwu_nor);
			setClickListener(ConferenceServiceActivity.class);
			break;
		case "8":
			setText(mContext.getString(R.string.my_followings));
			setImage(R.drawable.home_btn_wodeguanzhu_nor);
			setClickListener(FollowMyActivity.class);
			break;
		case "9":
			// setText(mContext.getString(R.string.my_followings));
			setImage(R.drawable.home_btn_wodebaoming_nor);
			setClickListener(MyRegistrationActivity.class);
			break;
		case "10":
			setText(mContext.getString(R.string.conference_comments));
			setImage(R.drawable.home_btn_dahuidianping_nor);
			setClickListener(DianPingActivity.class);
			break;
		case "11":
			setText(mContext.getString(R.string.service_dispatch));
			setImage(R.drawable.home_btn_fuwudiaodu_nor);
			setClickListener(DiaoDuActivity.class);
			break;
		case "12":
			// TODO 嘉宾列表
			setText(mContext.getString(R.string.guest_list));
			setImage(R.drawable.home_btn_jiabinliebiao_nor);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GuestListActivity.class);
					intent.putExtra("type", "liebiao");
					mContext.startActivity(intent);
				}
			});
			break;
		case "13":
			// TODO 大会统计
			setText(mContext.getString(R.string.conference_statistics));
			setImage(R.drawable.home_btn_huiyitongji_nor);
			setClickListener(GeneralStatisticsActivity.class);
			break;
		case "14":
			setText(mContext.getString(R.string.service_sign_in));
			setImage(R.drawable.home_btn_qiandao_nor);
			setClickListener(VipRegisterActivity.class);
			break;
		case "15":
			setText(mContext.getString(R.string.address_book));
			setImage(R.drawable.home_btn_tongxunlu_nor);
			setClickListener(AddressGroupActivity.class);
			break;
		case "16":
			setText(mContext.getString(R.string.guest_service));
			setImage(R.drawable.home_btn_jiabinfuwu_nor);
			setClickListener(GuestServiceActivity.class);
			break;
		case "17":
			setText(mContext.getString(R.string.guest_discussions));
			setImage(R.drawable.home_btn_jiabinjiaoliu_nor);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GuestCommunicationActivity.class);
					intent.putExtra("type", "jiaoliu");
					mContext.startActivity(intent);
				}
			});
			break;
		case "18":
			setText(mContext.getString(R.string.exhibitor_info));
			setImage(R.drawable.home_btn_zhanshangziliao_nor);
			setClickListener(BusinessInformationActivity.class);
			break;
		case "19":// 大会资料
			setText(mContext.getString(R.string.meetingdatas_title));
			setImage(R.drawable.home_btn_dahuiziliao_nor);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, MeetingDatasActivity.class);
					intent.putExtra("type", "2");
					mContext.startActivity(intent);
				}
			});
			break;
		case "20": // 采访预约
			setText(mContext.getString(R.string.book_an_interview));
			setImage(R.drawable.home_btn_caifangyuyue_nor);
			setClickListener(InterViewAppointmentActivity.class);
			break;
		case "21": // 媒体互动
			setText(mContext.getString(R.string.media_interaction));
			setImage(R.drawable.home_btn_meitihudong_nor);
			setClickListener(MediaInteractionActivity.class);
			break;
		case "22": // 我的足迹
			setText(mContext.getString(R.string.my_footprint));
			setImage(R.drawable.home_btn_wodezuji_nor);
			setClickListener(MyTracksActivity.class);
			break;
		case "23": // 媒体资料
			setText(mContext.getString(R.string.media_file));
			setImage(R.drawable.home_btn_meitiziliao_nor);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, MeetingDatasActivity.class);
					intent.putExtra("type", "1");
					mContext.startActivity(intent);
				}
			});
			break;
		default:
			break;
		}

	}

	private void setText(String name) {
		nameTextView.setText(name);
	}

	private void setImage(int imageId) {
		imageView.setImageResource(imageId);
	}

	/**
	 * 设置点击监听
	 * 
	 * @param <T>
	 */
	private <T> void setClickListener(final Class<T> cls) {
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, cls);
				mContext.startActivity(intent);
			}
		});

	}

}
