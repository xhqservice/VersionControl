package com.jadlwork.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jadlsoft.utils.JsonUtil;
import com.jadlwork.utils.FileSHA1Utils;
import com.jadlwork.utils.FtpUtils;
import com.jadlwork.utils.httputils.HttpRequestProxy;

public class T {

	public static void main(String[] args) throws Exception {
		T t = new T();
//		t.t1();
//		t.t2();
//		t.t3();
//		t.t4();
		t.t5();
	}
	
	private void t5() throws Exception {
		String ftpHost = "192.168.20.124";
		String ftpUserName = "xiaoF";
		String ftpPassword = "xiaoFTP";
		int ftpPort = 21;
		String ftpPath = "/";
		
		String fileName = "a_123.7z";
		File file = new File("C:\\Users\\Administrator\\Desktop\\a_123.7z");
		InputStream input = new FileInputStream(file);
		FtpUtils.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, input);
	}
	
	private void t4() {
		String s = "/sb/b/b/aaa.b";
		s = s.replace(File.separatorChar+File.separator, File.separator);
		System.out.println(s);
	}
	
	private void t3() throws Exception {
		String url = "http://192.168.20.222:8080/VersionControlCenter/scjBakServer.do";
		
		Map postData = new HashMap();
		postData.put("para", "{qqlx=\"backup_list\",dwdm=\"DW999\"}");
		
		HttpRequestProxy proxy = new HttpRequestProxy();
		String res = proxy.doRequest(url, postData, null, "UTF-8");
		
		Map map = JSONObject.fromObject(res);
		
//		Map map = JsonUtil.parserToMap(res);
		String ftphost = "";
		String ftpuser = "";
		String ftppwd = "";
		int ftpport = 21;
		if ("0".equals(map.get("returntype"))) {
			Map msgMap = (Map) map.get("returnmsg");
			
			//FTP
			Map ftpmap = (Map) msgMap.get("ftpinfo");
			ftphost = ftpmap.get("ftpip").toString();
			ftpuser = ftpmap.get("ftpuser").toString();
			ftppwd = ftpmap.get("ftppwd").toString();
			ftpport = Integer.valueOf(ftpmap.get("ftpport").toString());
			
			//hylist
			JSONArray hylist = (JSONArray) msgMap.get("hylist");
			if (hylist != null && hylist.size()>0) {
				Map hymap = (Map) hylist.get(0);
				String namePath = (String) hymap.get("filename");
				String filename = "";
				String filepath = "";
				if (namePath.contains("\\")) {
					filepath = namePath.substring(0, namePath.lastIndexOf("\\"));
					filename = namePath.substring(namePath.lastIndexOf("\\")+1);
					System.out.println(filepath);
					System.out.println(filename);
				}else if (namePath.contains("/")) {
					filepath = namePath.substring(0, namePath.lastIndexOf("/"));
					filename = namePath.substring(namePath.lastIndexOf("/")+1);
					System.out.println(filepath);
					System.out.println(filename);
				}
				
				String localPath = "C:\\Users\\Administrator\\Desktop\\test";
				FtpUtils.downloadFtpFile(ftphost, ftpuser, ftppwd, ftpport, filepath, localPath, filename);
				
//				for (Map hymap : (List<Map>)hylist) {
//					hymap.get("filename");
//				}
			}
			
		}
	}
	
	private void t1() {
		File f1 = new File("C:\\Users\\Administrator\\Desktop\\test\\1.txt");
		File f2 = new File("C:\\Users\\Administrator\\Desktop\\test\\t2\\t3\\ab.txt");
		boolean b = f1.renameTo(f2);
		if (!b) {
			f2.delete();
			System.out.println(f1.renameTo(f2));
		}
	}
	
	private void t2() throws Exception {
		File f1 = new File("C:\\Users\\Administrator\\Desktop\\test\\丹灵数据接口资源包.7z");
		long l1 = System.currentTimeMillis();
		String str = FileSHA1Utils.getFileSha1(f1.getAbsolutePath());
		long l2 = System.currentTimeMillis();
		System.out.println(str+"---time:" + (l2-l1));
	}
}
