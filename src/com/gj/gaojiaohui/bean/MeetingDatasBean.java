package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

public class MeetingDatasBean {
	@Expose
	public int resultCode;
	@Expose
	public List<MeetingDatasChildBean> list;
}
