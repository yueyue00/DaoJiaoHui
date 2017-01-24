package com.gj.gaojiaohui.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 亮点推荐-展商列表
 * 
 * @author zhangt
 * 
 */
public class LiangdianExhibitBean implements Serializable {

	@Expose
	public String id;
	@Expose
	public String name;
	@Expose
	public String exhibitNumber;

}
