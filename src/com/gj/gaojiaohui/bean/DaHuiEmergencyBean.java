package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class DaHuiEmergencyBean implements Serializable{
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 标题 */
	@Expose
	public String title;
	/** 时间 */
	@Expose
	public String date;
	/** 内容 */
	@Expose
	public String value;
}
