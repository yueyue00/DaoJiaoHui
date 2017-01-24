package com.dxmap.indoornavig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Configuration {
	
	private Properties properties;
	private InputStream inputFile;
	public Configuration (String path){
		
		properties = new Properties();
		try {
			inputFile = this.getClass().getResourceAsStream(path); 
			BufferedReader bf = new BufferedReader(new InputStreamReader(inputFile)); 
			properties.load(bf);
			inputFile.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("读取文件失败  原因：找不到文件或文件路径错...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("载入文件失败");
			e.printStackTrace();
		}
	}
	public String getValue(String key){
		if(key == null)
			return null;
		if(properties.containsKey(key)){
			String value = properties.getProperty(key);
			return value;
		}else {
			System.out.println("配置文件中并无该字段");
			return null;
		}
	}

}
