package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 大会车辆
 * @author Administrator
 *
 */
public class AssemblyVehicleBean {

	@Expose
	public int resultCode;
	@Expose
	public List<AssemblyVehicleChildBean> list;
}
