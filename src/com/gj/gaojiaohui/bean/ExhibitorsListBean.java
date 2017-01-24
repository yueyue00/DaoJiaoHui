package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 展商列表--bean类
 * @author Administrator
 *
 */
public class ExhibitorsListBean {
	@Expose
	public int resultCode;
	@Expose
	public List<ExhibitorsListChildBean> list;
}
