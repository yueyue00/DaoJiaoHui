package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 导航页的bean
 * 
 * @author zhangt
 * 
 */
public class MenuBean implements Serializable {

	@Expose
	public String name;

	@Expose
	public String id;

	public MenuBean(String name, String id) {
		this.name = name;
		this.id = id;
	}

}
