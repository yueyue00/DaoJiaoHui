package com.dxmap.indoornavig;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TextNaviView implements OnClickListener{
	
	private Context mContext;
	private View mView;
	private ListView mListView;
	private ArrayList<String> mDatas;
	private MyAdapter mAdapter;
	private ViewGroup mViewGroup;
	private ImageView mBtnClose;
	private EditText mSearchEditText;
	public TextNaviView(Context context,View view) {
		// TODO Auto-generated constructor stub
		mView = view;
		mContext = context;
		mView = (ViewGroup) View.inflate(context, R.layout.view_textplan, null);
		mBtnClose = (ImageView) mView.findViewById(R.id.btn_close);
		mBtnClose.setOnClickListener(this);
		mListView = (ListView) mView.findViewById(R.id.listview);
		mDatas = new ArrayList<String>();
		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
	}
	
	public void setViewData(ArrayList<String> data){
		mDatas = data;
		mAdapter.notifyDataSetChanged();
	}
	
	public void removeView(){
		mViewGroup.removeView(mView);
	}
	
	@SuppressLint("NewApi")
	public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mDatas.get(position);
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
				convertView = View.inflate(mContext, R.layout.view_textplan_item, null);
				holder = new ViewHolder();
				TextView name = (TextView) convertView.findViewById(R.id.list_item_name);
				holder.name = name;
				ImageView image = (ImageView) convertView.findViewById(R.id.list_item_img);
				holder.image = image;
				convertView.setTag(holder);
			}
			String name = (String) getItem(position);
			holder = (ViewHolder) convertView.getTag();
			holder.name.setText(name);
			if(position == 0){
				holder.image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_list_item_start));
				holder.name.setTextColor(mContext.getResources().getColor(R.color.text_bule));
				holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			}
			else if(position == mDatas.size()-1){
				holder.image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_list_item_end));
				holder.name.setTextColor(mContext.getResources().getColor(R.color.text_planend));
				holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			}
			else{
				holder.image.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_list_item_img));
				holder.name.setTextColor(mContext.getResources().getColor(R.color.text_darkgrey));
				holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			}
				
			return convertView;
		}
		
		public class ViewHolder{
			public TextView name;
			public ImageView image;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_close){
			removeView();
		}
	}
	
	
}
