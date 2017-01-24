package com.gj.gaojiaohui.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gaojiaohui.bean.MeetingNewsTitleBean;
import com.smartdot.wenbo.huiyi.R;
/**
 * 大会咨询栏目列表的适配器
 */
public class MeetingNewsRecyclerAdapter extends RecyclerView.Adapter<MeetingNewsRecyclerAdapter.MyViewHolder> {
	private List<MeetingNewsTitleBean> mDatas;
	private Context mContext;
	private LayoutInflater inflater;
	private int layoutPosition;

	public MeetingNewsRecyclerAdapter(Context context, List<MeetingNewsTitleBean> datas) {
		this.mContext = context;
		this.mDatas = datas;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	// 填充onCreateViewHolder方法返回的holder中的控件
	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {
		holder.tv.setText(mDatas.get(position).title);
		holder.layout.setTag(position);
		holder.layout.setOnClickListener(new OnClickListener() {

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
			holder.line.setVisibility(View.VISIBLE);
			holder.tv.setSelected(true);
		} else {
			holder.line.setVisibility(View.INVISIBLE);
			holder.tv.setSelected(false);
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

		View view = inflater.inflate(R.layout.item_meeting_news, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	class MyViewHolder extends ViewHolder {

		TextView tv;
		View line;
		LinearLayout layout;

		public MyViewHolder(View view) {
			super(view);
			tv = (TextView) view.findViewById(R.id.meeting_news_name_tv);
			line = view.findViewById(R.id.meeting_blue_line);
			layout = (LinearLayout) view.findViewById(R.id.meeting_news_layout);
		}

	}
}
