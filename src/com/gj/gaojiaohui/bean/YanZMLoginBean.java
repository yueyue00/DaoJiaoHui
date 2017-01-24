package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class YanZMLoginBean {
	@Expose
	public String resultCode="500";
	// 发送验证码的时间
	@Expose
	public String date;
	// 发送到手机端的验证码
	@Expose
	public String yzCode;
	// 用户id 唯一标识
	@Expose
	public String user_id;
}
