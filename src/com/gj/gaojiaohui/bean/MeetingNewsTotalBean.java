package com.gj.gaojiaohui.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 大会资讯实体类
 * 
 * @author lixiaoming
 * 
 */
public class MeetingNewsTotalBean {
	/** 响应码 */
	@Expose
	public int resultCode;
	/** 当前页 */
	@Expose
	public int pageNo;
	/** 总页数 */
	@Expose
	public int pageCount;
	/** 栏目list */
	@Expose
	public List<MeetingNewsTitleBean> type_list;
	/** 第一个栏目对应的数据 */
	@Expose
	public List<MeetingNewsBean> news_list;
	/** 第一个栏目对应的数据 轮播数组 */
	@Expose
	public List<MeetingNewsBean> news_top_list;
}
