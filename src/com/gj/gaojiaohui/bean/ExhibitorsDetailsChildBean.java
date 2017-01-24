package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 展商详情--子bean类
 * @author Administrator
 *
 */
public class ExhibitorsDetailsChildBean {

	/** 展品标题 */
	@Expose
	public String title;
	/** 内容 */
	@Expose
	public String value;
	/** 展品详情 id */
	@Expose
	public String id;
	/** 展商图片 */
	@Expose
	public String pic;
}
