package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 关注展商 取消关注展商 活动报名 取消活动报名 编辑个人资料
 * @author Administrator
 *
 */
public class PublicResultCodeBean {

	/**200 //成功 500//失败 */
	@Expose
	public int resultCode;
	@Expose
	public String massage;
}
