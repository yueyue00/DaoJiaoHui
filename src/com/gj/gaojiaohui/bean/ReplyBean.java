package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 回复的bean
 * @author zhangt
 *
 */
public class ReplyBean implements Serializable {
	@Expose
	public String name;
	@Expose
	public String value;
}
