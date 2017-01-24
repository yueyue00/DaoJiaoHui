package com.gj.gaojiaohui.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 大会车辆--子bean
 * 
 * @author Administrator
 */
public class AssemblyVehicleChildBean implements Serializable {

	@Expose
	public String name;
	@Expose
	public List<String> approach;
	@Expose
	public String map;
	@Expose
	public String longitude = "22.5363840000";
	@Expose
	public String latitude = "114.0665180000";

}
