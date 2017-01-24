package com.gj.gaojiaohui.activity;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.gj.gaojiaohui.bean.PicBean;
import com.gj.gaojiaohui.view.PinchImageView;
import com.gj.gaojiaohui.view.PinchImageViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;

/**
 * 显示图片的viewpager
 * 
 * @author zhangt
 * 
 */

public class PicViewPagerActivity extends GaoJiaoHuiBaseActivity {

	// private String[] mImages = new String[] {
	// "http://mmbiz.qpic.cn/mmbiz_jpg/LfaiaOo0DO2iaIoHmzmib16EYZhlJfCNQUggew0WzhTzc01XSh6eEXE9kKYRoAiaicib1VfdNicaeW5Z4qxMicvz2bghLg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",
	// "http://mmbiz.qpic.cn/mmbiz_jpg/LfaiaOo0DO2iaIoHmzmib16EYZhlJfCNQUg0PePElCCbJGhr84ib2ic4jib4m8U64IxK6RVdpB1cJ6SVx00m4QSq1F1w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",
	// "http://mmbiz.qpic.cn/mmbiz_jpg/LfaiaOo0DO2iaIoHmzmib16EYZhlJfCNQUgpmVZjFbw1Lb1dPHgHbWgQVUnP7OOicpw9DyUgBApwN97icpJnnCFG8vQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1",
	// "http://mmbiz.qpic.cn/mmbiz_jpg/LfaiaOo0DO2iaIoHmzmib16EYZhlJfCNQUg6KFbNgaMyib5ZbAWLFE59oOOLKMaGsR4hlZbFHCUSrAl5nuP2x7OW1A/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1"
	// };

	private LinkedList<PinchImageView> viewCache;
	private PinchImageViewPager pager;
	private ImageLoader imageLoader;
	private List<PicBean> datas;
	private int page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_pic_view_pager);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("pic");
		page = bundle.getInt("page");
		datas = (List<PicBean>) bundle.getSerializable("datas");
		initView();
	}

	private void initView() {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		viewCache = new LinkedList<PinchImageView>();
		pager = (PinchImageViewPager) findViewById(R.id.pic_viewpager);
		pager.setAdapter(new myPagerAdapter());
		pager.setCurrentItem(page);
	}

	class myPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PinchImageView piv;
			if (viewCache.size() > 0) {
				piv = viewCache.remove();
				piv.reset();
			} else {
				piv = new PinchImageView(PicViewPagerActivity.this);
			}
			DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).build();
			imageLoader.displayImage(datas.get(position).pic, piv, options);
			container.addView(piv);
			return piv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			PinchImageView piv = (PinchImageView) object;
			container.removeView(piv);
			viewCache.add(piv);
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			pager.setMainPinchImageView((PinchImageView) object);
		}
	}
	

}
