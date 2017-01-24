package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 采访预约实体类
 * 
 * @author lixiaoming
 * 
 */
public class InterviewAppointmentBean {
	@Expose
	public int resultCode;
	@Expose
	public List<InterviewAppointmentChildBean> list;
}
