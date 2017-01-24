package com.dxmap.indoornavig.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZipUtils {
	
	public static class StringUtils{
		public static boolean isEmpty(String str) {
			if (str == null || str.length() == 0)
				return true;
			return false;
		}
	}
	
	/** 
     * ��ѹ��zip�� 
     * @param zipFilePath zip�ļ���ȫ·�� 
     * @param unzipFilePath ��ѹ����ļ������·�� 
     * @param includeZipFileName ��ѹ����ļ������·���Ƿ����ѹ���ļ����ļ�����true-������false-������ 
     */  
    @SuppressWarnings("unchecked")  
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception  
    {  
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath))  
        {  
            return;          
        }  
        File zipFile = new File(zipFilePath);  
        //�����ѹ����ļ�����·������ѹ���ļ����ļ�������׷�Ӹ��ļ�������ѹ·��  
		if (includeZipFileName) {
			String fileName = zipFile.getName();
			if (!StringUtils.isEmpty(fileName)) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			unzipFilePath = unzipFilePath + File.separator + fileName;
		}
        //������ѹ���ļ������·��  
        File unzipFileDir = new File(unzipFilePath);  
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory())  
        {  
            unzipFileDir.mkdirs();  
        }  
          
        //��ʼ��ѹ  
        ZipEntry entry = null;  
        String entryFilePath = null, entryDirPath = null;  
        File entryFile = null, entryDir = null;  
        int index = 0, count = 0, bufferSize = 1024;  
        byte[] buffer = new byte[bufferSize];  
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null;  
        ZipFile zip = new ZipFile(zipFile);  
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zip.entries();  
        //ѭ����ѹ�������ÿһ���ļ����н�ѹ       
        while(entries.hasMoreElements())  
        {  
            entry = entries.nextElement();  
            //����ѹ������һ���ļ���ѹ�󱣴���ļ�ȫ·��  
            entryFilePath = unzipFilePath + File.separator + entry.getName();  
            //������ѹ�󱣴���ļ���·��  
            index = entryFilePath.lastIndexOf(File.separator);  
            if (index != -1)  
            {  
                entryDirPath = entryFilePath.substring(0, index);  
            }  
            else  
            {  
                entryDirPath = "";  
            }             
            entryDir = new File(entryDirPath);  
            //����ļ���·�������ڣ��򴴽��ļ���  
            if (!entryDir.exists() || !entryDir.isDirectory())  
            {  
                entryDir.mkdirs();  
            }  
              
            //������ѹ�ļ�  
            entryFile = new File(entryFilePath);  
            if (entryFile.exists())  
            {  
                //����ļ��Ƿ�����ɾ�������������ɾ���������׳�SecurityException  
                SecurityManager securityManager = new SecurityManager();  
                securityManager.checkDelete(entryFilePath);  
                //ɾ���Ѵ��ڵ�Ŀ���ļ�  
                entryFile.delete();   
            }  
              
            //д���ļ�  
            bos = new BufferedOutputStream(new FileOutputStream(entryFile));  
            bis = new BufferedInputStream(zip.getInputStream(entry));  
            while ((count = bis.read(buffer, 0, bufferSize)) != -1)  
            {  
                bos.write(buffer, 0, count);  
            }  
            bos.flush();  
            bos.close();              
        }  
    }

}
