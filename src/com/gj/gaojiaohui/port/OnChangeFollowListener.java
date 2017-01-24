package com.gj.gaojiaohui.port;

/***
 * 回调接口 - 用于在adapter取消关注时刷新外层列表
 */
public interface OnChangeFollowListener {
	/**
	 * 
	 * @param id 展商id
	 */
	public void OnRefresh(String id);
}
