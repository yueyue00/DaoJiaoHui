package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 通讯录实体类
 */
public class AddressBean {
	/** 姓名 */
	@Expose
	public String name;
	/** 部门id */
	@Expose
	public String dept_id;
	/** 电话 */
	@Expose
	public String tel;
	/** 是否部门 */
	@Expose
	public boolean is_dept;
}
