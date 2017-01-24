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
			System.out.println("��ȡ�ļ�ʧ��  ԭ���Ҳ����ļ����ļ�·����...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("�����ļ�ʧ��");
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
			System.out.println("�����ļ��в��޸��ֶ�");
			return null;
		}
	}

}
