package com.gj.gaojiaohui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gheng.exhibit.http.body.response.News_LieBiao;
import com.google.gson.annotations.Expose;

/**
 * 首页的数据的bean
 * 
 * @author zhangt
 * 
 */
public class HomePageBean implements Serializable {

	@Expose
	public int resultCode;
	@Expose
	public Boolean is_msg;
	@Expose
	public HomePageTopBean top;
	/** 大会通知 */
	@Expose
	public List<HomePageInformListBean> inform_list = new ArrayList<>();
	/** 亮点推荐 */
	@Expose
	public List<HomePageRecommendListBean> recommend_list = new ArrayList<>();
	/** 展馆导航 */
	@Expose
	public String exhibition;
	/** 新闻滚动头图 */
	@Expose
	public List<HomePageNewsTopBean> news_top_list = new ArrayList<>();
	/** 大会资讯 */
	@Expose
	public List<HomePageNewsListBean> news_list = new ArrayList<>();

}
