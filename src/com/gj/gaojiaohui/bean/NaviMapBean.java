package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

public class NaviMapBean {
	/** 经度 */
	@Expose
	public String longitude;
	/** 纬度 */
	@Expose
	public String latitude;
	/** 酒店名称/会场名称 */
	@Expose
	public String name;
}
