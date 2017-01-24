package com.gj.gaojiaohui.port;

/***
 *  回调接口 - 用于在adapter进行观众评星时刷新外层列表 用于我的关注
 */
public interface OnChangeMyFollowListener {
	/**
	 * 
	 * @param id 展商id
	 */
	public void OnRefresh(String id,float rating);
}
