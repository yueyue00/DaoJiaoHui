package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 融云所有用户的信息
 * 
 * @author Administrator
 * 
 */
public class AllUserBean implements Serializable {

	@Expose
	public String id;
	@Expose
	public String user_id;
	@Expose
	public String user_pic;
	@Expose
	public String user_name;
}
