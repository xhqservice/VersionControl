/**
 * <p>Title: SqlMapUtils.java</p>
 * <p>Description: 加载预先设置好的SQL语句</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 京安丹灵</p>
 * @date 2009-09-08
 * @author zongshuai
 * @version 1.0
*/

package com.jadlsoft.dbutils;

import java.io.File;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SqlMapUtils {
	private String [] sqlmapList = null;
	private static Map sqlMap = new LinkedHashMap();
	private static Logger log = Logger.getLogger(SqlMapUtils.class);
	
	public void setSqlmapList(String[] sqlmapList) {
		this.sqlmapList = sqlmapList; 
	}
	
	public void init(){
		for(int i=0;i<sqlmapList.length;i++){
			loadConfig(sqlmapList[i]);
		}
	}
	
	/**
	 * Description：从XML文件中读取字段的字典配置加入到已有的配置中
	 * @param fileName XML配置文件
	 */
	public void loadConfig(String fileName){
		synchronized (sqlMap) {
			URL url = SqlMapUtils.class.getResource(fileName);
			String configFile = url.getFile();
						
			try {
				configFile = URLDecoder.decode(configFile, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("获取SQL映射文件错误！");
				e.printStackTrace();
			}
			
			File file = new File(configFile.replaceAll("%20", " "));
			if(file.exists() && file.isFile() && file.canRead()) {
				SAXReader reader = new SAXReader();
				try {
					Document doc = reader.read(file);
					Element root = doc.getRootElement();
					String nm = root.getName();
					Iterator item = root.elementIterator();
					while(item.hasNext()) {
						Element sqlitem = (Element) item.next();
						String type = sqlitem.getName().trim().toLowerCase();
						String id = sqlitem.attributeValue("id").trim();
						String sql = sqlitem.getTextTrim();
						
						if("select".equals(type) || "update".equals(type)) {
							sqlMap.put(type + "#" + nm + "." + id, sql);
						}
					}
				} catch (DocumentException e) {
					log.error("读取SQL映射配置文件 " + fileName + " 错误！", e);
				}
			}
		}		
	}
	
	public static String getSql(String type, String id) {
		return (String) sqlMap.get(type + id);
	}
}