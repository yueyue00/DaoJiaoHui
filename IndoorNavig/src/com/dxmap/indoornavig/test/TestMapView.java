package com.dxmap.indoornavig.test;

import java.util.ArrayList;

import com.dxmap.indoornavig.MapDealUtils;
import com.dxmap.indoornavig.R;
import com.dxmap.indoornavig.test.ViewMapPos.OnMapLoadFinishListener;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.analysis.search.model.FMSearchModelByKeywordRequest;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMMapUpdateEvent;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class TestMapView implements OnFMMapInitListener, OnFMMapClickListener, OnFMMapUpdateEvent, OnClickListener {
	private View mView;

	private String mOffineMapName = "90295.fmap";
	private String mOffineThemeName = "2001.theme";
	private String mMapId = mOffineMapName.substring(0, mOffineMapName.lastIndexOf("."));
	private String mSetFloorName = "f1";
	private FMMapView mapView;
	private FMMap map;
	private LinearLayout mFloorLayout;
	private ScrollView mScrollView;
	private int mCurGroupId; // 焦点层
	private boolean mIsSingleLayer = true; // 是否单层
	private boolean mIs2D = true; // 是否2D
	private ImageView mBtn2D, mBtnLayer;
	private Context m_Context;
	private int[] mGids;
    private OnMapLoadFinishListener mFinishListener;
	public TestMapView(Context context) {
		// TODO Auto-generated constructor stub
		m_Context = context;
		mView = View.inflate(context, R.layout.view_map, null);
	//	mView.setVisibility(View.GONE);
		mBtn2D = (ImageView) mView.findViewById(R.id.btn_2d);
		mBtn2D.setOnClickListener(this);
		mBtnLayer = (ImageView) mView.findViewById(R.id.btn_layer);
		mBtnLayer.setOnClickListener(this);
		mFloorLayout = (LinearLayout) mView.findViewById(R.id.floorlayout);
		mScrollView = (ScrollView) mView.findViewById(R.id.scroll);
		mScrollView.setVisibility(View.GONE);

		String path = MapDealUtils.getDefaultPath(context);
		// 拷贝地图
		String mappath = path + mMapId + "/";
		MapDealUtils.writeMap(context, mappath, mOffineMapName, mOffineMapName);
		// 拷贝主题
		String themepath = path + "theme/";
		MapDealUtils.writeTheme(context, themepath, mOffineThemeName, mOffineThemeName);

		mapView = (FMMapView) mView.findViewById(R.id.mapview);
		map = (FMMap) mapView.getFMMap();
		// 地图id
		map.openMapByPath(mappath + mOffineMapName);
		map.loadThemeByPath(themepath + mOffineThemeName);
		// 打开场景
		// 必须设置地图初始化的回调
		map.setOnFMMapInitListener(this);
		map.setOnFMMapClickListener(this);
		map.setOnFMMapUpdateEvent(this); // 监听地图更新事件
	}
	
	public void setListener(OnMapLoadFinishListener listener){
		mFinishListener = listener;
	}

	public View getView() {
		return mView;
	}

	@Override
	public void onMapUpdate(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(float arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapInitFailure(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapInitSuccess(String arg0) {
		// TODO Auto-generated method stub
		map.hiddenCompass();
		// map.loadDefaultTheme(FMMap.DEFAULT_THEME_CANDY);
		// 场景配置
		FMMapInfo scene = map.getFMMapInfo();
		// 组（层）信息，可以简单理解为楼层
		if (scene == null)
			return;
		ArrayList<FMGroupInfo> groups = scene.getGroups();
		showFloorsLayout(groups);
		mFinishListener.onMapLoadSuccess(arg0);
	}

	private ArrayList<TextView> mTextViews;

	private void showFloorsLayout(ArrayList<FMGroupInfo> groups) {
		int floorNum = groups != null ? groups.size() : 0;
		if (floorNum == 0) {
			return;
		}
		mGids = new int[floorNum];
		mTextViews = new ArrayList<TextView>();
		// 有多少层
		mScrollView.setVisibility(View.VISIBLE);
		float scale = m_Context.getResources().getDisplayMetrics().scaledDensity;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 5, 5, 10);
		for (int i = 0; i < floorNum; i++) {
			int num = groups.get(i).getGroupId();
			mGids[i] = num;
			String name = groups.get(i).getGroupName();
			TextView textView = new TextView(m_Context);
			textView.setTextSize(7 * scale);
			textView.setText(name.toUpperCase());
			textView.setTextColor(m_Context.getResources().getColor(R.color.white));

			if (name.equals(mSetFloorName)) {
				mCurGroupId = num;
				textView.setTextColor(m_Context.getResources().getColor(R.color.text_bule));
			}
			textView.setTag(num);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int gid = (Integer) v.getTag();
					for (TextView view : mTextViews)
						view.setTextColor(m_Context.getResources().getColor(R.color.white));
					mCurGroupId = gid;
					if (mIsSingleLayer) {
						setSingleDisplay();
					} else
						setMultiDisplay();
					((TextView) v).setTextColor(m_Context.getResources().getColor(R.color.text_bule));
				}
			});
			mFloorLayout.addView(textView, 0, layoutParams);
			mTextViews.add(textView);
			if (i != floorNum - 1) {
				ImageView imageView = new ImageView(m_Context);
				imageView.setBackgroundColor(R.color.text_planend);
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 2);
				params.setMargins(10, 5, 5, 10);
				mFloorLayout.addView(imageView, 0, params);
			}
		}
		mScrollView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
		int[] gids = { mCurGroupId }; // 获取当前地图焦点层id
		map.setMultiDisplay(gids);
		/*
		 * if (mSetFMMapCoord != null){ map.setMapCenter(mCurGroupId,
		 * mSetFMMapCoord); map.setZoomLevel((float) (2/1E5)); }
		 */
		map.updateMap();
		/*
		 * for (int i = 0; i < groups.size(); i++) { FMModelLayer l =
		 * map.getFMLayerProxy().getFMModelLayer(groups.get(i).getGroupId());
		 * l.setOnFMNodeListener(this); }
		 */
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_2d) {
			if (mIs2D) {
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
				mBtn2D.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_3d));
				mIs2D = false;
			} else {
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
				mBtn2D.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_2d));
				mIs2D = true;
			}
			map.updateMap();
		} else if (v.getId() == R.id.btn_layer) {
			if (mIsSingleLayer) {
				mBtnLayer.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_layer_sel));
				setMultiDisplay();
				mIsSingleLayer = false;
			} else {
				mBtnLayer.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_layer));
				setSingleDisplay();
				mIsSingleLayer = true;
			}
			map.updateMap();
		}
	}

	// 多层/单层显示
	void setMultiDisplay() {
		int[] gids = map.getMapGroupIds(); // 获取地图所有的group
		map.setMultiDisplay(gids, mCurGroupId - 1);
		map.updateMap();
		// clearAndReDrawPath();
	}

	void setSingleDisplay() {
		int[] gids = { mCurGroupId }; // 获取当前地图焦点层id
		map.setMultiDisplay(gids);
		map.updateMap();
	}
}
