package com.jadlwork.utils;

import java.net.HttpURLConnection;
import java.net.URL;

/** 
 * 文件名称为：URLAvailability.java 
 * 文件功能简述： 描述一个URL地址是否有效 
 * @author Jason 
 * @time   2010-9-14  
 *  
 */
public class URLAvailability {
	private static URL url;
	private static HttpURLConnection con;
	private static int TimeOut =  5 * 1000 ;//失效时间(秒)
	private static int state = -1;

	/** 
	 * 功能：检测当前URL是否可连接或是否有效, 
	 * 描述：最多连接网络 2 次, 如果 2 次都不成功，视为该地址不可用 
	 * @param urlStr 指定URL网络地址 
	 * @return URL 
	 */
	public static  String isConnect(String urlStr) {
		String result = "";
		int counts = 0;
		if (urlStr == null || urlStr.length() <= 0) {
			return "地址为空！";
		}
		while (counts < 2) {
			try {
				url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(TimeOut);
				con.setReadTimeout(TimeOut);
				con.setRequestProperty("contentType", "UTF-8");  
				con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");  
				
				state = con.getResponseCode();
			
				if (Integer.toString(state).startsWith("4")) {
					// 4xx 系列错误
					result = "HTTP 错误【"+state+"】";
					counts++;
					continue;
				}
				
				if (Integer.toString(state).startsWith("5")) {
					// 5xx 系列错误
					result = "主机内部 错误【"+state+"】";
					counts++;
					continue;
				}
				result = "";
				break;
			} catch (Exception ex) {
				counts++;
				result = "地址不可用!";
				continue;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		URLAvailability u = new URLAvailability();
		System.out.println(u.isConnect("http://192.168.20.102:8080/singlesignonsy"));
	}
}