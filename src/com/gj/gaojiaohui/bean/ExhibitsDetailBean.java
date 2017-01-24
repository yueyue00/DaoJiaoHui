package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class ExhibitsDetailBean {
	@Expose
	public int resultCode;
	/** 详情标题 */
	@Expose
	public String title;
	/** 详情内容 */
	@Expose
	public String value;
	/** 详情图片 */
	@Expose
	public String pic;

}
