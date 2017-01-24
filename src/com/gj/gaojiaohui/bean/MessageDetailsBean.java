package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 留言详情--bean类
 * @author Administrator
 *
 */
public class MessageDetailsBean {

	@Expose
	public int resultCode;
	@Expose
	public List<MessageDetailsChildBean> list;
}
