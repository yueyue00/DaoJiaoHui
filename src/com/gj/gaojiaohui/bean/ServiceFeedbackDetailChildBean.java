package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class ServiceFeedbackDetailChildBean {
	/** 头像 */
	@Expose
	public String pic;
	/** 姓名 */
	@Expose
	public String name;
	/** 时间 */
	@Expose
	public String date;
	/** 留言内容 */
	@Expose
	public String value;
}
