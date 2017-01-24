package com.dxmap.indoornavig;

import java.util.ArrayList;

import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.analysis.search.facility.FMSearchFacilityByTypeRequest;
import com.fengmap.android.analysis.search.model.FMSearchModelByKeywordRequest;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.marker.FMModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class NaviSearchView extends BaseView implements OnClickListener,OnItemClickListener{
	
	public interface OnNaviSearchResultListener{
		public void onNaviSearchResult(String name,FMMapCoord coord,int tag,int gid);
	}

	private int[] mGridImgId = new int[] { R.drawable.icon_search_restaurant, R.drawable.icon_search_escalator,
			R.drawable.icon_search_lift, R.drawable.icon_search_subway, R.drawable.icon_search_stairs, R.drawable.icon_search_wc,
			R.drawable.icon_search_reception, R.drawable.icon_search_exit };
	
	private String[] mGridName = {"餐厅","扶梯","直梯","专线","步行梯","卫生间","服务台","出入口"};
    private long[] mTypeId = {0,170003,170006,0,170001,110000,140002,110001}; 
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
	@Override
	public boolean onCreateView(View view) {
		// TODO Auto-generated method stub
		super.onCreateView(view);
		mCancel = (TextView) findViewById(R.id.cancel);
		mCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) m_Context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
				BaseStack.RemoveLastView();
			}
		});
		mSearchText = (EditText) findViewById(R.id.edit_search);
		mSearchText.requestFocus();
		mSearchText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					String key = mSearchText.getText().toString();
					if(key.trim().length() == 0){
						Toast.makeText(m_Context, "请输入搜索内容", Toast.LENGTH_SHORT).show();
						return false;
					}
					InputMethodManager imm = (InputMethodManager) m_Context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					}
					ArrayList<FMSearchResult> searchResults = MapDealUtils.SearchForKey(key, mGids, mSearchAnalyser);
					
					if(searchResults.size() == 0){
						Toast.makeText(m_Context, "未搜索到结果", Toast.LENGTH_SHORT).show();
					}else{
						Intent intent = new Intent(m_Context,MapPosActivity.class);
						intent.putExtra("path", mPath);
						intent.putExtra("key", mSearchText.getText().toString());
						((Activity)m_Context).startActivityForResult(intent, REQUEST_CODE);
						/*mSearchResults = searchResults;
						mListAdapter.notifyDataSetChanged();*/
					}
					
					return true;
				}
				return false;
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
		return true;
	}
	
	
	public void setDatas(OnNaviSearchResultListener listener,FMMap map,int tag,int gid,int[] gids){
		mListener = listener;
		mMap = map;
		mSearchAnalyser = FMSearchAnalyser.init(map);
		mTag = tag;
		mGid = gid;
		mGids = gids;
	}
	
	public void setPath(String path){
		mPath = path;
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
		if(parent == mGridView){
			long type = mTypeId[position];
			ArrayList<FMSearchResult> resultSet = MapDealUtils.SearchForType(type, mGids, mSearchAnalyser);
			if(resultSet.size() == 0){
				Toast.makeText(m_Context, "未搜索到结果", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(m_Context,MapPosActivity.class);
				intent.putExtra("path", mPath);
				intent.putExtra("key", mGridName[position]);
				intent.putExtra("type", type);
				((Activity)m_Context).startActivityForResult(intent, REQUEST_CODE);
				((Activity)m_Context).overridePendingTransition(R.anim.textnavi_open,0);
				/*mSearchResults = searchResults;
				mListAdapter.notifyDataSetChanged();*/
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
				convertView = View.inflate(m_Context, R.layout.view_search_grid_item, null);
				TextView textView = (TextView) convertView.findViewById(R.id.name);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
				ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
				holder = new ViewHolder();
				holder.name = textView;
				holder.image = imageView;
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.name.setText(mGridName[position]);
			holder.image.setImageDrawable(m_Context.getResources().getDrawable(mGridImgId[position]));
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
				convertView = View.inflate(m_Context, R.layout.view_search_list_item, null);
				TextView textView = (TextView) convertView.findViewById(R.id.name);
				textView.setTextColor(Color.BLACK);
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
	
	public void Remove(){
		mSearchAnalyser.release();
		mSearchAnalyser = null;
	}
	
	@Override
	public void OnDestroy() {
		// TODO Auto-generated method stub
		Remove();
		super.OnDestroy();
	}
	
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE) {
			if (resultCode == RESULT_CODE) {
				String name = data.getStringExtra("name");
				mGid = data.getIntExtra("gid", 1);
				double x = data.getDoubleExtra("x", 0);
				double y = data.getDoubleExtra("y", 0);
				FMMapCoord coord = new FMMapCoord(x, y);
				mListener.onNaviSearchResult(name, coord, mTag, mGid);
				BaseStack.RemoveLastView();
			}
        }
		return super.onActivityResult(requestCode, resultCode, data);
	}
	
}