package com.jadlwork.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

import com.jadlwork.utils.FileSHA1Utils;
import com.jadlwork.utils.FileUtils;
import com.jadlwork.utils.ZipUtils;
import com.jadlwork.utils.zip.ZipEntry;
import com.jadlwork.utils.zip.ZipFile;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: T
 * @描述: TODO
 * @作者: lcx
 * @时间: 2018-3-13 下午6:06:03
 */
public class T {

	public static void main(String[] args) throws Exception {
		T t = new T();
//		t.t5();
//		t.t1();
//		t.t2();
		
		t.t3();
	}
	
	private void t2() throws Exception {
		File trueWar = new File("C:\\Users\\Administrator\\Desktop\\数据源测试\\sct\\s_ServiceCenter.war");
		File falseWar = new File("C:\\Users\\Administrator\\Desktop\\数据源测试\\sct\\f_ServiceCenter.war");
		
		String s1 = FileSHA1Utils.getFileSha1(trueWar.getAbsolutePath());
		String s2 = FileSHA1Utils.getFileSha1(falseWar.getAbsolutePath());
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s1.equals(s2));
		
	}
	
	private void t1() throws IOException, InterruptedException {
		String warsrc = "C:\\Users\\Administrator\\Desktop\\数据源测试\\commonTest.war";
//		String sjysrc = "C:\\Users\\Administrator\\Desktop\\数据源测试\\newsjy.xml";
		File warFile = new File(warsrc);
		
//		String s1 = FileSHA1Utils.getFileSha1(warsrc);
//		System.out.println("--------原始war包sha1值：--------"+s1);
		
//		File sjyFile = new File(sjysrc);
		
		//1、解压
		String warroot = ZipUtils.decompressZip(warFile.getAbsolutePath(), null, true);
		
		Thread.sleep(10000);
		
		//2、替换
//		File contextFile = new File(warroot, "META-INF"+File.separator+"context.xml");
//		FileUtils.copyFile(sjysrc, contextFile.getAbsolutePath());
		
		//3、打包
//		warFile.delete();
		ZipUtils.compressZip(warroot, warsrc);
		
//		String s2 = FileSHA1Utils.getFileSha1(warsrc);
//		System.out.println("--------解压再打包后war包sha1值：--------"+s2);
		
//		System.out.println(s1.equals(s2));
		
	}
	
	private void t5() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\数据源测试\\ServiceCenter#1.0.war");
		ZipUtils.decompressZip(file.getAbsolutePath(), null, true);
		
	}
	
	private void t3() {
		String zippaht = "C:\\Users\\Administrator\\Desktop\\测试打包\\commonTest.war";
		String addpath = "C:\\Users\\Administrator\\Desktop\\测试打包\\abc.txt";
		T.AddFileToZip(zippaht, addpath);
	}
	
	public static void AddFileToZip(String zippath, String addFilePath) {
		
		File addFile = new File(addFilePath);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zippath, "gb2312");
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				String name = zipEntry.getName();
				System.out.println(name);
						
				if (zipEntry.isDirectory()) {
					name = name.substring(0, name.length() - 1);
				} else {
				}
			}
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	
}
