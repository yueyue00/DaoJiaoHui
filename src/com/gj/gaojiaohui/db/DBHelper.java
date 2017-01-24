package com.gj.gaojiaohui.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gj.gaojiaohui.bean.ExhibitorsBeanForOffline;
import com.gj.gaojiaohui.bean.MeetingChildBeanForOffline;

/**
 * 数据库辅助操作类
 * 
 * @author zhangt
 * 
 */
public class DBHelper {

	public DBHelper() {

	}

	/**
	 * 获取全部数据集合-展商列表
	 */
	public List<ExhibitorsBeanForOffline> selectDataForExhibitors(SQLiteDatabase db) {
		List<ExhibitorsBeanForOffline> beans = new ArrayList<>();
		Cursor cursor = db.query("exhibitorlist", null, null, null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.moveToNext()) {
				ExhibitorsBeanForOffline bean = new ExhibitorsBeanForOffline();
				bean.id = cursor.getString(cursor.getColumnIndex("id"));
				bean.name = cursor.getString(cursor.getColumnIndex("name"));
				bean.name_en = cursor.getString(cursor.getColumnIndex("name_en"));
				bean.value = cursor.getString(cursor.getColumnIndex("value"));
				bean.value_en = cursor.getString(cursor.getColumnIndex("value_en"));
				bean.position = cursor.getString(cursor.getColumnIndex("position"));
				bean.tel = cursor.getString(cursor.getColumnIndex("tel"));
				bean.lndustry = cursor.getString(cursor.getColumnIndex("lndustry"));
				bean.address = cursor.getString(cursor.getColumnIndex("address"));
				bean.address_en = cursor.getString(cursor.getColumnIndex("address_en"));
				bean.url = cursor.getString(cursor.getColumnIndex("url"));
				beans.add(bean);
			}
		}
		cursor.close();
		return beans;
	}

	/**
	 * 获取全部数据集合-活动/日程列表
	 */
	public List<MeetingChildBeanForOffline> selectDataForActivity(SQLiteDatabase db) {
		List<MeetingChildBeanForOffline> beans = new ArrayList<>();
		Cursor cursor = db.query("activitylist", null, null, null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.moveToNext()) {
				MeetingChildBeanForOffline bean = new MeetingChildBeanForOffline();
				bean.title = cursor.getString(cursor.getColumnIndex("title"));
				bean.title_en = cursor.getString(cursor.getColumnIndex("title_en"));
				bean.type = cursor.getInt(cursor.getColumnIndex("type"));
				bean.tag = cursor.getString(cursor.getColumnIndex("tag"));
				bean.value = cursor.getString(cursor.getColumnIndex("value"));
				bean.value_en = cursor.getString(cursor.getColumnIndex("value_en"));
				bean.start_date = cursor.getString(cursor.getColumnIndex("start_date"));
				bean.end_date = cursor.getString(cursor.getColumnIndex("end_date"));
				bean.id = cursor.getString(cursor.getColumnIndex("id"));
				bean.site = cursor.getString(cursor.getColumnIndex("site"));
				bean.site_en = cursor.getString(cursor.getColumnIndex("site_en"));
				bean.longitude = cursor.getString(cursor.getColumnIndex("longitude"));
				bean.latitude = cursor.getString(cursor.getColumnIndex("latitude"));
				beans.add(bean);
			}
		}
		cursor.close();
		return beans;
	}

	/**
	 * 模糊搜索 -展商
	 * 
	 * @param db
	 * @param column
	 *            搜索项
	 * @param searcherFilter
	 *            约束值
	 * @return
	 */
	public List<ExhibitorsBeanForOffline> fuzzySearchForExhibitors(SQLiteDatabase db, String column, String searcherFilter) {
		List<ExhibitorsBeanForOffline> beans = new ArrayList<>();
		Cursor cursor = db.query("exhibitorlist", null, column + " like '%" + searcherFilter + "%'", null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.moveToNext()) {
				ExhibitorsBeanForOffline bean = new ExhibitorsBeanForOffline();
				bean.id = cursor.getString(cursor.getColumnIndex("id"));
				bean.name = cursor.getString(cursor.getColumnIndex("name"));
				bean.name_en = cursor.getString(cursor.getColumnIndex("name_en"));
				bean.value = cursor.getString(cursor.getColumnIndex("value"));
				bean.value_en = cursor.getString(cursor.getColumnIndex("value_en"));
				bean.position = cursor.getString(cursor.getColumnIndex("position"));
				bean.tel = cursor.getString(cursor.getColumnIndex("tel"));
				bean.lndustry = cursor.getString(cursor.getColumnIndex("lndustry"));
				bean.address = cursor.getString(cursor.getColumnIndex("address"));
				bean.address_en = cursor.getString(cursor.getColumnIndex("address_en"));
				bean.url = cursor.getString(cursor.getColumnIndex("url"));
				beans.add(bean);
			}
		}
		cursor.close();
		return beans;
	}

	/**
	 * 模糊搜索 -活动/日程
	 * 
	 * @param db
	 * @param column
	 *            搜索项
	 * @param searcherFilter
	 *            约束值
	 * @return
	 */
	public List<MeetingChildBeanForOffline> fuzzySearchForActivity(SQLiteDatabase db, String column, String searcherFilter) {
		List<MeetingChildBeanForOffline> beans = new ArrayList<>();
		Cursor cursor = db.query("activitylist", null, column + " like '%" + searcherFilter + "%'", null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.moveToNext()) {
				MeetingChildBeanForOffline bean = new MeetingChildBeanForOffline();
				bean.title = cursor.getString(cursor.getColumnIndex("title"));
				bean.title_en = cursor.getString(cursor.getColumnIndex("title_en"));
				bean.type = cursor.getInt(cursor.getColumnIndex("type"));
				bean.tag = cursor.getString(cursor.getColumnIndex("tag"));
				bean.value = cursor.getString(cursor.getColumnIndex("value"));
				bean.value_en = cursor.getString(cursor.getColumnIndex("value_en"));
				bean.start_date = cursor.getString(cursor.getColumnIndex("start_date"));
				bean.end_date = cursor.getString(cursor.getColumnIndex("end_date"));
				bean.id = cursor.getString(cursor.getColumnIndex("id"));
				bean.site = cursor.getString(cursor.getColumnIndex("site"));
				bean.site_en = cursor.getString(cursor.getColumnIndex("site_en"));
				bean.longitude = cursor.getString(cursor.getColumnIndex("longitude"));
				bean.latitude = cursor.getString(cursor.getColumnIndex("latitude"));
				beans.add(bean);
			}
		}
		cursor.close();
		return beans;
	}

}
