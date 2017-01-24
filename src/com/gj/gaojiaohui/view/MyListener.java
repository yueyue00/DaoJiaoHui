package com.gj.gaojiaohui.view;

import android.os.Handler;
import android.os.Message;

import com.gj.gaojiaohui.view.PullToRefreshLayout.OnRefreshListener;

public interface MyListener extends OnRefreshListener {

	public void onRefresh(PullToRefreshLayout pullToRefreshLayout);

	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout);

}
