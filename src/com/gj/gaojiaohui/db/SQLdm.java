package com.gj.gaojiaohui.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gj.gaojiaohui.abconstant.GloableConfig;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 * 
 * @author Big_Adamapple
 * 
 */
public class SQLdm {

	private String dbName;

	/** 数据库存储路径 + 名字 */
	String filePath;

	SQLiteDatabase database;

	/**
	 * 
	 * 
	 * @param dbName 数据库文件名字,包含后缀
	 */
	public SQLdm(String dbName) {
		this.dbName = dbName;
		filePath = GloableConfig.DB_PATH + "/" + dbName;
	}

	public SQLiteDatabase openDatabase(Context context) {
		File jhPath = new File(filePath);
		// 查看数据库文件是否存在
		if (jhPath.exists()) {
			Log.i("fate", "存在数据库");
			// 存在则直接返回打开的数据库
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else {
			// 不存在先创建文件夹
			File path = new File(GloableConfig.DB_PATH);
			Log.i("fate", "pathStr=" + path);
			if (path.mkdir()) {
				Log.i("fate", "创建成功");
			} else {
				Log.i("fate", "创建失败");
			}
			try {
				// 得到资源
				AssetManager am = context.getAssets();
				// 得到数据库的输入流
				InputStream is = am.open(dbName);
				// 用输出流写到SDcard上面
				FileOutputStream fos = new FileOutputStream(jhPath);
				Log.i("fate", "fos=" + fos);
				Log.i("fate", "jhPath=" + jhPath);
				// 创建byte数组 用于1KB写一次
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// 最后关闭就可以了
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			// 如果没有这个数据库 我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
			return openDatabase(context);
		}
	}
}