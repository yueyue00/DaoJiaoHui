package com.gj.gaojiaohui.bean;

import java.util.ArrayList;

public class ZhanShangBean {
	public String code;
	public String message;
	public int pagecount;
	public int pagenum;
	public ArrayList<Info> info;
	public class Info{
		public String id;
		public String memo;
		public String name;
	}
}
