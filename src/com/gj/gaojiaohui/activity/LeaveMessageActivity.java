package com.gj.gaojiaohui.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.utils.AsyncTaskForComPressPhoto;
import com.gj.gaojiaohui.utils.CommonUtils;
import com.gj.gaojiaohui.utils.CustomToast;
import com.gj.gaojiaohui.utils.ImageUtils;
import com.gj.gaojiaohui.utils.L;
import com.gj.gaojiaohui.utils.PhotoUtils;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.StringUtils;
import com.gj.gaojiaohui.utils.VolleyUtil;
import com.gj.gaojiaohui.view.AlbumViewPager;
import com.gj.gaojiaohui.view.FilterImageView;
import com.gj.gaojiaohui.view.LocalImageHelper;
import com.gj.gaojiaohui.view.LocalImageHelper.LocalFile;
import com.hebg3.mxy.utils.AsyncTaskForUpLoadFilesNew;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

/**
 * 发布留言界面
 * 
 * @author zhangt
 * 
 */
public class LeaveMessageActivity extends GaoJiaoHuiBaseActivity implements OnClickListener, com.gj.gaojiaohui.view.MatrixImageView.OnSingleTapListener {

	private Context mContext;
	private TextView backTV; // 返回
	private TextView publishTV; // 发布
	private EditText leaveMessageED; // 动态内容编辑框
	private InputMethodManager imm;// 软键盘管理
	private TextView textRemainTV;// 字数提示
	private TextView picRemain;// 图片数量提示
	private ImageView add;// 添加图片按钮
	private LinearLayout picContainer;// 图片容器
	private List<LocalFile> pictures = new ArrayList<>();// 图片路径数组
	HorizontalScrollView scrollView;// 滚动的图片容器
	View editContainer;// 动态编辑部分
	View pagerContainer;// 图片显示部分

	// 显示大图的viewpager 集成到了Actvity中 下面是和viewpager相关的控件
	AlbumViewPager viewpager;// 大图显示pager
	ImageView mBackView;// 返回/关闭大图
	TextView mCountView;// 大图数量提示
	View mHeaderBar;// 大图顶部栏
	ImageView delete;// 删除按钮

