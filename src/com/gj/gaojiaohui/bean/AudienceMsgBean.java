package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 观众详情 最新留言 - 实体类
 */
public class AudienceMsgBean {
	/** 留言内容*/
	@Expose
	public String value;
	/** 留言时间 */
	@Expose
	public String date;

}
