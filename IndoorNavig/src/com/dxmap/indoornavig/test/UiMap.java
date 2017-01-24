package com.dxmap.indoornavig.test;

import java.util.ArrayList;

import com.dxmap.indoornavig.BaseView;
import com.dxmap.indoornavig.MapDealUtils;
import com.dxmap.indoornavig.R;
import com.dxmap.indoornavig.R.color;
import com.dxmap.indoornavig.R.drawable;
import com.dxmap.indoornavig.R.id;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.FMPickMapCoordResult;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMMapUpdateEvent;
import com.fengmap.android.map.event.OnFMNodeListener;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.geometry.FMScreenCoord;
import com.fengmap.android.map.layer.FMModelLayer;
import com.fengmap.android.map.marker.FMLineMarker;
import com.fengmap.android.map.marker.FMNode;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class UiMap extends BaseView
		implements OnFMMapInitListener, OnFMMapClickListener, OnFMMapUpdateEvent, OnClickListener {
	/**
	 * 楼层单层多层切换监听接口
	 * 
	 * @author yangbowen
	 */
	public interface OnMapGroupSwitchListener {
		public void onMapGroupSwitch(boolean isSingle);
	}
	/**
	 * 加载地图成功后，调用此接口
	 * @author yangbowen
	 *
	 */
    public interface OnMapLoadSuccessListener{
    	public void onMapLoadSuccess();
    }

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
    private OnMapGroupSwitchListener mGroupSwitchListener;
    private OnMapLoadSuccessListener mLoadSuccessListener;
	@Override
	public boolean onCreateView(View view) {
		// TODO Auto-generated method stub
		super.onCreateView(view);
		mBtn2D = (ImageView) findViewById(R.id.btn_2d);
		mBtn2D.setOnClickListener(this);
		mBtnLayer = (ImageView) findViewById(R.id.btn_layer);
		mBtnLayer.setOnClickListener(this);
		mFloorLayout = (LinearLayout) findViewById(R.id.floorlayout);
		mScrollView = (ScrollView) findViewById(R.id.scroll);
		mScrollView.setVisibility(View.GONE);
		return true;
	}
	
	public void setOnMapGroupSwitchListener(OnMapGroupSwitchListener listener){
		mGroupSwitchListener = listener;
	}
	
	public void setOnMapLoadSuccessListener(OnMapLoadSuccessListener listener){
		mLoadSuccessListener = listener;
	}
	
	public FMMap getMap(){
		return map;
	}

	/**
	 * @param path
	 *            离线数据缓存在手机的目录
	 * @param fileName
	 *            文件名
	 **/
	public void initMap(String path) {
		if (path == null)
			path = MapDealUtils.getDefaultPath(m_Context);
		// 拷贝地图
		String mappath = path + mMapId + "/";
		MapDealUtils.writeMap(m_Context, mappath, mOffineMapName, mOffineMapName);
		// 拷贝主题
		String themepath = path + "theme/";
		MapDealUtils.writeTheme(m_Context, themepath, mOffineThemeName, mOffineThemeName);

		mapView = (FMMapView) findViewById(R.id.mapview);
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

	@Override
	public void onMapUpdate(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(float x, float y) {
		// TODO Auto-generated method stub
		Log.i("ClickscreenCoord", "x: "+x+"，y: "+y);
		//获取屏幕点击位置的地图坐标
		final FMPickMapCoordResult mapCoordResult = map.pickMapCoord(x, y);
		if(mapCoordResult == null)
			return;
		FMScreenCoord sCoord = new FMScreenCoord(x, y);
		FMMapCoord coord = map.toFMMapCoord(mCurGroupId, sCoord);
		Log.i("clientCoor:",coord.toString()+"   "+mCurGroupId);
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
		if(mLoadSuccessListener != null)
			mLoadSuccessListener.onMapLoadSuccess();
	}

	private ArrayList<TextView> mTextViews;

	private void showFloorsLayout(ArrayList<FMGroupInfo> groups) {
		int floorNum = groups != null ? groups.size() : 0;
		if (floorNum == 0) {
			return;
		}
		mTextViews = new ArrayList<TextView>();
		// 有多少层
		mScrollView.setVisibility(View.VISIBLE);
		float scale = m_Context.getResources().getDisplayMetrics().scaledDensity;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 5, 5, 10);
		for (int i = 0; i < floorNum; i++) {
			int num = groups.get(i).getGroupId();
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
		/*for (int i = 0; i < groups.size(); i++) {
			FMModelLayer l = map.getFMLayerProxy().getFMModelLayer(groups.get(i).getGroupId());
			l.setOnFMNodeListener(this);
		}*/
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
			if(mGroupSwitchListener != null)
				mGroupSwitchListener.onMapGroupSwitch(mIsSingleLayer);
			map.updateMap();
		}
	}

	/**
	 * 清理路径。 场景改变，若存在线，则需要画。
	 */
	/*
	 * void clearAndReDrawPath() { drawNaviPath(); }
	 * 
	 *//**
		 * 画导航路径
		 *//*
		 * void drawNaviPath() { FMLineMarker sceneLineMarker =
		 * MapDealUtils.clearAndReDrawLine(map,results, lineLayer, 8.0f,
		 * Color.RED); mNaviPrediction.preparePrediction(sceneLineMarker);
		 * map.updateMap(); }
		 */

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
		// clearAndReDrawPath();
	}
	
	@Override
	public void OnDestroy() {
		// TODO Auto-generated method stub
		map.onDestory();
		super.OnDestroy();
	}

}
