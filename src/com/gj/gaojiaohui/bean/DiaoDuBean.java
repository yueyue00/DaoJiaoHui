package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 服务调度的bean(融云群组的bean)
 * 
 * @author zhangt
 * 
 */
public class DiaoDuBean implements Serializable {

	@Expose
	public String id;
	@Expose
	public String group_id;
	@Expose
	public String name;
}
