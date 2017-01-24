package com.dxmap.indoornavig;

import java.io.Serializable;
import java.util.ArrayList;

import com.dxmap.indoornavig.ExecuteNaviTask.OnNaviTaskResultListener;
import com.dxmap.indoornavig.NaviSearchView.OnNaviSearchResultListener;
import com.dxmap.indoornavig.R.color;
import com.dxmap.indoornavig.test.MapView;
import com.dxmap.indoornavig.test.TestPoint;
import com.fengmap.android.FMErrorMsg;
import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.navi.FMNaviModule;
import com.fengmap.android.analysis.navi.FMNaviPrediction;
import com.fengmap.android.analysis.navi.FMNaviPrediction.OnFMPrepareNavigationResourcesListener;
import com.fengmap.android.analysis.navi.FMNaviResult;
import com.fengmap.android.analysis.navi.FMPredictionInfo;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.analysis.search.model.FMSearchModelByCircleRequest;
import com.fengmap.android.analysis.search.model.FMSearchModelByIdRequest;
import com.fengmap.android.data.FMDataManager;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.FMNodeInfoWindow;
import com.fengmap.android.map.FMPickMapCoordResult;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMMapUpdateEvent;
import com.fengmap.android.map.event.OnFMNodeListener;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.geometry.FMScreenCoord;
import com.fengmap.android.map.layer.FMImageLayer;
import com.fengmap.android.map.layer.FMLineLayer;
import com.fengmap.android.map.layer.FMModelLayer;
import com.fengmap.android.map.marker.FMImageMarker;
import com.fengmap.android.map.marker.FMLineMarker;
import com.fengmap.android.map.marker.FMModel;
import com.fengmap.android.map.marker.FMNode;
import com.fengmap.android.map.marker.FMSegment;
import com.fengmap.android.map.style.FMImageMarkerStyle;
import com.fengmap.android.map.style.FMLineMarkerStyle;
import com.fengmap.android.utils.FMLog;

