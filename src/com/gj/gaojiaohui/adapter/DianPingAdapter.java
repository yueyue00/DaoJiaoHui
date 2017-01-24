package com.gj.gaojiaohui.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.Encoder;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.DianPingBean;
import com.gj.gaojiaohui.bean.ReplyBean;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageLoaderUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会点评的adapter
 * 
 * @author zhangt
 * 
 */
public class DianPingAdapter extends CommonAdapter<DianPingBean> {

	private Context mContext;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView headImageView;
	private ImageView dianping_reply_img;
	private GridView item_dianping_img_gv;
	private ListView dianping_leavemsg_lv;
	private DianPingGirdviewAdapter dianPingGirdviewAdapter;
	private ReplyAdapter replyAdapter;
	private Map<Integer, ReplyAdapter> adapterMap;
	private RelativeLayout dianping_reply_rl;
	private LinearLayout dianping_leavemsg_rl;
	private ImageView item_dianping_img_img;
	private int layoutPosition;
	private Map<Integer, Boolean> dataMap;
	private EditText dianping_reply_et;
	private ImageView reply_publish_img;
	private Boolean showKeyBoard;
	private List<DianPingBean> dianPingBeans;
	private Map<Integer, EditText> edMap;
	private int currentPosin; // 当前回复位置
	private ReplyBean replybean; // 当前回复的bean
	public EditReplyCallBack callBack;
	private DisplayImageOptions headOptions;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1001) {
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						CustomToast.showToast(mContext, mContext.getResources().getString(R.string.reply_success));
						ProgressUtil.dismissProgressDialog();
						edMap.get(currentPosin).setText("");
						changeView(layoutPosition, false);
						dianping_reply_et.setFocusable(false);
						// 自己评论的内容直接显示
						if (callBack != null) {
							callBack.clickReply(currentPosin, replybean);
						}
					} else {
						CustomToast.showToast(mContext, mContext.getResources().getString(R.string.reply_fail));
						ProgressUtil.dismissProgressDialog();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};

	public DianPingAdapter(Context context, List<DianPingBean> datas, int itemLayoutResId) {
		super(context, datas, itemLayoutResId);
		mContext = context;
		options = ImageLoaderUtils.initOptions();
		imageLoader = ImageLoader.getInstance();
		dianPingBeans = datas;
		edMap = new HashMap<>();
		dataMap = new HashMap<>();
		adapterMap = new HashMap<>();
		for (int i = 0; i < datas.size(); i++) {
			dataMap.put(i, false);
		}

		headOptions = initHeadOptions();
	}

	@Override
	public void convert(final ViewHolder viewHolder, DianPingBean bean) {

		int position = viewHolder.getPosition();

		headImageView = viewHolder.getView(R.id.item_dianping_head_img);
		dianping_reply_img = viewHolder.getView(R.id.dianping_reply_img);
		item_dianping_img_gv = viewHolder.getView(R.id.item_dianping_img_gv);
		dianping_leavemsg_lv = viewHolder.getView(R.id.dianping_leavemsg_lv);
		dianping_reply_rl = viewHolder.getView(R.id.dianping_reply_rl);
		dianping_leavemsg_rl = viewHolder.getView(R.id.dianping_leavemsg_rl);
		item_dianping_img_img = viewHolder.getView(R.id.item_dianping_img_img);
		dianping_reply_et = viewHolder.getView(R.id.dianping_reply_et);
		reply_publish_img = viewHolder.getView(R.id.reply_publish_img);

		edMap.put(viewHolder.getPosition(), dianping_reply_et);

		imageLoader.displayImage(bean.pic, headImageView, headOptions);
		viewHolder.setText(R.id.item_dianping_name_tv, bean.name);
		viewHolder.setText(R.id.item_dianping_time_tv, bean.date);
		viewHolder.setText(R.id.item_dianping_contain_tv, bean.value);

		CommonUtils.EmojiFilter(dianping_reply_et, 100);

		dianping_reply_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutPosition = viewHolder.getPosition();
				notifyDataSetChanged();
				if (!dataMap.get(layoutPosition)) {
					for (Integer key : dataMap.keySet()) {
						dataMap.put(key, false);
					}
					changeView(layoutPosition, true);
					dianping_reply_et.setFocusable(true);
					dianping_reply_et.setFocusableInTouchMode(true);
					dianping_reply_et.requestFocus();
					showKeyBoard = true;

				} else {
					changeView(layoutPosition, false);
					InputMethodManager imm = (InputMethodManager) dianping_reply_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(dianping_reply_et.getWindowToken(), 0); // 强制隐藏键盘
				}
			}
		});

		if (dataMap.get(position)) {
			dianping_reply_rl.setVisibility(View.VISIBLE);
			if (showKeyBoard) {
				dianping_reply_et.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
						dianping_reply_et.getLeft() + 100, dianping_reply_et.getTop() + 10, 0));
				dianping_reply_et.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
						dianping_reply_et.getLeft() + 100, dianping_reply_et.getTop() + 10, 0));
				showKeyBoard = false;
			}

		} else {
			dianping_reply_rl.setVisibility(View.GONE);
		}

		if (bean.pic_list.size() > 1) {
			// 图片数量大于1
			item_dianping_img_gv.setVisibility(View.VISIBLE);
			item_dianping_img_img.setVisibility(View.GONE);
			dianPingGirdviewAdapter = new DianPingGirdviewAdapter(mContext, bean.pic_list, R.layout.item_dianping_gridview_layout);
			item_dianping_img_gv.setAdapter(dianPingGirdviewAdapter);
			setGridViewHeightBasedOnChildren(item_dianping_img_gv);
			dianPingGirdviewAdapter.notifyDataSetChanged();
		} else if (bean.pic_list.size() == 1) {
			// 图片数量等于1
			item_dianping_img_gv.setVisibility(View.GONE);
			item_dianping_img_img.setVisibility(View.VISIBLE);
			ImageAware imageAware = new ImageViewAware(item_dianping_img_img, false);
			imageLoader.displayImage(bean.pic_list.get(0).pic, imageAware,options);

		} else {
			// 图片数量等于0
			item_dianping_img_gv.setVisibility(View.GONE);
			item_dianping_img_img.setVisibility(View.GONE);
		}

		if (bean.comment_list.size() != 0) {
			dianping_leavemsg_rl.setVisibility(View.VISIBLE);
			replyAdapter = new ReplyAdapter(mContext, bean.comment_list, R.layout.item_dianping_reply_item);
			adapterMap.put(viewHolder.getPosition(), replyAdapter);
			dianping_leavemsg_lv.setAdapter(replyAdapter);
			setListViewHeightBasedOnChildren(dianping_leavemsg_lv);
		} else {
			dianping_leavemsg_rl.setVisibility(View.GONE);
		}

		reply_publish_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				replybean = new ReplyBean();
				String replyString = edMap.get(viewHolder.getPosition()).getText().toString();
				if (StringUtils.isNull(replyString)) {
					CustomToast.showToast(mContext, mContext.getResources().getString(R.string.replyIsNull));
				} else {
					currentPosin = viewHolder.getPosition();
					replybean.name = GloableConfig.username;
					replybean.value = replyString;
					ProgressUtil.showPregressDialog(mContext);
					VolleyUtil volleyUtil = new VolleyUtil(mContext);
					Map<String, String> map = new HashMap<>();
					try {
						replyString = URLEncoder.encode(replyString, "UTF-8");
						String url = String.format(GloableConfig.DIANPING_REPLE_URL, dianPingBeans.get(viewHolder.getPosition()).icomment_id,
								GloableConfig.userid, replyString);
						volleyUtil.stringRequest(handler, url, map, 1001);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				}
			}
		});

	}

	private void changeView(int position, Boolean value) {
		dataMap.put(position, value);
	}

	/*
	 * 动态设置ListView组建的高度
	 */
	@SuppressLint("NewApi")
	public void setGridViewHeightBasedOnChildren(GridView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int count = listAdapter.getCount();
		int columns;
		if (count > 4) {
			columns = 3;
		} else if (count > 1) {
			columns = 2;
		} else {
			columns = 1;
		}
		int line;
		if (count > 6) {
			line = 3;
		} else if (count > 3) {
			line = 2;
		} else {
			line = 1;
		}

		int totalHeight = 0;
		int totalWidth = 0;
		listView.setNumColumns(columns);

		for (int i = 0; i < columns; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalWidth += listItem.getMeasuredHeight();

		}
		for (int i = 0; i < line; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		params.width = totalWidth + (columns - 1) * 15;
		listView.setLayoutParams(params);
	}

	/*
	 * 动态设置ListView组建的高度
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public void Changed() {
		for (int i = 0; i < datas.size(); i++) {
			dataMap.put(i, false);
		}
		notifyDataSetChanged();
	}

	public void setCallBack(EditReplyCallBack callBack) {
		this.callBack = callBack;
	}

	public interface EditReplyCallBack {
		public void clickReply(int position, ReplyBean bean);
	}

	/**
	 * 初始化DisplayImageOptions
	 */
	public DisplayImageOptions initHeadOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// 设置图片在下载期间显示的图片
				.showImageOnLoading(R.drawable.pic_vip)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.pic_vip)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisk(true)
				// 设置图片的url为空的时候显示的图片
				.showImageForEmptyUri(R.drawable.pic_vip)
				// --------------------------------------------------------------------
				// 如果您只想简单使用ImageLoader这块也可以不用配置
				// 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.considerExifParams(true)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				// 设置图片的解码类型//
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 设置图片的解码配置
				// .decodingOptions(options)
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				// 设置图片在下载前是否重置，复位
				.resetViewBeforeLoading(true)
				// 是否设置为圆角，弧度为多少
				.displayer(new CircleBitmapDisplayer(-24))// 设置图片变成圆形
				// // -22是圆角弧度　可以正数也可负数
				// // 默认是0 就是最圆的
				// 是否图片加载好后渐入的动画时间
				// .displayer(new FadeInBitmapDisplayer(100))
				// 构建完成
				// -------------------------------------------------------------------

				.build();
		return options;
	}

}
