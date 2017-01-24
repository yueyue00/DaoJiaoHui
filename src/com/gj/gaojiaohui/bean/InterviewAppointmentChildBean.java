package com.gj.gaojiaohui.bean;

import com.google.gson.annotations.Expose;

/**
 * 采访预约的实体类子类
 * 
 * @author lixiaoming
 * 
 */
public class InterviewAppointmentChildBean {
	/** 展商名称 */
	@Expose
	public String title;
	/** 展商介绍 */
	@Expose
	public String value;
	/** 展商id */
	@Expose
	public String id;
}
