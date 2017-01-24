package com.dxmap.indoornavig;

import com.fengmap.android.map.geometry.FMMapCoord;

import android.R.color;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class IndoorHomeView extends BaseView implements OnItemClickListener {
	private ListView mListView;
	private int[] mImgId = { R.drawable.img_hall1, R.drawable.img_hall2, R.drawable.img_hall3 };
	private String[] mNames = { "展馆一", "展馆二", "展馆三" };
	private HallListAdapter mAdapter;
	private ViewGroup mGroup;
    private String mOffineMapPath = null;
	@Override
	public boolean onCreateView(View view) {
		// TODO Auto-generated method stub
		super.onCreateView(view);
		mAdapter = new HallListAdapter();
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
		return true;
	}
	/*public IndoorHomeView(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		
	}*/

	public void setParentView(ViewGroup group) {
		mGroup = group;
	}
	/**
	 * 添加离线地图路径
	 * @param path
	 */
	public void setOffineMapPath(String path){
		mOffineMapPath = path;
	}


	class HallListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNames.length;
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
			ViewHoldor holdor = null;
			if (convertView == null) {
				convertView = View.inflate(m_Context, R.layout.view_home_list_item, null);
				TextView textView = (TextView) convertView.findViewById(R.id.name);
				textView.setTextColor(Color.BLACK);
				ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
				imageView.setVisibility(View.VISIBLE);
				holdor = new ViewHoldor();
				holdor.name = textView;
				holdor.image = imageView;
				convertView.setTag(holdor);
			}
			holdor = (ViewHoldor) convertView.getTag();
			holdor.name.setText(mNames[position]);
			holdor.image.setBackgroundResource(mImgId[position]);
			return convertView;
		}

		class ViewHoldor {
			TextView name;
			ImageView image;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		switch (position) {
		// 展馆一
		case 0:
			//1.29388541554785E7/4853107.97235717/4.0
			double x = 1.26975982461292E7;
			double y = 2575967.54547102;
			FMMapCoord coord = new FMMapCoord(x, y);
			IndoorNaviView mPlanView = (IndoorNaviView) BaseStack.CreateViewAndAddView(IndoorNaviView.class, R.layout.view_indoorplan);
//			mPlanView.initMap(mOffineMapPath);
//			mPlanView.setTitleName("展馆一");
//			mPlanView.setHallPos("f1",coord);
			BaseStack.SetContentView(mPlanView);
			break;
		// 展馆二
		case 1:
			mPlanView = (IndoorNaviView) BaseStack.CreateViewAndAddView(IndoorNaviView.class, R.layout.view_indoorplan);
//			mPlanView.initMap(mOffineMapPath);
//			mPlanView.setTitleName("展馆二");
//			mPlanView.setHallPos("f1");
			BaseStack.SetContentView(mPlanView);
			break;
		// 展馆三
		case 2:
			mPlanView = (IndoorNaviView) BaseStack.CreateViewAndAddView(IndoorNaviView.class, R.layout.view_indoorplan);
//			mPlanView.initMap(mOffineMapPath);
//			mPlanView.setTitleName("展馆三");
//			mPlanView.setHallPos("f2");
			BaseStack.SetContentView(mPlanView);
			break;

		default:
			break;
		}
	}

}
