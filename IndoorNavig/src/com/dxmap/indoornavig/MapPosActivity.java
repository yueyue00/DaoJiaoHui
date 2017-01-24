package com.dxmap.indoornavig;

import java.util.ArrayList;

import com.dxmap.indoornavig.ShrinkView.OnShrinkOffsetListener;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.analysis.search.model.FMSearchModelByIdRequest;
import com.fengmap.android.analysis.search.model.FMSearchModelByKeywordRequest;
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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ListView;

@SuppressLint("ResourceAsColor")
public class MapPosActivity extends Activity
		implements OnFMMapInitListener, OnFMMapClickListener, OnFMMapUpdateEvent, OnClickListener ,OnItemClickListener,OnShrinkOffsetListener{
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
    private int[] mImages = {R.drawable.water1,R.drawable.water2,R.drawable.water3,R.drawable.water4,R.drawable.water5};
    private LinearLayout mShrinkView;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mShrinkBottom;
    private int mShrinkTop;
    private int mShrinkRight;
    private int mShrinkLeft;
    private int mLimtHeight;
    private int mMaxWidth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mappos);
		WindowManager wm = this.getWindowManager();
		mScreenWidth = wm.getDefaultDisplay().getWidth();
	    mScreenHeight = wm.getDefaultDisplay().getHeight();
		mBtn2D = (ImageView) findViewById(R.id.btn_2d);
		mBtn2D.setOnClickListener(this);
		mBtn2D.setImageDrawable(getResources().getDrawable(R.drawable.icon_3d));
		mBtnLayer = (ImageView) findViewById(R.id.btn_layer);
		mBtnLayer.setOnClickListener(this);
		mFloorLayout = (LinearLayout) findViewById(R.id.floorlayout);
		mScrollView = (ScrollView) findViewById(R.id.scroll);
		mScrollView.setVisibility(View.GONE);
		mBtnShrink = (ShrinkView) findViewById(R.id.btn_shrink);
		mBtnShrink.setOnClickListener(this);
		mBtnShrink.setListener(this);
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mPageLayout = (LinearLayout) findViewById(R.id.pagelayout);
		mPageLayout.setVisibility(View.GONE);
		mPagePre = (ImageView) findViewById(R.id.pagepre);
		mPagePre.setOnClickListener(this);
		mPageText = (TextView) findViewById(R.id.page);
		mPageNext = (ImageView) findViewById(R.id.pagenext);
		mPageNext.setOnClickListener(this);
		mSearchEdit = (EditText) findViewById(R.id.search_edit);
		mSearchEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		mSearchEdit.setFocusable(false);;
		mSearchEdit.setOnClickListener(this);
		mShrinkView = (LinearLayout) findViewById(R.id.layout_shrink);
		mResults = new ArrayList<FMSearchResult>();
		mAllResults = new ArrayList<FMSearchResult>();
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mAdapter = new MySearchResultAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setVisibility(View.GONE);
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		mKey = intent.getStringExtra("key");
		mType = intent.getLongExtra("type", 0);
		init(path);
	}

	private void init(String path) {
		if (path == null)
			path = MapDealUtils.getDefaultPath(this);
		// 拷贝地图
		String mappath = path + mMapId + "/";
		MapDealUtils.writeMap(this, mappath, mOffineMapName, mOffineMapName);
		// 拷贝主题
		String themepath = path + "theme/";
		MapDealUtils.writeTheme(this, themepath, mOffineThemeName, mOffineThemeName);

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_2d) {
			if (mIs2D) {
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
				mBtn2D.setImageDrawable(getResources().getDrawable(R.drawable.icon_3d));
				mIs2D = false;
			} else {
				map.setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
				mBtn2D.setImageDrawable(getResources().getDrawable(R.drawable.icon_2d));
				mIs2D = true;
			}
			map.updateMap();
		} else if (v.getId() == R.id.btn_layer) {
			if (mIsSingleLayer) {
				mBtnLayer.setImageDrawable(getResources().getDrawable(R.drawable.icon_layer_sel));
				setMultiDisplay();
				mIsSingleLayer = false;
			} else {
				mBtnLayer.setImageDrawable(getResources().getDrawable(R.drawable.icon_layer));
				setSingleDisplay();
				mIsSingleLayer = true;
			}
			map.updateMap();
		}else if(v.getId() == R.id.btn_back){
			CloseActivity();
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
			CloseActivity();
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

	}

	@Override
	public void onMapInitSuccess(String arg0) {
		// TODO Auto-generated method stub
		map.hiddenCompass();
		// map.loadDefaultTheme(FMMap.DEFAULT_THEME_CANDY);
		map.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
		// 场景配置
		FMMapInfo scene = map.getFMMapInfo();
		// 组（层）信息，可以简单理解为楼层
		if (scene == null)
			return;
		groups = scene.getGroups();
		showFloorsLayout(groups);
		mSearchEdit.setText(mKey);
		mFMSearchAnalyser = FMSearchAnalyser.init(map);
		if(mType > 0){
			Search(mType);
			return;
		}
		Search(mKey);
	}
	
	private void Search(String key){
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
			Toast.makeText(this, "当前搜索无结果", Toast.LENGTH_SHORT).show();
		
	}
	
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
		float scale = getResources().getDisplayMetrics().scaledDensity;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 5, 5, 10);
		for (int i = 0; i < floorNum; i++) {
			int num = groups.get(i).getGroupId();
			mGids[i] = num;
			String name = groups.get(i).getGroupName();
			TextView textView = new TextView(this);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
			textView.setText(name.toUpperCase());
			textView.setTextColor(getResources().getColor(R.color.white));

			if (name.equals(mSetFloorName)) {
				mCurGroupId = num;
				textView.setTextColor(getResources().getColor(R.color.text_bule));
			}
			textView.setTag(num);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int gid = (Integer) v.getTag();
					for (TextView view : mTextViews)
						view.setTextColor(getResources().getColor(R.color.white));
					mCurGroupId = gid;
					if (mIsSingleLayer) {
						setSingleDisplay();
					} else
						setMultiDisplay();
					((TextView) v).setTextColor(getResources().getColor(R.color.text_bule));
				}
			});
			mFloorLayout.addView(textView, 0, layoutParams);
			mTextViews.add(textView);
			if (i != floorNum - 1) {
				ImageView imageView = new ImageView(this);
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
				convertView = View.inflate(MapPosActivity.this, R.layout.view_search_list_item, null);
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
			int num = position + 1;
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
			FMMapCoord coord = getFMMapCoord(fid);
			if(coord == null)
				continue;
			MapDealUtils.addImageMarker(imageLayer, getFMMapCoord(fid), mImages[i], 30);	
		}
	}
	final int RESULT_CODE=101;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		FMSearchResult res = mResults.get(position);
		Intent intent = new Intent();
		String name = res.get("name").toString();
		String fid = res.get("fid").toString();
		FMMapCoord coord = getFMMapCoord(fid);
		if(coord == null)
			return ;
		FMGroupInfo info = getGroupInfo(fid);
		intent.putExtra("name", name);
		intent.putExtra("x", coord.x);
		intent.putExtra("y", coord.y);
		intent.putExtra("gid", info.getGroupId());
		setResult(RESULT_CODE, intent);
		CloseActivity();
	}
	
	private void CloseActivity(){
		mFMSearchAnalyser.release();
		map.onDestory();
		finish();
	}
    
    private int offsetb ;
	@Override
	public boolean onShrinkOffset(int offset) {
		// TODO Auto-generated method stub
		int goneheight = mShrinkView.getMeasuredHeight() - mBtnShrink.getMeasuredHeight();
		offsetb = mShrinkView.getBottom() + offset;
		if (offsetb > mLimtHeight) {
				mBtnShrink.layout(mBtnShrink.getLeft(), mBtnShrink.getTop()+offset,mBtnShrink.getRight(),mBtnShrink.getBottom()+offset);
				mShrinkView.layout(mShrinkView.getLeft(), mShrinkView.getTop()+offset,mShrinkView.getRight(),mShrinkView.getBottom()+offset);
			
		}
		return true;
	}
	private boolean isDown = false;
	@Override
	public void onShrinkUp(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
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
