package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 展商搜索
 * @author Administrator
 *
 */
public class ExhibitorsSearchBean {

	@Expose
	public int resultCode;
	@Expose
	public List<ExhibitorsSearchChildBean> list;
}