	int size;// 小图大小
	int padding;// 小图间距
	DisplayImageOptions options;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				// 图片压缩完毕，开始上传文件
				uploadPic();
			} else if (msg.what == 200) {
				ProgressUtil.dismissProgressDialog();
				// 发送成功
				BasePojo bp = (BasePojo) msg.obj;
				CustomToast.showToast(mContext, mContext.getResources().getString(R.string.public_success));
				setResult(RESULT_OK);
				finish();
			} else if (msg.what == 1001) {
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					if (jsonObject.getInt("resultCode") == 200) {
						ProgressUtil.dismissProgressDialog();
						CustomToast.showToast(mContext, mContext.getResources().getString(R.string.public_success));
						setResult(RESULT_OK);
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		/**
		 * 上传图片+文字
		 */
		private void uploadPic() {
			HashMap<String, String> params = new HashMap<String, String>();
			String comment_vlaue;
			comment_vlaue = leaveMessageED.getText().toString().trim();
			try {
				comment_vlaue = new String(comment_vlaue.getBytes("UTF-8"), "iso-8859-1");
				params.put("method", "createTheme2");
				params.put("userid", GloableConfig.userid);
				params.put("comment_value", comment_vlaue);

				HashMap<String, File> files = new HashMap<String, File>();

				for (int i = 0; i < pictures.size(); i++) {
					File photo = new File(pictures.get(i).getUploadUrl());// 上传压缩后的图片，而不是原图
					files.put("" + i, photo);
				}
				AsyncTaskForUpLoadFilesNew at = new AsyncTaskForUpLoadFilesNew(mContext, GloableConfig.DIANPING_PUBLIC_URL, params, files,
						handler.obtainMessage());
				at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leave_message);
		mContext = this;
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		// 设置ImageLoader参数
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false).showImageForEmptyUri(R.drawable.zsxq_img_default_nor)
				.showImageOnFail(R.drawable.zsxq_img_default_nor).showImageOnLoading(R.drawable.zsxq_img_default_nor).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();
		initViews();
		initData();
	}

	/**
	 * @Description： 初始化Views
	 */
	private void initViews() {
		backTV = (TextView) findViewById(R.id.cancle_tv);
		publishTV = (TextView) findViewById(R.id.publish_tv);
		leaveMessageED = (EditText) findViewById(R.id.leave_message_tv);
		textRemainTV = (TextView) findViewById(R.id.word_num_tv);
		picRemain = (TextView) findViewById(R.id.post_pic_remain);
		add = (ImageView) findViewById(R.id.post_add_pic);
		picContainer = (LinearLayout) findViewById(R.id.post_pic_container);
		scrollView = (HorizontalScrollView) findViewById(R.id.post_scrollview);
		viewpager = (AlbumViewPager) findViewById(R.id.albumviewpager);
		mBackView = (ImageView) findViewById(R.id.header_bar_photo_back);
		mCountView = (TextView) findViewById(R.id.header_bar_photo_count);
		mHeaderBar = findViewById(R.id.album_item_header_bar);
		delete = (ImageView) findViewById(R.id.header_bar_photo_delete);
		editContainer = findViewById(R.id.publish_edit_container);
		pagerContainer = findViewById(R.id.pagerview);
		delete.setVisibility(View.VISIBLE);

		viewpager.setOnPageChangeListener(pageChangeListener);
		viewpager.setOnSingleTapListener(this);
		mBackView.setOnClickListener(this);
		mCountView.setOnClickListener(this);
		backTV.setOnClickListener(this);
		publishTV.setOnClickListener(this);
		add.setOnClickListener(this);
		delete.setOnClickListener(this);

		CommonUtils.EmojiFilter(leaveMessageED, 100);

		leaveMessageED.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable content) {
				textRemainTV.setText(content.toString().length() + "");
			}
		});
	}

	private void initData() {
		size = (int) getResources().getDimension(R.dimen.pic_size);
		padding = (int) getResources().getDimension(R.dimen.padding_10);
	}

	@Override
	public void onBackPressed() {
		if (pagerContainer.getVisibility() != View.VISIBLE) {
			finish();
		} else {
			hideViewPager();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_tv:
			// CustomToast.showToast(mContext, "返回");
			finish();
			break;
		case R.id.header_bar_photo_back:
		case R.id.header_bar_photo_count:
			hideViewPager();
			break;
		case R.id.header_bar_photo_delete:
			final int index = viewpager.getCurrentItem();
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(mContext.getResources().getString(R.string.notifi)).setTitle(mContext.getResources().getString(R.string.isDelete))
					.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					}).setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							pictures.remove(index);
							if (pictures.size() == 9) {
								add.setVisibility(View.GONE);
							} else {
								add.setVisibility(View.VISIBLE);
							}
							if (pictures.size() == 0) {
								hideViewPager();
							}
							picContainer.removeView(picContainer.getChildAt(index));
							picRemain.setText(pictures.size() + "/9");
							mCountView.setText((viewpager.getCurrentItem() + 1) + "/" + pictures.size());
							viewpager.getAdapter().notifyDataSetChanged();
							LocalImageHelper.getInstance().setCurrentSize(pictures.size());
							dialog.dismiss();

						}
					}).show();

			break;
		case R.id.publish_tv:
			ProgressUtil.showPregressDialog(mContext);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			String content = leaveMessageED.getText().toString();
			if (StringUtils.isNull(content) && pictures.isEmpty()) {
				Toast.makeText(this, mContext.getResources().getString(R.string.leastPic), Toast.LENGTH_SHORT).show();
				return;
			} else if (pictures.isEmpty()) {
				try {
					content = URLEncoder.encode(content, "UTF-8");
					VolleyUtil volleyUtil = new VolleyUtil(mContext);
					Map<String, String> map = new HashMap<>();
					String url = String.format(GloableConfig.DIANPING_PUBLIC_NOPIC_URL, GloableConfig.userid, content);
					volleyUtil.stringRequest(handler, url, map, 1001);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

			} else {
				// 启动线程压缩准备上传的图片原图
				AsyncTaskForComPressPhoto at = new AsyncTaskForComPressPhoto(pictures, handler.obtainMessage());
				at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
			}
			break;
		case R.id.post_add_pic:
			Intent intent = new Intent(mContext, LocalAlbum.class);
			startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
			break;
		default:
			if (v instanceof FilterImageView) {
				for (int i = 0; i < picContainer.getChildCount(); i++) {
					if (v == picContainer.getChildAt(i)) {
						showViewPager(i);
					}
				}
			}
			break;
		}

	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			if (viewpager.getAdapter() != null) {
				String text = (position + 1) + "/" + viewpager.getAdapter().getCount();
				mCountView.setText(text);
			} else {
				mCountView.setText("0/0");
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	// 显示大图pager
	private void showViewPager(int index) {
		pagerContainer.setVisibility(View.VISIBLE);
		editContainer.setVisibility(View.GONE);
		viewpager.setAdapter(viewpager.new LocalViewPagerAdapter(pictures));
		viewpager.setCurrentItem(index);
		mCountView.setText((index + 1) + "/" + pictures.size());
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1, pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
		scaleAnimation.setDuration(200);
		set.addAnimation(scaleAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
		alphaAnimation.setDuration(200);
		set.addAnimation(alphaAnimation);
		pagerContainer.startAnimation(set);
	}

	// 关闭大图显示
	private void hideViewPager() {
		pagerContainer.setVisibility(View.GONE);
		editContainer.setVisibility(View.VISIBLE);
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9, pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
		scaleAnimation.setDuration(200);
		set.addAnimation(scaleAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(200);
		set.addAnimation(alphaAnimation);
		pagerContainer.startAnimation(set);
	}

	@Override
	public void onSingleTap() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
			if (LocalImageHelper.getInstance().isResultOk()) {
				LocalImageHelper.getInstance().setResultOk(false);
				// 获取选中的图片
				List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
				for (int i = 0; i < files.size(); i++) {
					files.get(i).setOriginalUrl(PhotoUtils.getPath(mContext, Uri.parse(files.get(i).getOriginalUri())));
					LayoutParams params = new LayoutParams(size, size);
					params.rightMargin = padding;
					FilterImageView imageView = new FilterImageView(this);
					imageView.setLayoutParams(params);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					ImageLoader.getInstance().displayImage(files.get(i).getThumbnailUri(), new ImageViewAware(imageView), options, null, null,
							files.get(i).getOrientation());
					imageView.setOnClickListener(this);
					files.get(i).setUploadUrl(
							getApplicationContext().getFilesDir().getAbsolutePath()
									+ "/"
									+ files.get(i).getOriginalUrl()
											.substring(files.get(i).getOriginalUrl().lastIndexOf("/") + 1, files.get(i).getOriginalUrl().length()));

					pictures.add(files.get(i));
					if (pictures.size() == 9) {
						add.setVisibility(View.GONE);
					} else {
						add.setVisibility(View.VISIBLE);
					}
					picContainer.addView(imageView, picContainer.getChildCount() - 1);
					picRemain.setText(pictures.size() + "/9");
					LocalImageHelper.getInstance().setCurrentSize(pictures.size());
				}
				// 清空选中的图片
				files.clear();
				// 设置当前选中的图片数量
				LocalImageHelper.getInstance().setCurrentSize(pictures.size());
				// 延迟滑动至最右边
				new Handler().postDelayed(new Runnable() {
					public void run() {
						scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
					}
				}, 50L);
			}
			// 清空选中的图片
			LocalImageHelper.getInstance().getCheckedItems().clear();
			break;
		default:
			break;
		}
	}

}
