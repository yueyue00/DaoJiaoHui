package com.dxmap.indoornavig.test;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.dxmap.indoornavig.BaseView;
import com.dxmap.indoornavig.Configuration;
import com.dxmap.indoornavig.IndoorNaviView;
import com.dxmap.indoornavig.MainActivity;
import com.dxmap.indoornavig.MapDealUtils;
import com.dxmap.indoornavig.MapPosActivity;
import com.dxmap.indoornavig.R;
import com.dxmap.indoornavig.ShrinkView;
import com.dxmap.indoornavig.MapPosActivity.MySearchResultAdapter;
import com.dxmap.indoornavig.NaviSearchActivity;
import com.dxmap.indoornavig.R.id;
import com.dxmap.indoornavig.ShrinkView.OnShrinkOffsetListener;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMMapUpdateEvent;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.layer.FMImageLayer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class ViewMapPos implements OnFMMapInitListener, OnFMMapClickListener, OnFMMapUpdateEvent, OnClickListener ,OnItemClickListener,OnShrinkOffsetListener{
	/**
	 * 加载地图接口
	 */
	public interface OnMapLoadFinishListener{
		/**
		 * 加载成功
		 * @param path  路径
 		 */
		public void onMapLoadSuccess(String path);
		/**
		 * 加载失败
		 * @param path  路径
 		 */
		public void onMapLoadFaile(String path);
	}
	
	public interface OnMapGetResultListener{
		public void onGetResult(String name,FMMapCoord coord,int gid);
	}
	
	private String mOffineMapName = IndoorNaviView.mOffineMainMap;
    private String mOffineThemeName = IndoorNaviView.mOffineMainTheme;
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
    private String mKey;
    private long mType;
    private FMSearchAnalyser mFMSearchAnalyser;
    private int[] mGids;
    private ImageView mBtnBack;
    private ShrinkView mBtnShrink;
    private ListView mListView;
    private ArrayList<FMSearchResult> mResults;
    private ArrayList<FMSearchResult> mAllResults;
    private MySearchResultAdapter mAdapter; 
    private EditText mSearchEdit;
    private ArrayList<FMGroupInfo> groups;
    //分页
    private LinearLayout mPageLayout;
    private ImageView mPagePre,mPageNext;
    private TextView mPageText;
    private int mCurPage = 1;  //当前页数
    private int mTalPage = 0;  //总页数
    private int[] mImages = {R.drawable.water_a,R.drawable.water_b,R.drawable.water_c,R.drawable.water_d,R.drawable.water_e};
    private LinearLayout mShrinkView;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mShrinkBottom;
    private int mShrinkTop;
    private int mShrinkRight;
    private int mShrinkLeft;
    private int mLimtHeight;
    private int mMaxWidth;
	private LinearLayout mLayout;
    private Context mContext;
    private View mView;
    private OnMapLoadFinishListener mOnMapLoadFinishListener;
    private OnMapGetResultListener onMapGetResultListener;
    private boolean isHighlight = false;
    private NaviSearchActivity mNaviActivity;
    private TextView mTitle;
    private Configuration mConfiguration;
    private int mMapType = IndoorNaviView.MAP_TYPE_MAIN;
    private String mPath;
    private View mSearchView;
    public ViewMapPos(Context context) {
		// TODO Auto-generated constructor stub
    	mContext = context;
    	mView =  View.inflate(context, R.layout.activity_mappos, null);
    	mTitle = (TextView) mView.findViewById(R.id.title);
    	mBtn2D = (ImageView) mView.findViewById(R.id.btn_2d);
		mBtn2D.setOnClickListener(this);
		mBtn2D.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_3d));
		mBtnLayer = (ImageView) mView.findViewById(R.id.btn_layer);
		mBtnLayer.setOnClickListener(this);
		mBtnLayer.setVisibility(View.GONE);
		mFloorLayout = (LinearLayout) mView.findViewById(R.id.floorlayout);
		mScrollView = (ScrollView) mView.findViewById(R.id.scroll);
		mScrollView.setVisibility(View.GONE);
		mBtnShrink = (ShrinkView)mView.findViewById(R.id.btn_shrink);
		mBtnShrink.setOnClickListener(this);
		mBtnShrink.setListener(this);
		mBtnShrink.setVisibility(View.GONE);
		mBtnBack = (ImageView) mView.findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mPageLayout = (LinearLayout) mView.findViewById(R.id.pagelayout);
		mPageLayout.setVisibility(View.GONE);
		mPagePre = (ImageView) mView.findViewById(R.id.pagepre);
		mPagePre.setOnClickListener(this);
		mPageText = (TextView) mView.findViewById(R.id.page);
		mPageNext = (ImageView) mView.findViewById(R.id.pagenext);
		mPageNext.setOnClickListener(this);
		mSearchEdit = (EditText) mView.findViewById(R.id.search_edit);
		mSearchEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		mSearchEdit.setFocusable(false);;
		mSearchEdit.setOnClickListener(this);
		mShrinkView = (LinearLayout) mView.findViewById(R.id.layout_shrink);
		mResults = new ArrayList<FMSearchResult>();
		mAllResults = new ArrayList<FMSearchResult>();
		mListView = (ListView) mView.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mAdapter = new MySearchResultAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setVisibility(View.GONE);
		mapView = (FMMapView) mView.findViewById(R.id.mapview);
		map = (FMMap) mapView.getFMMap();
	//	init(path);
		/*Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		mKey = intent.getStringExtra("key");
		mType = intent.getLongExtra("type", 0);
		init(path);*/
	}
    
    /**
     * 加载地图前调用  设置是主会场还是分会场  默认主会场
     * @param type
     */
    public void setMapType(int type){
    	mMapType = type;
    }
    
    public void setMapFinishListener(OnMapLoadFinishListener listener){
    	mOnMapLoadFinishListener = listener;
    }
    
    public void setMapGetResultListener(OnMapGetResultListener listener){
    	onMapGetResultListener = listener;
    }
    
    public void setData(String key,long type){
    	mKey = key;
    	mType = type;
    }
    
    public void setHighlight(boolean ishighlight){
    	isHighlight = ishighlight;
    	mTitle.setVisibility(View.VISIBLE);
    	mSearchEdit.setVisibility(View.GONE);
    }
    
    public void setSearchView(View view){
    	mSearchView = view;
    }
    private ArrayList<String> mLightNos;
    public void setLightNo(ArrayList<String> nos){
    	mLightNos = nos;
    }
    
    /**
     * 亮点功能
     * @param list
     * @param searchAnalyser
     */
    public void setData(ArrayList<String> list,FMSearchAnalyser searchAnalyser,NaviSearchActivity activity){
    	mNaviActivity = activity;
    	mFMSearchAnalyser = searchAnalyser;
    	if(list.size() == 0)
    		return;
    	for(int i=0;i<list.size();i++){
    		mAllResults.addAll(MapDealUtils.getFMResultForFid(list.get(i), mGids, mFMSearchAnalyser));
    	}
    	/*for (int i = 0; i < mAllResults.size(); i++) {
			FMSearchResult result = mAllResults.get(i);
			result.put("no", mLightNos.get(i));
		}*/
    	//进行楼层排序
    	Collections.sort(mAllResults, new Comparator<FMSearchResult>(){

			@Override
			public int compare(FMSearchResult lhs, FMSearchResult rhs) {
				// TODO Auto-generated method stub
				return lhs.get("gid").toString().compareTo(rhs.get("gid").toString());
			}
    		
    	});
   // 	mAllResults = MapDealUtils.SortNo(mAllResults);
    	setPageText(mAllResults);
    	mBtnShrink.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.VISIBLE);
		mPageLayout.setVisibility(View.VISIBLE);
		mResults = getCurResult(mAllResults);
		mAdapter.notifyDataSetChanged();
		mView.setVisibility(View.VISIBLE);
		if(list.size() == 1){
			mCurGroupId = getGroupInfo(list.get(0)).getGroupId();
	    	setSingleDisplay();
	    	for (TextView view : mTextViews) {
				if ((Integer) view.getTag() == mCurGroupId) {
					view.setTextColor(mContext.getResources().getColor(R.color.text_bule));
				} else
					view.setTextColor(mContext.getResources().getColor(R.color.white));
			}
		}
		map.updateMap();
    }
    
    private String getMapPathFileName(int type){
    	if(mMapType == IndoorNaviView.MAP_TYPE_MAIN){
    		if(type == IndoorNaviView.MAP_FILEALLPATH)
    			return  mPath + IndoorNaviView.MAP_MAIN_FOLDER + IndoorNaviView.mOffineMainMap;
    		if(type == IndoorNaviView.MAP_FILEPARH)
    			return mPath + IndoorNaviView.MAP_MAIN_FOLDER;
    		if(type == IndoorNaviView.MAP_FILENAME)
    			return IndoorNaviView.mOffineMainMap;
		}else if(mMapType == IndoorNaviView.MAP_TYPE_SUB){
			if(type == IndoorNaviView.MAP_FILEALLPATH)
    			return  mPath + IndoorNaviView.MAP_SUB_FOLDER + IndoorNaviView.mOffineSubMap;
    		if(type == IndoorNaviView.MAP_FILEPARH)
    			return mPath + IndoorNaviView.MAP_SUB_FOLDER;
    		if(type == IndoorNaviView.MAP_FILENAME)
    			return IndoorNaviView.mOffineSubMap;
		}
    	return null;
    }
    
    private String getThemePathFileName(int type){
    	if(mMapType == IndoorNaviView.MAP_TYPE_MAIN){
    		if(type == IndoorNaviView.THEME_FILEALLPATH)
    			return  mPath + IndoorNaviView.THEME_MAIN_FOLDER + IndoorNaviView.mOffineMainTheme;
    		if(type == IndoorNaviView.THEME_FILEPARH)
    			return mPath + IndoorNaviView.THEME_MAIN_FOLDER;
    		if(type == IndoorNaviView.THEME_FILENAME)
    			return IndoorNaviView.mOffineMainTheme;
		}else if(mMapType == IndoorNaviView.MAP_TYPE_SUB){
			if(type == IndoorNaviView.THEME_FILEALLPATH)
    			return  mPath + IndoorNaviView.THEME_SUB_FOLDER + IndoorNaviView.mOffineSubTheme;
    		if(type == IndoorNaviView.THEME_FILEPARH)
    			return mPath + IndoorNaviView.THEME_SUB_FOLDER;
    		if(type == IndoorNaviView.THEME_FILENAME)
    			return IndoorNaviView.mOffineSubTheme;
		}
    	return null;
    }
    
    //填入关键字搜索数据
    public void setFMSearchResult(String key,ArrayList<FMSearchResult> list,FMSearchAnalyser searchAnalyser){
    	mFMSearchAnalyser = searchAnalyser;
    	mSearchEdit.setText(key);
    	mAllResults = list;
		setPageText(mAllResults);
		mBtnShrink.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.VISIBLE);
		mPageLayout.setVisibility(View.VISIBLE);
		mResults = getCurResult(mAllResults);
		mAdapter.notifyDataSetChanged();
		mView.setVisibility(View.VISIBLE);
		map.updateMap();
    }
    //填入展位搜索数据  可以直接定位相应楼层
    public void setFMSearchNumResult(String key,ArrayList<FMSearchResult> list,FMSearchAnalyser searchAnalyser){
    	setFMSearchResult(key, list, searchAnalyser);
    	mCurGroupId = getGroupInfo(list.get(0).get("fid").toString()).getGroupId();
    	setSingleDisplay();
    	for (TextView view : mTextViews) {
			if ((Integer) view.getTag() == mCurGroupId) {
				view.setTextColor(mContext.getResources().getColor(R.color.text_bule));
			} else
				view.setTextColor(mContext.getResources().getColor(R.color.white));
		}
    }
    
    public void init(String path) {
    	mPath = path;
		if (mPath == null)
			mPath = MapDealUtils.getDefaultPath(mContext);
		//拷贝地图
        MapDealUtils.writeMap(mContext, getMapPathFileName(IndoorNaviView.MAP_FILEPARH), getMapPathFileName(IndoorNaviView.MAP_FILENAME), getMapPathFileName(IndoorNaviView.MAP_FILENAME));
        //拷贝主题
        MapDealUtils.writeTheme(mContext, getThemePathFileName(IndoorNaviView.THEME_FILEPARH), getThemePathFileName(IndoorNaviView.THEME_FILENAME), getThemePathFileName(IndoorNaviView.THEME_FILENAME));
		// 地图id
		map.openMapByPath(getMapPathFileName(IndoorNaviView.MAP_FILEALLPATH));
		// 打开场景
		// 必须设置地图初始化的回调
		map.setOnFMMapInitListener(this);
		map.setOnFMMapClickListener(this);
		map.setOnFMMapUpdateEvent(this); // 监听地图更新事件
		
//		mView.setVisibility(View.GONE);
	}
    
    public FMMap getMap(){
    	return map;
    }
    
    public ArrayList<FMGroupInfo> getGroups(){
    	return groups;
    }
    
    public View getView(){
    	return mView;
    }
    
    public int[] getGids(){
    	return mGids;
    }
    
    private boolean isDown = false;
    private int offsetb ;
	@Override
	public boolean onShrinkOffset(int offset) {
		// TODO Auto-generated method stub
		offsetb = mShrinkView.getBottom() + offset;
		if (offsetb > mLimtHeight) {
				mBtnShrink.layout(mBtnShrink.getLeft(), mBtnShrink.getTop()+offset,mBtnShrink.getRight(),mBtnShrink.getBottom()+offset);
				mShrinkView.layout(mShrinkView.getLeft(), mShrinkView.getTop()+offset,mShrinkView.getRight(),mShrinkView.getBottom()+offset);
			
		}
		return true;
	}
	@Override
	public void onShrinkUp(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (offsetb > mLimtHeight) {
			int goneheight = mShrinkView.getMeasuredHeight() - mBtnShrink.getMeasuredHeight();
			if(!isDown){
				if (offsetb - mShrinkBottom < 150){
					mBtnShrink.layout(mShrinkLeft, mShrinkTop - goneheight, mBtnShrink.getRight(),
							mShrinkBottom - goneheight);
					mShrinkView.layout(mShrinkLeft, mShrinkTop, mShrinkRight,
							mShrinkBottom );
				}else{
					mBtnShrink.layout(mShrinkLeft, mShrinkBottom - mBtnShrink.getMeasuredHeight(), mBtnShrink.getRight(),
							mShrinkBottom);
					mShrinkView.layout(mShrinkLeft, mShrinkTop + goneheight, mShrinkRight,
							mShrinkBottom + goneheight);
					isDown= true;
				}
			}else{
				if (offsetb - mShrinkBottom > goneheight - 150){
					mBtnShrink.layout(mShrinkLeft, mShrinkBottom - mBtnShrink.getMeasuredHeight(), mBtnShrink.getRight(),
							mShrinkBottom);
					mShrinkView.layout(mShrinkLeft, mShrinkTop + goneheight, mShrinkRight,
							mShrinkBottom + goneheight);
				}else{
					mBtnShrink.layout(mShrinkLeft, mShrinkTop - goneheight, mBtnShrink.getRight(),
							mShrinkBottom - goneheight);
					mShrinkView.layout(mShrinkLeft, mShrinkTop, mShrinkRight,
							mShrinkBottom );
					isDown= false;
				}
			}
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		FMSearchResult res = mResults.get(position);
		if(isHighlight){
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName("com.smartdot.wenbo.huiyi", "com.gj.gaojiaohui.activity.DaHuiNaviActivity");
			intent.setComponent(cn);
			String fid = res.get("fid").toString();
			intent.putExtra("endfid", fid);
			intent.putExtra("mapType", "indoor");
			mNaviActivity.startActivity(intent);
			mNaviActivity.CloseActivity();
			return;
		}
		if(onMapGetResultListener != null){
			System.out.println("-------------------"+System.currentTimeMillis());
			String name = res.get("name").toString();
			String fid = res.get("fid").toString();
			FMMapCoord coord = MapDealUtils.getFMMapCoord(fid,mGids, mFMSearchAnalyser);
			if(coord == null)
				return;
			FMGroupInfo info = MapDealUtils.getGroupInfo(fid, getGroups(), mFMSearchAnalyser);
			onMapGetResultListener.onGetResult(name, coord, info.getGroupId());
		//	CloseView();
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_2d) {
			if (!mIs2D) {
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
				mBtn2D.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_3d));
				mIs2D = true;
			} else {
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
				mBtn2D.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_2d));
				mIs2D = false;
			}
			map.updateMap();
		} else if (v.getId() == R.id.btn_layer) {
			if (mIsSingleLayer) {
				mBtnLayer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_layer_sel));
				setMultiDisplay();
				mIsSingleLayer = false;
			} else {
				mBtnLayer.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_layer));
				setSingleDisplay();
				mIsSingleLayer = true;
			}
			map.updateMap();
		}else if(v.getId() == R.id.btn_back){
			if (!isHighlight)
				CloseView();
			else 
				mNaviActivity.CloseActivity();
		}else if(v.getId() == R.id.btn_shrink){
			/*if(mListView.getVisibility() == View.GONE){
				mListView.setVisibility(View.VISIBLE);
				mPageLayout.setVisibility(View.VISIBLE);
			}else if(mListView.getVisibility() == View.VISIBLE){
				mListView.setVisibility(View.GONE);
				mPageLayout.setVisibility(View.GONE);
			}*/
		}else if(v.getId() == R.id.pagepre){
			mCurPage --;
			setPageText(mAllResults);
			mResults = getCurResult(mAllResults);
			mAdapter.notifyDataSetChanged();
		}else if(v.getId() == R.id.pagenext){
			mCurPage ++;
			setPageText(mAllResults);
			mResults = getCurResult(mAllResults);
			mAdapter.notifyDataSetChanged();
		}else if(v.getId() == R.id.search_edit){
			if (!isHighlight)
				CloseView();
			else 
				mNaviActivity.CloseActivity();
		}
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
		if(mOnMapLoadFinishListener != null){
			mOnMapLoadFinishListener.onMapLoadFaile(arg0);
		}
	}
	@Override
	public void onMapInitSuccess(String arg0) {
		// TODO Auto-generated method stub
		if(map == null)
			return;
		map.hiddenCompass();
		map.loadThemeByPath(getThemePathFileName(IndoorNaviView.THEME_FILEALLPATH));
		map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
		// 场景配置
		FMMapInfo scene = map.getFMMapInfo();
		map.setMapScaleRange(IndoorNaviView.MAP_SCALE_MIN, IndoorNaviView.MAP_SCALE_MAX);
		// 组（层）信息，可以简单理解为楼层
		if (scene == null)
			return;
		groups = scene.getGroups();
		showFloorsLayout(groups);
		if(mOnMapLoadFinishListener != null){
			mOnMapLoadFinishListener.onMapLoadSuccess(arg0);
		}
		
	}
	
	/*private void Search(String key){
		mAllResults.clear();
		mResults.clear();
		mCurPage = 1;
		mTalPage = 0;
		mAllResults = SearchForKey(key);
		boolean is = setPageText(mAllResults);
		if(is){
			mListView.setVisibility(View.VISIBLE);
			mPageLayout.setVisibility(View.VISIBLE);
			mResults = getCurResult(mAllResults);
			mAdapter.notifyDataSetChanged();
		}else
			Toast.makeText(mContext, "当前搜索无结果", Toast.LENGTH_SHORT).show();
		
	}*/
	
	private void Search(long type){
		mAllResults.clear();
		mResults.clear();
		mCurPage = 1;
		mTalPage = 0;
		mAllResults = MapDealUtils.SearchForType(type, mGids, mFMSearchAnalyser);
		setPageText(mAllResults);
		mListView.setVisibility(View.VISIBLE);
		mPageLayout.setVisibility(View.VISIBLE);
		mResults = getCurResult(mAllResults);
		mAdapter.notifyDataSetChanged();
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
		float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 5, 5, 10);
		for (int i = 0; i < floorNum; i++) {
			int num = groups.get(i).getGroupId();
			mGids[i] = num;
			String name = groups.get(i).getGroupName();
			TextView textView = new TextView(mContext);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
			textView.setText(name.toUpperCase());
			textView.setTextColor(mContext.getResources().getColor(R.color.white));

			if (name.equals(mSetFloorName)) {
				mCurGroupId = num;
				textView.setTextColor(mContext.getResources().getColor(R.color.text_bule));
			}
			textView.setTag(num);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int gid = (Integer) v.getTag();
					for (TextView view : mTextViews)
						view.setTextColor(mContext.getResources().getColor(R.color.white));
					mCurGroupId = gid;
					if (mIsSingleLayer) {
						setSingleDisplay();
					} else
						setMultiDisplay();
					((TextView) v).setTextColor(mContext.getResources().getColor(R.color.text_bule));
				}
			});
			mFloorLayout.addView(textView, 0, layoutParams);
			mTextViews.add(textView);
			if (i != floorNum - 1) {
				ImageView imageView = new ImageView(mContext);
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

	// 多层/单层显示
	void setMultiDisplay() {
		int[] gids = map.getMapGroupIds(); // 获取地图所有的group
		map.setMultiDisplay(gids, mCurGroupId - 1);
		map.updateMap();
	}

	void setSingleDisplay() {
		int[] gids = { mCurGroupId }; // 获取当前地图焦点层id
		map.setMultiDisplay(gids);
		map.updateMap();
	}
	
	private ArrayList<FMSearchResult> SearchForKey(String key){
		return MapDealUtils.SearchForKey(key, mGids, mFMSearchAnalyser);
	}
	
	public class MySearchResultAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mResults.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mResults.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.view_search_list_item, null);
				holder = new ViewHolder();
				holder.mName = (TextView) convertView.findViewById(R.id.name);
				holder.mName.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
				holder.mGidName = (TextView) convertView.findViewById(R.id.group);
				holder.mGidName.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
				holder.mGidName.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			FMSearchResult result = (FMSearchResult) getItem(position);
			/*for(Object key : result.keySet()){
				System.out.println("key: "+key+"   value: "+result.get(key).toString());
			}*/
			String num = MapDealUtils.getIndexOfNo(position);
