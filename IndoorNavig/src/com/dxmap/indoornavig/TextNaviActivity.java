package com.dxmap.indoornavig;

import java.util.ArrayList;

import com.dxmap.indoornavig.TextNaviView.MyAdapter;
import com.dxmap.indoornavig.TextNaviView.MyAdapter.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TextNaviActivity extends Activity implements OnClickListener{
	
	private TextNaviView mTextNaviView;
	
	private ListView mListView;
	private ArrayList<String> mDatas;
	private MyAdapter mAdapter;
	private ViewGroup mViewGroup;
	private ImageView mBtnClose;
	private int mCareTag = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_textplan);
		Intent intent = getIntent();
		
		mBtnClose = (ImageView)findViewById(R.id.btn_close);
		mBtnClose.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listview);
		mDatas = intent.getStringArrayListExtra("data");
		mCareTag = intent.getIntExtra("caretag", -1);
		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
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
				convertView = View.inflate(TextNaviActivity.this, R.layout.view_textplan_item, null);
				holder = new ViewHolder();
				TextView name = (TextView) convertView.findViewById(R.id.list_item_name);
				holder.name = name;
				ImageView image = (ImageView) convertView.findViewById(R.id.list_item_img);
				holder.image = image;
				convertView.setTag(holder);
			}
			String name = (String) getItem(position);
			holder = (ViewHolder) convertView.getTag();
			if(mCareTag == position){
				String str = name + "\n（请注意所乘电梯是否到达F6）";
				SpannableStringBuilder builder = new SpannableStringBuilder(str);  
				ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_bule)); 
				builder.setSpan(redSpan, name.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.name.setText(builder);
			}else{
				holder.name.setText(name);
			}
			if(position == 0){
				holder.image.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_list_item_start));
				holder.name.setTextColor(getResources().getColor(R.color.text_bule));
			}
			else if(position == mDatas.size()-1){
				holder.image.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_list_item_end));
				holder.name.setTextColor(getResources().getColor(R.color.text_planend));
			}
			else{
				holder.image.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_list_item_img));
				holder.name.setTextColor(getResources().getColor(R.color.text_darkgrey));
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
			finish();
			//关闭窗体动画显示  
		    this.overridePendingTransition(0,R.anim.textnavi_close);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		this.overridePendingTransition(0,R.anim.textnavi_close);
		super.onBackPressed();
	}
}
