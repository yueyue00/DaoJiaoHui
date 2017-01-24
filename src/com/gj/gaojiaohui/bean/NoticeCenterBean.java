package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 通知中心--bean类
 * 
 * @author lixiaoming
 * 
 */
public class NoticeCenterBean {
	@Expose
	public int resultCode;
	@Expose
	public List<NoticeCenterChildBean> list;
}
