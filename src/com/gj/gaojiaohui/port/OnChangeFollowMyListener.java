package com.gj.gaojiaohui.port;

/***
 * 观众互动 回调接口 - 用于在adapter关注/取消关注时刷新外层列表
 */
public interface OnChangeFollowMyListener {
	/**
	 * 
	 * @param id 展商id
	 */
	public void OnRefresh(String id,boolean state);
}
