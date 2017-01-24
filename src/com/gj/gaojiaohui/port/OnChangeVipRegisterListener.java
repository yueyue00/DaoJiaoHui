package com.gj.gaojiaohui.port;

/***
 * 回调接口 - 用于在adapter请求签到时回调到activity执行
 */
public interface OnChangeVipRegisterListener {
	/**
	 * 
	 * @param userId
	 *            嘉宾id
	 * @param btnId
	 *            签到按钮id
	 */
	public void OnRegisterData(String userId, String btnId);
}
