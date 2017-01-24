package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.MeetingScheduleBean;
import com.smartdot.wenbo.huiyi.R;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
	private List<MeetingScheduleBean> mDatas;
	private Context mContext;
	private LayoutInflater inflater;
	private int layoutPosition;

	public MyRecyclerAdapter(Context context, List<MeetingScheduleBean> scheduleDatas) {
		this.mContext = context;
		this.mDatas = scheduleDatas;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	// 填充onCreateViewHolder方法返回的holder中的控件
	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {
		holder.richeng_week_tv.setText(mDatas.get(position).title);
		holder.richeng_date_tv.setText(mDatas.get(position).time);
		holder.layouyt.setTag(position);
		holder.layouyt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取当前点击的位置
				layoutPosition = holder.getLayoutPosition();
				// 刷新单选显示状态
				notifyDataSetChanged();
				// 将当前item的数据回调到外层可被调用
				mItemClickListener.onItemClick(holder.itemView, (String) holder.itemView.getTag(), layoutPosition);
			}
		});

		// 更改状态,判断是否是当前点击的item,进行单选显示
		if (position == layoutPosition) {
			holder.line.setBackgroundColor((Color.parseColor("#FFFFFF")));
			holder.layouyt.setSelected(true);
			holder.imageView.setSelected(true);
		} else {
			holder.line.setBackgroundColor((Color.parseColor("#C8C7CC")));
			holder.layouyt.setSelected(false);
			holder.imageView.setSelected(false);
		}
	}

	public void changData(List<MeetingScheduleBean> scheduleDatas) {
		if (scheduleDatas != null) {
			this.mDatas = scheduleDatas;
			notifyDataSetChanged();
		}
	}

	/** 指定默认选中的item */
	public void setDefaultSelected(int position) {
		this.layoutPosition = position;
	}

	// 给RecyclerView的Item添加点击事件
	public OnRecyclerViewItemClickListener mItemClickListener;

	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, String data, int position);
	}

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	// 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = inflater.inflate(R.layout.item_schedule_date, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	class MyViewHolder extends ViewHolder {

		TextView richeng_week_tv, richeng_date_tv;
		View line;
		ImageView imageView;
		LinearLayout layouyt;

		public MyViewHolder(View view) {
			super(view);
			richeng_week_tv = (TextView) view.findViewById(R.id.richeng_week_tv);
			richeng_date_tv = (TextView) view.findViewById(R.id.richeng_date_tv);
			imageView = (ImageView) view.findViewById(R.id.richeng_image);
			line = view.findViewById(R.id.richeng_line_view);
			layouyt = (LinearLayout) view.findViewById(R.id.richeng_layout);
		}

	}
}
