package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 会议服务
 * @author Administrator
 *
 */
public class ConferenceServiceBean {

	@Expose
	public int resultCode;
	@Expose
	public List<ConferenceServiceChildBean> list;
}
