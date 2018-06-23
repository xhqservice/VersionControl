package com.jadlsoft.utils;

import com.jadlsoft.utils.SysConfigUtils;

/**
 * 接口调用方法
 * @作者：吴家旭
 * @时间：Nov 7, 2016 5:59:50 PM
 */
public class InterfaceUtils {
	
	/**
	 * URI头
	 */
	private static final String URI_HEADER = "uri.";//正式环境头部
	private static final String URI_HEADER_TEST = "uri.test.";//测试环境头部
	private static final String ISQYZSHJ = "ISQYZSHJ";//是否启用正式环境  0：启用  1：不启用
	private static final String LOCAL = "127.0.0.1:8080";//本地环境
	
	/**
	 * 获取KEY获取接口地址
	 * @参数：@param key
	 * @参数：@return
	 * @返回值：String
	 */
	public static String getUrl(String key){
		if(key == null || "".equals(key)){
			return "";
		}
		String property = SysConfigUtils.getProperty(key);
		if(key.indexOf(".") != -1 && property != null && property.indexOf(URI_HEADER) != -1){
			String appkey = key.substring(0,key.indexOf("."));
			return getRealUrl(property,appkey);
		}
		return property;
	}
	
	/**
	 * 列表查询获取webservice接口
	 * @参数：@param key
	 * @参数：@return
	 * @返回值：String
	 */
	public static String getUrlForSearch(String url){
		if(url.indexOf(URI_HEADER) != -1){
			String appkey = url.substring(url.indexOf(URI_HEADER)+4,url.indexOf("]"));
			return getRealUrl(url,appkey);
		}
		return url;
	}
	
	/**
	 * 获取真正的url
	 * @param appkey2 
	 * @return 
	 * @参数：@param url
	 * @参数：@param type
	 * @返回值：void
	 */
	private static String getRealUrl(String url, String appkey) {
		String uriKey = "";
		//判断运行环境
		String jreVal = SysConfigUtils.getProperty(ISQYZSHJ);
		String replacKey = URI_HEADER+appkey;
		if(jreVal != null && jreVal.equals("0")){
			uriKey = URI_HEADER+appkey;
		}else{
			uriKey = URI_HEADER_TEST+appkey;
		}
		//获取替换的uri
		String uriVal = SysConfigUtils.getProperty(uriKey);
		if(uriVal == null || "".equals(uriVal)){
			uriVal = LOCAL;
		}
		//System.out.println("url:"+property.replace("["+replacKey+"]", uriVal));
		return url.replace("["+replacKey+"]", uriVal);
		
	}

	
}
