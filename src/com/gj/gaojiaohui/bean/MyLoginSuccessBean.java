package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 我的 已登录
 * @author Administrator
 *
 */
public class MyLoginSuccessBean {

	@Expose
	public int resultCode;
	/**用户二维码*/
	@Expose
	public MyLoginSuccessChildBean user_qr;
	/**下载地址*/
	@Expose
	public String meeting_qr;
	@Expose
	/**赠票二维码地址*/
	public String coupon;
}
