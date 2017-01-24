package com.gj.gaojiaohui.bean;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.annotations.Expose;

public class BDLocations {
	// 酒店名称
	@Expose
	public String name;
	// 经度
	@Expose
	public String latitude;
	// 纬度
	@Expose
	public String longitude;
	@Expose
	public LatLng ll;

	public void setLl(LatLng ll) {
		this.ll = ll;
	}

}
