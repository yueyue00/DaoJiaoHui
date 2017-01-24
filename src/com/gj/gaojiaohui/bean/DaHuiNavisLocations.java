package com.gj.gaojiaohui.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class DaHuiNavisLocations {
	@Expose
	public String resultCode;
	@Expose
	public List<BDLocations> exhibition_list = new ArrayList<BDLocations>();// 展馆
	@Expose
	public List<BDLocations> hotel_list = new ArrayList<BDLocations>();// 酒店

}
