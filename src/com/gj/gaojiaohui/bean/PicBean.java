package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 缩略图和大图的bean
 * 
 * @author zhangt
 * 
 */
public class PicBean implements Serializable{
	@Expose
	public String pic;
	@Expose
	public String pic_thumbnail;
}
