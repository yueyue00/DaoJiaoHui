package com.gj.gaojiaohui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gaojiaohui.activity.ZiXunDetailActivity;
import com.gj.gaojiaohui.bean.HomePageNewsListBean;
import com.gj.gaojiaohui.bean.HomePageNewsTopBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartdot.wenbo.huiyi.R;

/** 轮播图适配器 */
public class ScrollHeadAdapter extends PagerAdapter {
	List<HomePageNewsTopBean> data; // 正常的数据
	List<HomePageNewsTopBean> tmpdata; // 为了手动循环滑动而添加的临时数据
	Map<Integer, Bitmap> maps = new HashMap<Integer, Bitmap>();
	ViewPager mViewPager;
	Context context;

	public ScrollHeadAdapter(Context context, List<HomePageNewsTopBean> data, ViewPager mViewPager) {
		this.tmpdata = data;
		this.data = data;

		this.context = context;
		this.mViewPager = mViewPager;
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
		ImageLoader.getInstance().init(configuration);
	}

	/** 刷新数据 */
	public void refreshData() {
		maps.clear();
		// if (data.size() > 0) {
		// tmpdata = new ArrayList<>();
		// tmpdata.add(data.get(data.size()-1));
		// for (int i = 0; i < data.size(); i++) {
		// tmpdata.add(data.get(i));
		// }
		// tmpdata.add(data.get(0));
		// }
		notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
	}

	@Override
	public int getCount() {
		return tmpdata.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		// 在此不执行销毁操作
		((ViewPager) view).removeView((View) object);
	}

	// 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View v = LayoutInflater.from(context).inflate(R.layout.scroll_headview_item, null);
		final ImageView imgv = (ImageView) v.findViewById(R.id.scroll_headview_item_icon);
		TextView title = (TextView) v.findViewById(R.id.scroll_headview_item_title);
		view.addView(v);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 跳转事件
				Intent intent = new Intent(context, ZiXunDetailActivity.class);
				intent.putExtra("title", tmpdata.get(position).title);
				intent.putExtra("content", tmpdata.get(position).value);
				intent.putExtra("imgurl", tmpdata.get(position).pic);
				intent.putExtra("webviewUrl", tmpdata.get(position).url);
				context.startActivity(intent);
			}
		});

		HomePageNewsTopBean item = ((HomePageNewsTopBean) tmpdata.get(position));
		title.setText(item.title.trim());
		if (maps.get(position) != null) {
			imgv.setImageBitmap(maps.get(position));
		} else {
			ImageLoader.getInstance().loadImage(item.pic, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String s, View view) {

				}

				@Override
				public void onLoadingFailed(String s, View view, FailReason failReason) {

				}

				@Override
				public void onLoadingComplete(String s, View view, Bitmap bitmap) {
					maps.put(position, bitmap);
					if (bitmap != null)
						imgv.setImageBitmap(bitmap);
				}

				@Override
				public void onLoadingCancelled(String s, View view) {

				}
			});
		}

		return v;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
