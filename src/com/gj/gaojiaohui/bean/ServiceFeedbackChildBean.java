package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class ServiceFeedbackChildBean {
	/** true//已读 false //未读 */
	@Expose
	public boolean state;
	/** 头像 */
	@Expose
	public String pic;
	/** 嘉宾id */
	@Expose
	public String id;
	/** 姓名 */
	@Expose
	public String name;
}
