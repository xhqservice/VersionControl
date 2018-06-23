package com.jadlsoft.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * 系统配置信息
 * 
 * @author 张方俊 2006/11/16 14:48
 *
 */
public class SysConfigUtils {
	
	private static Properties SYSTEM_CONFIG_PROPERTIES = new Properties();
	
	static {		
		Logger logger = Logger.getLogger(SysConfigUtils.class);
		InputStream is = null;
		try {
			is = SysConfigUtils.class.getClassLoader().getResourceAsStream("struts.properties");
			SYSTEM_CONFIG_PROPERTIES.load(is);
			logger.info("struts.properties配置文件加载成功");
		} catch (IOException e) {
			logger.error("初始化配置文件：struts.properties失败。 "+e);
		}finally{
			try {
				if(is != null)	is.close();
			} catch (IOException e) {
				logger.error(" 配置文件：struts.properties关闭失败。 " + e);
			}
		}		
	}
	
	private SysConfigUtils(){}
	/**
	 * 获取配置信息
	 * @param key 关键字
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getProperty(String key, String defaultValue){
		String property = getProperty(key);
		if(property == null || property.equals("")) return defaultValue;
		else return property;
	}
	
	/**
	 * 从属性文件中获取键对应的值
	 * @param key
	 * @return key对应的value
	 */
	public static String getProperty(String key){
		String val = SYSTEM_CONFIG_PROPERTIES.getProperty(key);
		//return val == null ? null : new String(val.getBytes("ISO8859_1") , "utf-8");
		return val == null ? null : new String(val);
	}
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected static void setProperty(String key , String value){
		
	}
	
}
