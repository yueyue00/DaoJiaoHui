package com.gj.gaojiaohui.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static String streamToStr(InputStream inputStream) throws IOException{
		ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
		byte [] buffer = new byte [1024] ;
		int len=0;
		while((len=inputStream.read(buffer))!=-1){
			arrayOutputStream.write(buffer, 0, len);
		}
		return arrayOutputStream.toString();
	}
}
