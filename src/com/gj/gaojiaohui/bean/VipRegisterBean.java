package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 嘉宾签到实体类
 */
public class VipRegisterBean {
	@Expose
	public int resultCode;
	/** 当前用户权限是否允许签到 */
	@Expose
	public boolean permission;
	@Expose
	public List<VipRegisterListBean> list;
}