//			holder.mName.setText(num+"."+result.get("name").toString()+"("+result.get("no").toString()+")");
			holder.mName.setText(num+"."+result.get("name").toString());
			holder.mGidName.setText(getGroupInfo(result.get("fid").toString()).getGroupName().toUpperCase());
			return convertView;
		}
		
		
		class ViewHolder{
			public TextView mName;
			public TextView mGidName;
		}
	}
	
	private FMGroupInfo getGroupInfo(String fid){
		return MapDealUtils.getGroupInfo(fid, groups, mFMSearchAnalyser);
	}
	
	private FMMapCoord getFMMapCoord(String fid){
		return MapDealUtils.getFMMapCoord(fid, mGids, mFMSearchAnalyser);
	}
	
	private ArrayList<FMSearchResult> getCurResult(ArrayList<FMSearchResult> allresult){
		ArrayList<FMSearchResult> result = new ArrayList<FMSearchResult>();
		int i = 5*(mCurPage - 1);
		if(mCurPage == mTalPage){
			for (; i < allresult.size(); i++) {
				result.add(allresult.get(i));
			}
		}else{
			for (; i < 5*mCurPage; i++) {
				result.add(allresult.get(i));
			}
		}
		addImageLayer(result);
		mShrinkView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mShrinkBottom = mShrinkView.getBottom();
				mLimtHeight = mShrinkView.getBottom();
				mShrinkTop = mShrinkView.getTop();
				mShrinkRight = mShrinkView.getRight();
				mShrinkLeft = mShrinkView.getLeft();
			}
		});
		return result;
	}
	
	private boolean setPageText(ArrayList<FMSearchResult> allresult){
		int size = allresult.size();
		if(size == 0)
			return false;
		if(mTalPage == 0){
			if(size <= 5)
				mTalPage = 1;
			else if(size%5 == 0)
				mTalPage = size/5;
			else if(size%5 > 0)
				mTalPage = size/5 + 1;
		}
		mPageText.setText(mCurPage+"/"+mTalPage);
		mPagePre.setEnabled(true);
		mPageNext.setEnabled(true);
		if(mCurPage == 1)
			mPagePre.setEnabled(false);
		if(mTalPage == 1||mCurPage == mTalPage)
			mPageNext.setEnabled(false);
		return true;
	}
	
	private void addImageLayer(ArrayList<FMSearchResult> result){
		map.removeAll();
		for (int i = 0; i < result.size(); i++) {
			String fid = result.get(i).get("fid").toString();
			FMImageLayer imageLayer = map.getFMLayerProxy().createFMImageLayer(getGroupInfo(fid).getGroupId());
			map.addLayer(imageLayer);
			MapDealUtils.addImageMarker(imageLayer, getFMMapCoord(fid), mImages[i], 30);	
		}
	}
	public void CloseView(){
		mAllResults.clear();
		mResults.clear();
		mCurPage = 1;
		mTalPage = 0;
		mSearchEdit.setText("");
		map.removeAll();
		mBtnShrink.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		mPageLayout.setVisibility(View.GONE);
		mSearchView.setVisibility(View.VISIBLE);
	}
	
	public void onDestoryMapView(){
		
        if(mFMSearchAnalyser != null){		
        	mFMSearchAnalyser.release();
        	mFMSearchAnalyser = null;
        }
		
		if(map != null){
			map.onDestory();
			map = null;
		}
	}
}
