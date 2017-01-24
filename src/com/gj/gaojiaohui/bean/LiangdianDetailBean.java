package com.gj.gaojiaohui.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 亮点推荐 详情的bean
 * 
 * @author zhangt
 * 
 */
public class LiangdianDetailBean implements Serializable {
	@Expose
	public int resultCode;
	@Expose
	public String title;
	@Expose
	public String date;
	@Expose
	public String pic;
	@Expose
	public String value;
	@Expose
	public List<LiangdianExhibitBean> list;
}
