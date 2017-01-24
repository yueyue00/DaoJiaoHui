package com.gj.gaojiaohui.port;

/**
 * 接口回调--用于在adapter关注/取消关注是刷新数据
 * 
 * @author lixiaoming
 * 
 */
public interface OnChangeFollowZhanShang {
	/**
	 * @param id
	 *            -- 展商id
	 */
	public void onFollowRefresh(String id);

	/**
	 * @param id
	 *            -- 展商id
	 */
	public void onCancleFollowRefresh(String id);
}