import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.SynthesisCallback;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class IndoorNaviView extends BaseView implements OnClickListener, OnFMMapInitListener, OnFMMapClickListener, OnFMNodeListener,
		OnFMPrepareNavigationResourcesListener,OnFMMapUpdateEvent,OnNaviTaskResultListener{

//	private ViewGroup mView;
	private FMMapView mapView;
	private FMMap map;
	// 当前楼层
	private int mCurGroupId = 0;
	private FMModel lastModel;
    private ImageView mBtn2D,mBtnLayer;
    private TextView mBtnMain,mBtnSub;
 //   private Context m_Context;
    
    private ImageView mImgStart,mImgEnd,mImgChange;
    private TextView mTxtPlanChear,mTxtPlanNavi;
    private EditText mEditStart,mEditEnd;
    
    private boolean mIsStart = false;  //是否为选择起点
    private boolean mIsEnd = false;   // 是否为选择终点
    private boolean mIsNaviFinish = false;  //是否规划完成
    private FMMapCoord mStartPt,mEndPt;	
    private int startGroupId,endGroupId;          //起点终点所在组Id
    private FMImageLayer mlForNaviStart;    //标注物图层，起点
    private FMImageLayer mlForNaviEnd;      //标注物图层，终点
    private FMLineLayer lineLayer;                //线图层
    private FMNaviAnalyser naviAnalyser;
    private FMSearchAnalyser mSearchAnalyser;
    private FMNaviPrediction mNaviPrediction;
    private ArrayList<FMNaviResult> results = new ArrayList<FMNaviResult>();
    private TextNaviView mTextNaviView;
    //楼层
    private int[] gids;
    private LinearLayout mFloorLayout;
    private ScrollView mScrollView;
    
    public static final int TAG_START = 1;
    public static final int TAG_END = 2;
    private boolean mIs2D = true;
    private boolean mIsSingleLayer = true;
    public static final String mOffineMainMap = "90295_m_v8.fmap";
    public static final String mOffineSubMap = "90312_s_v1.fmap";
    public static final String mOffineMainTheme = "2001_m_v2.theme";
    public static final String mOffineSubTheme = "2002_s_v1.theme";
    public static final String mOffineThemeZip = "theme.zip";
    public static final int MAP_FILEALLPATH = 1;  //地图全路径
    public static final int MAP_FILEPARH = 2;  //地图路径
    public static final int MAP_FILENAME = 3;  //地图文件名
    public static final int THEME_FILEALLPATH = 4;  //主题全路径
    public static final int THEME_FILEPARH = 5;  //主题路径
    public static final int THEME_FILENAME = 6;  //主题文件名
    private String mOffineMapName = mOffineMainMap;
    private String mOffineThemeName = mOffineMainTheme;
    private String mMapId = mOffineMapName.substring(0,mOffineMapName.lastIndexOf("."));
    private String mSetFloorName = "f1";
    private FMMapCoord mSetFMMapCoord ;
    private ViewGroup mGroup;
    private ProgressDialog mDialog;
    private String mPath;
    private String mStartFid;
    private String mEndFid;
    private ArrayList<String> mFids;
    private ArrayList<String> mWordResults;
    private LinearLayout mClearLayout,mPlanLayout;
    final int RESULT_CODE=101;
    final int REQUEST_CODE=1;
    private Configuration mConfiguration;
    public static final int MAP_TYPE_MAIN = 0; //主会场
    public static final int MAP_TYPE_SUB = 1; //分会场
    public static final float MAP_SCALE_MIN = 1.0f;
    public static final float MAP_SCALE_MAX = 24.0f;
    //主会场地图文件目录
    public static final String MAP_MAIN_FOLDER = "mainmap/";
    //主会场主题文件目录
    public static final String THEME_MAIN_FOLDER = "maintheme/";
    //分会场地图文件目录
    public static final String MAP_SUB_FOLDER = "submap/";
    //分会场主题文件目录
    public static final String THEME_SUB_FOLDER = "subtheme/";
    private int mMapType = MAP_TYPE_MAIN;
    private ArrayList<FMGroupInfo> groups ;
    private boolean isSetStart = false;   //是否设置了起点
    private boolean isSetEnd = false;  //是否设置了终点
    private ExecuteNaviTask mNaviTask;
    private FMNaviAnalyser mMainNaviAnalyser,mSubNaviAnalyser;
    private FMSearchAnalyser mMainSearchAnalyser,mSubSearchAnalyser;
    @Override
    public boolean onCreateView(View view) {
    	// TODO Auto-generated method stub
    	super.onCreateView(view);
		//mView = (ViewGroup) View.inflate(context, R.layout.view_indoorplan, null);
		mBtn2D = (ImageView) findViewById(R.id.btn_2d);
		mBtn2D.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_3d));
		mBtn2D.setOnClickListener(this);
		mBtnMain = (TextView) findViewById(R.id.btn_main);
		mBtnMain.setOnClickListener(this);
		mBtnSub = (TextView) findViewById(R.id.btn_sub);
		mBtnSub.setOnClickListener(this);
		mBtnLayer = (ImageView) findViewById(R.id.btn_layer);
		mBtnLayer.setOnClickListener(this);
		mBtnLayer.setVisibility(View.GONE);
		mImgStart = (ImageView) findViewById(R.id.img_start);
		mImgStart.setOnClickListener(this);
		mImgEnd = (ImageView) findViewById(R.id.img_end);
		mImgEnd.setOnClickListener(this);
		mImgChange = (ImageView) findViewById(R.id.btn_change);
		mImgChange.setOnClickListener(this);		
		mTxtPlanChear = (TextView) findViewById(R.id.text_palnclear);
		mTxtPlanChear.setOnClickListener(this);
		mTxtPlanNavi = (TextView) findViewById(R.id.text_palnvavi);
		mTxtPlanNavi.setOnClickListener(this);
		mEditStart = (EditText) findViewById(R.id.edit_start);
		mEditStart.setOnClickListener(this);
		mEditEnd = (EditText) findViewById(R.id.edit_end);
		mEditEnd.setOnClickListener(this);
		mFloorLayout = (LinearLayout) findViewById(R.id.floorlayout);
		mScrollView = (ScrollView) findViewById(R.id.scroll);
		mScrollView.setVisibility(View.GONE);
		mClearLayout = (LinearLayout) findViewById(R.id.layout_palnclear);
		mClearLayout.setOnClickListener(this);
		mPlanLayout = (LinearLayout) findViewById(R.id.layout_palnvavi);
		mPlanLayout.setOnClickListener(this);
		
		//加载配置文件
    	mConfiguration = new Configuration("/MainConfiguration.properties");
    	mapView = (FMMapView) findViewById(R.id.mapview); 
    	map = (FMMap) mapView.getFMMap();
    	
    	mNaviTask = new ExecuteNaviTask();
    	mNaviTask.setListener(this);
    	return true;
    }
    
  /**
    * @param path  离线数据缓存在手机的目录
    * @param fileName  文件名
    **/
    public void initMap(String path){
    	mDialog = ProgressDialog.show(m_Context, null, "正在加载地图...");
    	mPath = path;
    	if(mPath == null)
    		mPath = MapDealUtils.getDefaultPath(m_Context);
    	//拷贝地图
        MapDealUtils.writeMap(m_Context, getMapPathFileName(MAP_FILEPARH), getMapPathFileName(MAP_FILENAME), getMapPathFileName(MAP_FILENAME));
        //拷贝主题
        MapDealUtils.writeTheme(m_Context, getThemePathFileName(THEME_FILEPARH), getThemePathFileName(THEME_FILENAME), getThemePathFileName(THEME_FILENAME));
		//地图path
		map.openMapByPath(getMapPathFileName(MAP_FILEALLPATH));
		//打开场景 
		// 必须设置地图初始化的回调
		map.setOnFMMapInitListener(this);
		map.setOnFMMapClickListener(this);
		map.setOnFMMapUpdateEvent(this);     //监听地图更新事件
		// 文字导航
		mNaviPrediction = new FMNaviPrediction(this);
    }
    
    public FMNaviAnalyser getMapNaviAnalyser(){
    	if(mMapType == MAP_TYPE_MAIN){
    		if(mMainNaviAnalyser == null)
    			mMainNaviAnalyser = FMNaviAnalyser.init(map);
    		return mMainNaviAnalyser;
    	}else {
    		if(mSubNaviAnalyser == null)
    			mSubNaviAnalyser = FMNaviAnalyser.init(map);
    		return mSubNaviAnalyser;
    	}
    }
    
    public FMSearchAnalyser getMapSearchAnalyser(){
    	if(mMapType == MAP_TYPE_MAIN){
    		if(mMainSearchAnalyser == null)
    			mMainSearchAnalyser = FMSearchAnalyser.init(map);
    		return mMainSearchAnalyser;
    	}else {
    		if(mSubSearchAnalyser == null)
    			mSubSearchAnalyser = FMSearchAnalyser.init(map);
    		return mSubSearchAnalyser;
    	}
    }
    
    /**
     * 加载地图前调用  设置是主会场还是分会场  默认主会场
     * @param type
     */
    public void setMapType(int type){
    	mMapType = type;
    	if(type == IndoorNaviView.MAP_TYPE_SUB){
    		mBtnMain.setTextColor(m_Context.getResources().getColor(R.color.white));
    		mBtnSub.setTextColor(m_Context.getResources().getColor(R.color.text_bule));
    	}else{
    		mBtnMain.setTextColor(m_Context.getResources().getColor(R.color.text_bule));
    		mBtnSub.setTextColor(m_Context.getResources().getColor(R.color.white));
    	}
    }
    
    public int getMapType(){
    	return mMapType;
    }
    
    
    private String getMapPathFileName(int type){
    	if(mMapType == IndoorNaviView.MAP_TYPE_MAIN){
    		if(type == MAP_FILEALLPATH)
    			return  mPath + MAP_MAIN_FOLDER + mOffineMainMap;
    		if(type == MAP_FILEPARH)
    			return mPath + MAP_MAIN_FOLDER;
    		if(type == MAP_FILENAME)
    			return mOffineMainMap;
		}else if(mMapType == IndoorNaviView.MAP_TYPE_SUB){
			if(type == MAP_FILEALLPATH)
    			return  mPath + MAP_SUB_FOLDER + mOffineSubMap;
    		if(type == MAP_FILEPARH)
    			return mPath + MAP_SUB_FOLDER;
    		if(type == MAP_FILENAME)
    			return mOffineSubMap;
		}
    	return null;
    }
    
    private String getThemePathFileName(int type){
    	if(mMapType == IndoorNaviView.MAP_TYPE_MAIN){
    		if(type == THEME_FILEALLPATH)
    			return  mPath + THEME_MAIN_FOLDER + mOffineMainTheme;
    		if(type == THEME_FILEPARH)
    			return mPath + THEME_MAIN_FOLDER;
    		if(type == THEME_FILENAME)
    			return mOffineMainTheme;
		}else if(mMapType == IndoorNaviView.MAP_TYPE_SUB){
			if(type == THEME_FILEALLPATH)
    			return  mPath + THEME_SUB_FOLDER + mOffineSubTheme;
    		if(type == THEME_FILEPARH)
    			return mPath + THEME_SUB_FOLDER;
    		if(type == THEME_FILENAME)
    			return mOffineSubTheme;
		}
    	return null;
    }
    
    /**
     * 设置起始点的fid
     * @param fid
     */
	public void setStartPos(String num){
		isSetStart = true;
		mStartFid = mConfiguration.getValue(num);
	}
	/**
	 * 设置终点的fid
	 * @param fid
	 */
	public void setEndPos(String num){
		isSetEnd = true;
		mEndFid = mConfiguration.getValue(num);
	}
	/**
	 * 设置亮点模式的终点
	 * @param fid
	 */
	public void setHigthlightEndPos(String fid){
		mEndFid = fid;
	}
	
    /**
     * 设置多个终点
     * @param fids
     */
	private void setManyPos(ArrayList<String> fids){
		mFids = fids;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_2d){
			if(!mIs2D){
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D); 
				mBtn2D.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_3d));
				mIs2D = true;
			}else{
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_3D); 
				mBtn2D.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_2d));
				mIs2D = false;
			}
			map.updateMap();
		}else if(v.getId() == R.id.img_start){
			mIsStart = true;
			mIsEnd = false;
			mImgStart.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_ding_sel));
			mImgEnd.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_ding));
			Toast.makeText(m_Context, "请选择起点", Toast.LENGTH_SHORT).show();
		}else if(v.getId() == R.id.img_end){
			mIsEnd = true;
			mIsStart = false;
			mImgStart.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_ding));
			mImgEnd.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_ding_sel));
			Toast.makeText(m_Context, "请选择终点", Toast.LENGTH_SHORT).show();
		}else if(v.getId() == R.id.text_palnclear){
			clear();
			mStartPt = null;
			mEndPt = null;
			mEditStart.setText("");
			mEditEnd.setText("");
		}else if(v.getId() == R.id.layout_palnclear){
			clear();
			mStartPt = null;
			mEndPt = null;
			mEditStart.setText("");
			mEditEnd.setText("");
		}else if(v.getId() == R.id.text_palnvavi){
			//计算
			if(mStartPt!=null && mEndPt != null) {
		//		anlyzeNavi(startGroupId, mStartPt, endGroupId, mEndPt);
				ToTextNaviActivity();
		//		mIsNaviFinish = false;
			}else
				Toast.makeText(m_Context, "请完善起点或终点信息", Toast.LENGTH_SHORT).show();
		}else if(v.getId() == R.id.layout_palnvavi){
			//计算
			if(mStartPt!=null && mEndPt != null) {
		//		anlyzeNavi(startGroupId, mStartPt, endGroupId, mEndPt);
				ToTextNaviActivity();
		///		mIsNaviFinish = false;
			}else
				Toast.makeText(m_Context, "请完善起点或终点信息", Toast.LENGTH_SHORT).show();
		}else if(v.getId() == R.id.edit_start){
			ToSearchNaviActivity(TAG_START);
		}else if(v.getId() == R.id.edit_end){
			ToSearchNaviActivity(TAG_END);
		}else if(v.getId() == R.id.btn_layer){
			if(mIsSingleLayer){
				mBtnLayer.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_layer_sel));
				setMultiDisplay();
				mIsSingleLayer = false;
			}else{
				mBtnLayer.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_layer));
				setSingleDisplay();
				mIsSingleLayer = true;
			}
			map.updateMap();
		}else if(v.getId() == R.id.btn_change){
			ChangePlan();
		}else if(v.getId() == R.id.btn_main){
			openMap(MAP_TYPE_MAIN);
		}else if(v.getId() == R.id.btn_sub){
			openMap(MAP_TYPE_SUB);
		}
	}
	//打开地图
	private void openMap(int type){
		if(mMapType != type){
			mDialog = ProgressDialog.show(m_Context, null, "正在加载地图...");
			clear();
			mStartPt = null;
			mEndPt = null;
			mEditStart.setText("");
			mEditEnd.setText("");
			mStartFid = null;
			mEndFid = null;
			setMapType(type);
			//拷贝地图
	        MapDealUtils.writeMap(m_Context, getMapPathFileName(MAP_FILEPARH), getMapPathFileName(MAP_FILENAME), getMapPathFileName(MAP_FILENAME));
	        //拷贝主题
	        MapDealUtils.writeTheme(m_Context, getThemePathFileName(THEME_FILEPARH), getThemePathFileName(THEME_FILENAME), getThemePathFileName(THEME_FILENAME));
			map.openMapByPath(getMapPathFileName(MAP_FILEALLPATH));
		}
	}
	
	//多层/单层显示
    void setMultiDisplay() {
        int[] gids = map.getMapGroupIds();    //获取地图所有的group
        map.setMultiDisplay(gids, mCurGroupId-1);
        map.updateMap();
        clearAndReDrawPath();
    }
    void setSingleDisplay() {
        int[] gids = { mCurGroupId };       //获取当前地图焦点层id
        map.setMultiDisplay(gids);
        map.updateMap();
        clearAndReDrawPath();
    }
    
    private void ToTextNaviActivity(){
    	if(mWordResults == null || mWordResults.size() == 0)
    		return;
    	Intent intent = new Intent(m_Context,TextNaviActivity.class);
		intent.putStringArrayListExtra("data", mWordResults);
		intent.putExtra("caretag", CareTag);
		m_Context.startActivity(intent);
		((Activity)m_Context).overridePendingTransition(R.anim.textnavi_open,0);
    }
    
    private void ToSearchNaviActivity(int tag){
    	Intent intent = new Intent(m_Context,NaviSearchActivity.class);
    	intent.putExtra("tag", tag);
    	intent.putExtra("gids", gids);
    	intent.putExtra("path", mPath);
    	intent.putExtra("maptype", mMapType);
    	((Activity)m_Context).startActivityForResult(intent, REQUEST_CODE);
    }
    
	
	public void onDestoryMap(){
		if(mSearchAnalyser != null){
			mSearchAnalyser.release();
			mSearchAnalyser = null;
		}
		if(mNaviPrediction != null){
			mNaviPrediction.release();
			mNaviPrediction = null;
		}
		if(naviAnalyser != null){
			naviAnalyser.release();
			naviAnalyser = null;
		}
		if(map != null){
			map.onDestory();
			map = null;
		}
	}
	
	@Override
	public void onMapInitFailure(String path, int errCode) {
		// TODO Auto-generated method stub
		mBtnMain.setEnabled(true);
		if(mDialog != null)
			mDialog.dismiss();
		Toast.makeText(m_Context, FMErrorMsg.getErrorMsg(errCode), Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onMapInitSuccess(String arg0) {
		// TODO Auto-generated method stub
		
		map.hiddenCompass(); 
		
 		map.loadThemeByPath(getThemePathFileName(THEME_FILEALLPATH));
	//	map.loadDefaultTheme(FMMap.DEFAULT_THEME_CANDY);
		map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
		// 场景配置
		FMMapInfo scene = map.getFMMapInfo();
		map.setMapScaleRange(MAP_SCALE_MIN, MAP_SCALE_MAX);
		// 组（层）信息，可以简单理解为楼层
		if(scene == null)
			return;
		groups = scene.getGroups();
		
		showFloorsLayout(groups);
		
		lineLayer = map.getFMLayerProxy().getFMLineLayer();
    	map.addLayer(lineLayer);          //将图层添加到地图
    	naviAnalyser = getMapNaviAnalyser();
		mSearchAnalyser = getMapSearchAnalyser();
	//	setTest();
	//	setStartFid("990950164");
		
		if(mStartFid != null){
			ArrayList<FMSearchResult> list = MapDealUtils.getFMResultForFid(mStartFid, gids, mSearchAnalyser);
			if(list.size() > 0){
				FMSearchResult result = list.get(0);
			    FMMapCoord coord = MapDealUtils.getFMMapCoord(mStartFid, gids, mSearchAnalyser);
			    if(coord == null){
			    	Toast.makeText(m_Context, "未搜索到结果", Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    FMGroupInfo info = MapDealUtils.getGroupInfo(mStartFid, groups, mSearchAnalyser);
			    String name = result.get("name").toString();
			    addStartPos(name, coord, info.getGroupId());
			    mCurGroupId = info.getGroupId();
			    TurnFloor();
			}
		}else {
			if(isSetStart){
				Toast.makeText(m_Context, "未搜索到结果", Toast.LENGTH_SHORT).show();
			}
		}
		
		if(mEndFid != null){
			ArrayList<FMSearchResult> list = MapDealUtils.getFMResultForFid(mEndFid, gids, mSearchAnalyser);
			if(list.size() > 0){
				FMSearchResult result = list.get(0);
			    FMMapCoord coord = MapDealUtils.getFMMapCoord(mEndFid, gids, mSearchAnalyser);
			    if(coord == null){
			    	Toast.makeText(m_Context, "未搜索到结果", Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    FMGroupInfo info = MapDealUtils.getGroupInfo(mEndFid, groups, mSearchAnalyser);
			    String name = result.get("name").toString();
				addEndPos(name, coord, info.getGroupId());
				mCurGroupId = info.getGroupId();
				TurnFloor();
			}
		}else {
			if(isSetEnd){
				Toast.makeText(m_Context, "未搜索到结果", Toast.LENGTH_SHORT).show();
			}
		}
		isSetStart = false;
		isSetEnd = false;
		if(mStartPt!=null && mEndPt != null) {
			anlyzeNavi(startGroupId, mStartPt, endGroupId, mEndPt);
			/*//跳转到起点的楼层，并设为中心点及合适的缩放级别
			mCurGroupId = startGroupId;
			TurnFloor();
			map.setMapCenter(startGroupId, mStartPt);
			map.setZoomLevel((float) (2/1E5));*/
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mBtnMain.setEnabled(true);
				if(mDialog != null)
					mDialog.dismiss();
			}
		}, 1500);
	}
	
	private void TurnFloor() {
		setSingleDisplay();
		for (TextView view : mTextViews) {
			if ((Integer) view.getTag() == mCurGroupId) {
				view.setTextColor(m_Context.getResources().getColor(R.color.text_bule));
			} else
				view.setTextColor(m_Context.getResources().getColor(R.color.white));
		}
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
	public boolean onClick(FMNode marker) {
		// TODO Auto-generated method stub
		FMModel model = (FMModel) marker;
		if(mIsStart){
			mImgStart.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_ding));
			addStartPos(model.getName(), model.getCenterMapCoord(), model.getGroupId());
		}
		
		if(mIsEnd){
			mImgEnd.setImageDrawable(m_Context.getResources().getDrawable(R.drawable.icon_ding));
			addEndPos(model.getName(), model.getCenterMapCoord(), model.getGroupId());
		}
		if(mIsEnd == true || mIsStart == true){
			if(mStartPt!=null && mEndPt != null) {
				anlyzeNavi(startGroupId, mStartPt, endGroupId, mEndPt);
				/*//跳转到起点的楼层，并设为中心点及合适的缩放级别
				mCurGroupId = startGroupId;
				TurnFloor();
				map.setMapCenter(startGroupId, mStartPt);
				map.setZoomLevel((float) (2/1E5));*/
			}
		}
		mIsStart = false;
		mIsEnd = false;
		
		/*mCurGroupId = model.getGroupId();
		if(map.getDisplayGroupIds().length > 1){
			map.setMultiDisplay(gids, mCurGroupId-1);
		}*/
		Log.i("clientCoor:",model.getCenterMapCoord().toString()+"   "+mCurGroupId);
		Log.i("modelfid:",model.getFid()+"   "+mCurGroupId);
	//	map.updateMap();
		return true;        							//图层事件消耗，不再传给Map处理了
	}
	@Override
	public boolean onLongPress(FMNode arg0) {
		// TODO Auto-generated method stub
	//	Toast.makeText(m_Context, "模型长按事件", Toast.LENGTH_LONG).show();
		return true;
	}

	/**
   	 * 在指定位置添加标注物到指定的图层。
   	 * @param layer 图层
   	 * @param point 地图坐标
   	 * @return      标注物
   	 */
	private FMImageMarker addMarker(FMImageLayer layer, FMMapCoord point, FMImageMarkerStyle style) {
		//创建标注物
		FMImageMarker marker = new FMImageMarker(point);
		marker.setStyle(style);
		//指针对象
		long handle = layer.addMarker(marker);
		map.updateMap();
		if(handle <= 0)
			return null;
		return marker;
   	}
	
	private void addStartLayer(){
		//添加起点图层
		if(mlForNaviStart != null){
			mlForNaviStart.removeAll();
			map.removeLayer(mlForNaviStart);
			mlForNaviStart = null;
		}
		mlForNaviStart = new FMImageLayer(map, startGroupId);
		map.addLayer(mlForNaviStart);
		//标注物样式
		FMImageMarkerStyle style = new FMImageMarkerStyle();           //图片名字
		style.setImageFromAssets("ico_start.png");
		//设置标注物位于模型之上，默认就是位于地面之上
//		style.setFMMarkerOffsetType(FMMarkerOffsetType.FMMARKER_EXTENT_ABOVE);
		addMarker(mlForNaviStart, mStartPt,style);
	}
	
	private void addEndLayer(){
		//添加终点图层
		if(mlForNaviEnd != null){
			mlForNaviEnd.removeAll();
			map.removeLayer(mlForNaviEnd);
			mlForNaviEnd = null;
		}
		mlForNaviEnd = new FMImageLayer(map, endGroupId);
		map.addLayer(mlForNaviEnd);

		//标注物样式
		FMImageMarkerStyle style = new FMImageMarkerStyle();            //图片名字
		style.setImageFromAssets("ico_end.png");
		addMarker(mlForNaviEnd, mEndPt,style);
	}
	
	private void ChangePlan(){
		String temp = mEditStart.getText().toString();
		mEditStart.setText(mEditEnd.getText().toString());
		mEditEnd.setText(temp);
		
		int tempgroup = startGroupId;
		startGroupId = endGroupId ;
		endGroupId = tempgroup;
		
		FMMapCoord tempCoor = mStartPt;
		mStartPt = mEndPt;
		mEndPt = tempCoor;
		
		clear();
		
		//标注物样式
		if(mStartPt != null){
			addStartLayer();
		}
		
		//标注物样式
		if(mEndPt != null){
			addEndLayer();
		}
		
		if(mStartPt!=null && mEndPt != null) {
			anlyzeNavi(startGroupId, mStartPt, endGroupId, mEndPt);
			/*//跳转到起点的楼层，并设为中心点及合适的缩放级别
			mCurGroupId = startGroupId;
			TurnFloor();
			map.setMapCenter(startGroupId, mStartPt);
			map.setZoomLevel((float) (2/1E5));*/
		}
			
	}
	
	private void clear() {
		//清除所有线
		if(lineLayer !=null) {
			lineLayer.removeAll();
		}
		//清除标注物
		if(mlForNaviStart!=null) {
			mlForNaviStart.removeAll();
			map.removeLayer(mlForNaviStart);  //移除图层
		}
		if(mlForNaviEnd!=null) {
			mlForNaviEnd.removeAll();
			map.removeLayer(mlForNaviEnd);    //移除图层
		}
		if(results != null)
			results.clear();
		map.updateMap();
	}

	
	/**
     * 导航分析。
     * @param startGroupId 起点所在层
     * @param startPt      起点坐标
     * @param endGroupId   终点所在层
     * @param endPt        终点坐标
     */
    private void anlyzeNavi(int startGroupId, FMMapCoord startPt, int endGroupId, FMMapCoord endPt) {
    	mDialog = ProgressDialog.show(m_Context, null, "正在导航...");
    	mNaviTask.ExecuteNavi(map, naviAnalyser, startGroupId, startPt, endGroupId, endPt);
	}
    private boolean isCare = false;
    private int CareTag = -1;
	@Override
	public void onLoadCompleted() {
		// TODO Auto-generated method stub
		ArrayList<FMPredictionInfo> arrayList = mNaviPrediction.performStaticPrediction();
		// if(mIsNaviFinish){
		int size = arrayList != null ? arrayList.size() : 0;
		System.out.println("TextNaviResultSize == " + size);
		if (size == 0)
			return;
		ArrayList<String> result = new ArrayList<String>();
		String myLocation = "我的位置";
		result.add(0, myLocation);
		for (int i = 0; i < size; i++) {
			FMPredictionInfo info = arrayList.get(i);
			// System.out.println("TextNaviResultContent == " +
			// info.getNaviDiscription()+" dis == "+info.getDistance());
			String ginfostr = "";
			FMGroupInfo ginfo = getInfoforId(info.getGroupId());
			if (i == size - 1) {
				if (ginfo != null){
					ginfostr = ginfo.getGroupName().toUpperCase();
					if(ginfostr.equals("F6")){
						if(isCare){
							CareTag = i;
							isCare = false;
						}
					}
				}
				String last = info.getNaviDiscription();
				String lastfortwo = ginfostr +" "+last.split(",")[0] + ";";
				result.add(lastfortwo);
				String lastforone = "到达目的地";
				result.add(lastforone);
			} else {
				FMSearchModelByCircleRequest byCircleRequest = new FMSearchModelByCircleRequest(info.getGroupId(),
						info.getMapCoord(), 10);
				ArrayList<FMSearchResult> list = mSearchAnalyser.executeFMSearchRequest(byCircleRequest);
				String name = "";
				if (list.size() != 0)
					if (list.get(0).get("name").toString().length() > 0)
						name = "在" + list.get(0).get("name").toString() + "处";
				if (ginfo != null){
					ginfostr = ginfo.getGroupName().toUpperCase();
					if(ginfostr.equals("F6")){
						if(isCare){
							CareTag = i;
							isCare = false;
						}
					}
				}
					
				String content = ginfostr + " " + info.getNaviDiscription().replace("###", name);
				result.add(content);
			}
		}
		mWordResults = result;
		// }
	}
    
	private FMGroupInfo getInfoforId(int id){
		for (int i = 0; i < groups.size(); i++) {
			if(groups.get(i).getGroupId() == id)
				return groups.get(i);
		}
		return null;
	}
	
	/**
	 * 画导航路径
	 */
	void drawNaviPath() {
//		if(mFids == null){
			FMLineMarker sceneLineMarker = MapDealUtils.clearAndReDrawLine(map,results, lineLayer, 8.0f, Color.RED);
	/*	}else{
			MapDealUtils.clearAndReDrawLineNo(map, results, lineLayer, 8.0f, Color.RED);
		}*/
		
		map.updateMap();
	}
	

	private ArrayList<TextView> mTextViews;
	private void showFloorsLayout(ArrayList<FMGroupInfo> groups){
		int floorNum = groups != null?groups.size():0;
		if(floorNum == 0){
			return;
		}
		mTextViews = new ArrayList<TextView>();
		// 有多少层
		gids = new int[floorNum];
		mScrollView.setVisibility(View.VISIBLE);
		mFloorLayout.removeAllViews();
		float scale = m_Context.getResources().getDisplayMetrics().scaledDensity;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10,5, 5, 10);
		layoutParams.gravity = Gravity.CENTER;
		for (int i = 0; i < floorNum; i++) {
			int num = groups.get(i).getGroupId();
			gids[i] = num;
			String name = groups.get(i).getGroupName();
			TextView textView = new TextView(m_Context);
		//	float size = 15 * scale;
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
			textView.setText(name.toUpperCase());
			textView.setTextColor(m_Context.getResources().getColor(R.color.white));;
			if(name.equals(mSetFloorName)){
				mCurGroupId = num;
				textView.setTextColor(m_Context.getResources().getColor(R.color.text_bule));
			}
			textView.setTag(num);
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int gid = (Integer) v.getTag();
					for(TextView view : mTextViews)
						view.setTextColor(m_Context.getResources().getColor(R.color.white));
					mCurGroupId = gid;
					if(mIsSingleLayer){
						setSingleDisplay();
					}else 
						setMultiDisplay();
					((TextView)v).setTextColor(m_Context.getResources().getColor(R.color.text_bule));
				}
			});
			mFloorLayout.addView(textView, 0,layoutParams);
			mTextViews.add(textView);
			if(i != floorNum - 1){
				ImageView imageView = new ImageView(m_Context);
				imageView.setBackgroundColor(R.color.text_planend);;
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,2);
				params.setMargins(10,5, 5, 10);
				mFloorLayout.addView(imageView,0, params);
			}
		}
		mScrollView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
		int[] gids = { mCurGroupId };       //获取当前地图焦点层id
        map.setMultiDisplay(gids);
		if (mSetFMMapCoord != null){
			map.setMapCenter(mCurGroupId, mSetFMMapCoord);
			map.setZoomLevel((float) (2/1E5));
		}
		map.updateMap();
		for(int i=0; i<groups.size(); i++) {
			FMModelLayer l = map.getFMLayerProxy().getFMModelLayer(groups.get(i).getGroupId());
			l.setOnFMNodeListener(this);
		}
	}

	
	private void addStartPos(String name,FMMapCoord coord,int gid){
		mEditStart.setText(name);
		mStartPt = coord;
		startGroupId = gid;
		addStartLayer();
	}
	
	private void addEndPos(String name,FMMapCoord coord,int gid){
		mEditEnd.setText(name);
		mEndPt = coord;
		endGroupId = gid;
		addEndLayer();
	}
	
	/**
     * 清理路径。
     * 场景改变，若存在线，则需要画。
     */
    void clearAndReDrawPath() {
        drawNaviPath();
    }


	@Override
	public void onMapUpdate(long arg0) {
		// TODO Auto-generated method stub
	//	System.out.println("zoom : "+map.getZoomLevel());
	}
	
	@Override
	public void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
	}
	
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE) {
			if (resultCode == RESULT_CODE) {
				String name = data.getStringExtra("name");
				int gid = data.getIntExtra("gid", 1);
				double x = data.getDoubleExtra("x", 0);
				double y = data.getDoubleExtra("y", 0);
				int tag = data.getIntExtra("tag", 0);
				FMMapCoord coord = new FMMapCoord(x, y);
				
				if(tag == TAG_START){
					addStartPos(name, coord, gid);
				}else if(tag == TAG_END){
					addEndPos(name, coord, gid);
				}
				if(mStartPt!=null && mEndPt != null) {
					anlyzeNavi(startGroupId, mStartPt, endGroupId, mEndPt);
				}else{
					//若起点和终点信息不完善，则直接定位到所选的点的楼层
					mCurGroupId = gid;
					int[] gids = { mCurGroupId }; // 获取当前地图焦点层id
					map.setMultiDisplay(gids);
					map.updateMap();
				}
			}
        }
		return super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onDestoryMap();
		}
		return super.onKeyDown(keyCode, keyevent);
	}

	@Override
	public void onNaviTaskResult(int type, FMNaviAnalyser analyser) {
		// TODO Auto-generated method stub
		if(mDialog != null)
			mDialog.dismiss();
		if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_SUCCESS) {
			results.clear();
			// [1,1,2,3...] 1,1 同层线 1,2跨层线 2,3跨层线
			results = analyser.getNaviResults();
			isCare = false;
			CareTag = -1;
            if(getInfoforId(endGroupId).getGroupName().toUpperCase().equals("F6")&&!getInfoforId(startGroupId).getGroupName().toUpperCase().equals("F6"))
            	isCare = true;
			/*FMLog.le("anlyzeNavi", "first: " + results.get(0).getPointList().get(0).toString() + " last: "
					+ results.get(0).getPointList().get(results.get(0).getPointList().size() - 1).toString());*/

	//		drawNaviPath();
			FMLineMarker sceneLineMarker = MapDealUtils.drawSceneLine(results, lineLayer, 8.0f, Color.RED);
			mNaviPrediction.preparePrediction(sceneLineMarker);
			mCurGroupId = startGroupId;
			//跳转到起点的楼层，并设为中心点及合适的缩放级别
			TurnFloor();
			map.setMapCenter(startGroupId, mStartPt);
			map.setZoomLevel((float) (2/1E5));
			map.updateMap();
	//		mIsNaviFinish = true;
     		
		}else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_STAIR_FLOORS) {
        	Toast.makeText(m_Context, "没有电梯或者扶梯进行跨层导航", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NOTSUPPORT_FLOORS) {
        	Toast.makeText(m_Context, "不支持跨层导航", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_PARAM_ERROR) {
        	Toast.makeText(m_Context, "导航参数出错", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_TOO_CLOSE) {
        	Toast.makeText(m_Context, "太近了", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_DATABASE_ERROR){
        	Toast.makeText(m_Context, "数据库出错", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_END) {
        	Toast.makeText(m_Context, "数据出错，终点不在其对应层的数据中", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_START) {
        	Toast.makeText(m_Context, "数据错误，起点不在其对应层的数据中", Toast.LENGTH_SHORT).show();
        }else if(type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_FAILURE_NO_FMDBKERNEL) {
        	Toast.makeText(m_Context, "底层指针错误", Toast.LENGTH_SHORT).show();
        }
	}
}
