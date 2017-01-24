package com.dxmap.indoornavig;

import java.util.ArrayList;
import java.util.Collections;

import com.dxmap.indoornavig.NaviSearchView.OnNaviSearchResultListener;
import com.dxmap.indoornavig.R;
import com.dxmap.indoornavig.test.ViewMapPos;
import com.dxmap.indoornavig.test.ViewMapPos.OnMapGetResultListener;
import com.dxmap.indoornavig.test.ViewMapPos.OnMapLoadFinishListener;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.marker.FMModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class NaviSearchActivity extends Activity implements OnItemClickListener,OnMapLoadFinishListener,OnMapGetResultListener{
	
	private int[] mGridImgId = new int[] { R.drawable.icon_search_restaurant, R.drawable.icon_search_ticket,
			R.drawable.icon_search_register, R.drawable.icon_search_reception, R.drawable.icon_search_subway, R.drawable.icon_search_meet,
			R.drawable.icon_search_wc, R.drawable.icon_search_exit };
	
	private String[] mGridName = {"餐饮区","售票处","观众注册","总服务台","地铁","会议室","卫生间","出入口"};
	
	
	private String[] mFids = { "90295015", "902950110", "902950155", "90295011", "902950157", "9029501695",
			"9029501678", "9029501683", "9029501642", "9029501646", "9029501766", "9029501772", "9029501723",
			"902950147", "902950150", "902950159", "902950164", "902950167", "902950176", "902950181", "902950175",
			"902950170", "902950190", "902950193", "902950166", "902950168" };
	//主会场分类
	private long[] mTypeMainId = { 140000, //餐饮区
			140100,//售票处
			140800, //观众注册
			200008,  //总服务台
			100000,  //地铁
			160000,  //会议室
			200002, //卫生间
			200001 }; // 出入口
	//分会场分类
	private long[] mTypeSubId = { 120000, //餐饮区
			140000,//售票处
			140800, //观众注册
			200008,  //总服务台
			100000,  //地铁
			140000,  //会议室
			200002, //卫生间
			200001 }; // 出入口
//	private ViewGroup mView;
	private OnNaviSearchResultListener mListener;
//	private Context m_Context;
	private ListView mListView;
	private GridView mGridView;
	private TextView mCancel;
	private EditText mSearchText;
	private FMSearchAnalyser mSearchAnalyser;
	private ArrayList<FMSearchResult> mSearchResults;
	private MyGridAdapter mGridAdapter;
	private MyListAdapter mListAdapter;
	private ViewGroup mViewGroup;
	private int mTag;
	private int mGid;
	private int[] mGids;
	private FMMap mMap;
	private String mPath;
	final int RESULT_CODE=101;
    final int REQUEST_CODE=1;
    private ViewMapPos mViewMapPos;
    private ProgressDialog mDialog;
    private FrameLayout mMainView;
    private boolean mIsTypeSearch;  //是否是在地图加载前进行类型搜索
    private boolean mIsKeySearch ;  //是否是在地图加载前进行关键字搜索
    private String mKey;
    private int mTypePos;
    private ArrayList<String> mManyPoint;
    private boolean isHighlight = false;  //是否为亮点模式
    private boolean isMapLoadFinish = false;  //地图是否加载完成
    private Configuration mConfiguration;
    private int mMapType ;
//    private boolean isActivtyFinish = false; //当前Activity是否已经关闭了
    private View mSearchView ;
    private ArrayList<String> mLigthNos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_searchnavi);
		mSearchView = findViewById(R.id.search_view);
		mSearchText = (EditText) findViewById(R.id.edit_search);
		mSearchText.requestFocus();
		mSearchText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					mKey = mSearchText.getText().toString();
					if(mKey.trim().length() == 0){
						Toast.makeText(NaviSearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
						return false;
					}
					hideSoftInput(v);
					
					if(!isMapLoadFinish){
						mIsKeySearch = true;
						LoadMap();
						return true;
					}
					
					ArrayList<FMSearchResult> searchResults ;
					String fid = mConfiguration.getValue(mKey);
					if(fid != null){
						searchResults = MapDealUtils.getFMResultForFid(fid, mGids, mSearchAnalyser);
						mViewMapPos.setFMSearchNumResult(mKey, searchResults,mSearchAnalyser);
						SearchViewHide();
					}else{
						searchResults = MapDealUtils.SearchForKey(mKey, mGids, mSearchAnalyser);
						if(searchResults.size() == 0){
							Toast.makeText(NaviSearchActivity.this, "未搜索到结果", Toast.LENGTH_SHORT).show();
						}else{
							if(searchResults.size() == 1)
								mViewMapPos.setFMSearchNumResult(mKey, searchResults,mSearchAnalyser);
							else
								mViewMapPos.setFMSearchResult(mKey, searchResults,mSearchAnalyser);
							SearchViewHide();
						}
					}
					return true;
				}
				return false;
			}
		});
		mCancel = (TextView) findViewById(R.id.cancel);
		mCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideSoftInput(v);
			//	if (isMapLoadFinish)
				//	CloseActivity();
					onBackPressed();
			}
		});
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setOnItemClickListener(this);
		mGridAdapter = new MyGridAdapter();
		mGridView.setAdapter(mGridAdapter);
		
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mSearchResults = new ArrayList<FMSearchResult>();
		mListAdapter = new MyListAdapter();
		mListView.setAdapter(mListAdapter);
		mMainView = (FrameLayout) findViewById(R.id.mainView);
		Intent intent = getIntent();
		mPath = intent.getStringExtra("path");
		mGids = intent.getIntArrayExtra("gids");
		mTag = intent.getIntExtra("tag", 0);
		mMapType = intent.getIntExtra("maptype", 0);
		//亮点
		mLigthNos = intent.getStringArrayListExtra("data");
		if(mLigthNos != null){
			//展位号进行字符串排序
			Collections.sort(mLigthNos);
			LoadMap();
			mSearchView.setVisibility(View.GONE);
			isHighlight = true;
			mViewMapPos.setHighlight(isHighlight);
			mManyPoint = new ArrayList<String>();
			for (int i = 0; i < mLigthNos.size(); i++) {
				String mNum = mConfiguration.getValue(mLigthNos.get(i));
				if (mNum != null)
					mManyPoint.add(mNum);
			}
		//	LoadMap();
		}
	}

	private void LoadMap() {
		if(mViewMapPos == null){
			//加载配置文件
			if (mMapType == IndoorNaviView.MAP_TYPE_MAIN)
				mConfiguration = new Configuration("/MainConfiguration.properties");
			else
				mConfiguration = new Configuration("/SubConfiguration.properties");
			mViewMapPos = new ViewMapPos(this);
			mViewMapPos.setMapFinishListener(this);
			mViewMapPos.setMapGetResultListener(this);
			mViewMapPos.setSearchView(mSearchView);
			mViewMapPos.setMapType(mMapType);
			mViewMapPos.init(mPath);
			mMainView.addView(mViewMapPos.getView(),0);
			mDialog = ProgressDialog.show(this, null, "正在搜索...");
		}
		
	}
    
	private long[] getTypes(){
		if(mMapType == IndoorNaviView.MAP_TYPE_MAIN)
			return mTypeMainId;
		else if(mMapType == IndoorNaviView.MAP_TYPE_SUB)
			return mTypeSubId;
		return null;
	}
	
	class MyGridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mGridImgId.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
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
				convertView = View.inflate(NaviSearchActivity.this, R.layout.view_search_grid_item, null);
				TextView textView = (TextView) convertView.findViewById(R.id.name);
				ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
				holder = new ViewHolder();
				holder.name = textView;
				holder.image = imageView;
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.name.setText(mGridName[position]);
			holder.image.setImageDrawable(getResources().getDrawable(mGridImgId[position]));
			return convertView;
		}
		
	}
	
	class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSearchResults.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSearchResults.get(position);
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
				convertView = View.inflate(NaviSearchActivity.this, R.layout.view_search_list_item, null);
				TextView textView = (TextView) convertView.findViewById(R.id.name);
				holder = new ViewHolder();
				holder.name = textView;
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			FMSearchResult result = mSearchResults.get(position);
			holder.name.setText(result.get("name").toString());
			return convertView;
		}
	}
	
	class ViewHolder{ 
		public TextView name;
		public ImageView image;
	}
	
	public void CloseActivity() {
		if (mViewMapPos != null) {
			mViewMapPos.onDestoryMapView();
		}
		this.finish();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(parent == mListView){
			if(mListener != null){
				FMSearchResult result = mSearchResults.get(position);
				/*for(Object key:result.keySet()){
					System.out.println("key: "+ key.toString() +"   vaule:  "+result.get(key).toString());
				}*/
				String name = result.get("name").toString();
				String fid = (String) result.get("fid");
                FMModel m = mMap.getFMLayerProxy().queryFMModelByFid(fid);
				 //坐标  
				FMMapCoord coord =  m.getCenterMapCoord(); 
				mListener.onNaviSearchResult(name,coord, mTag,mGid);
				BaseStack.RemoveLastView();
			}
		}
		if (parent == mGridView) {
			mTypePos = position;
			if (!isMapLoadFinish) {
				mIsTypeSearch = true;
		//		System.out.println("11111111111111111111111111");
		//		mDialog = ProgressDialog.show(this, null, "正在搜索...");
				LoadMap();
				return;
			}
			long type = getTypes()[position];
			ArrayList<FMSearchResult> resultSet = MapDealUtils.SearchForType(type, mGids, mSearchAnalyser);
			if(resultSet.size() == 0){
				Toast.makeText(this, "未搜索到结果", Toast.LENGTH_SHORT).show();
			}else{
				hideSoftInput(parent);
				mViewMapPos.setFMSearchResult(mGridName[position], resultSet,mSearchAnalyser);
				SearchViewHide();
				
				/*Intent intent = new Intent(this,MapPosActivity.class);
				intent.putExtra("path", mPath);
				intent.putExtra("key", mGridName[position]);
				intent.putExtra("type", type);
				startActivityForResult(intent, REQUEST_CODE);*/
			//	((Activity)m_Context).overridePendingTransition(R.anim.textnavi_open,0);
				/*mSearchResults = searchResults;
				mListAdapter.notifyDataSetChanged();*/
			}
		}
	}

	@Override
	public void onMapLoadSuccess(String path) {
		// TODO Auto-generated method stub
		isMapLoadFinish = true;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mDialog != null)
					mDialog.dismiss();
				/*if(isActivtyFinish)
					CloseActivity();*/
			}
		}, 1500);
		
	    mMap = mViewMapPos.getMap();
	    mSearchAnalyser = FMSearchAnalyser.init(mMap);
	    mGids = mViewMapPos.getGids();
	    if(isHighlight){
	    	mViewMapPos.setLightNo(mLigthNos);
	    	mViewMapPos.setData(mManyPoint, mSearchAnalyser, this);
	    	return;
	    }
	    if(mIsTypeSearch){
	    	ArrayList<FMSearchResult> resultSet = MapDealUtils.SearchForType(getTypes()[mTypePos], mGids, mSearchAnalyser);
			if(resultSet.size() == 0){
				Toast.makeText(this, "未搜索到结果", Toast.LENGTH_SHORT).show();
			}else{
				hideSoftInput(mSearchView);
				mViewMapPos.setFMSearchResult(mGridName[mTypePos], resultSet,mSearchAnalyser);
				SearchViewHide();
			}
	    }
	    if(mIsKeySearch){
	    	ArrayList<FMSearchResult> searchResults = MapDealUtils.SearchForKey(mKey, mGids, mSearchAnalyser);
			
			if(searchResults.size() == 0){
				Toast.makeText(NaviSearchActivity.this, "未搜索到结果", Toast.LENGTH_SHORT).show();
			}else{
				hideSoftInput(mSearchView);
				mViewMapPos.setFMSearchResult(mKey, searchResults,mSearchAnalyser);
				SearchViewHide();
			}
		}
	   
	}

	@Override
	public void onMapLoadFaile(String path) {
		// TODO Auto-generated method stub
		isMapLoadFinish = true;
		if(mDialog != null)
	    	mDialog.dismiss();
	}

	@Override
	public void onGetResult(String name,FMMapCoord coord,int gid) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("x", coord.x);
		intent.putExtra("y", coord.y);
		intent.putExtra("gid", gid);
		intent.putExtra("tag", mTag);
		setResult(RESULT_CODE, intent);
		CloseActivity();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!isHighlight){
			if(mSearchView.getVisibility() == View.GONE){
				mViewMapPos.CloseView();
				return;
			}
		}
		CloseActivity();
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private View get_root_view() {
		return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
	}

	private void hideSoftInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	
	private void SearchViewHide(){
		mSearchView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mSearchView.setVisibility(View.GONE);
			}
		}, 500);
	}
}
