package com.jadlsoft.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title:QueryUtils.java </p>
 * <p>Description: 根据Map生成查询条件</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @date 2006-09-13
 * @author Zong
 * @version 1.0
*/

public class QueryUtils {

	/**
	 * Description: 根据Map生成查询条件
	 * @param map:map的key与数据库表字段必须对应,map的value为条件值
	 * return 查询条件: AND 1 = 1 AND 2 = 2
	 */
	public static String getCondition(Map map) {
		StringBuffer sql = new StringBuffer();
		String fieldName;
		
		if ((map == null) || (map.size() == 0)) {
			return sql.toString();
		}
		Set keys = map.keySet();
		Iterator item = keys.iterator();
		while (item.hasNext()) {
			fieldName = (String)item.next();
			if ((map.get(fieldName) != null) && (!"".equals(map.get(fieldName).toString()))){
				sql.append(" AND ").append(fieldName.toUpperCase()).append(" = '");
				sql.append(map.get(fieldName));
				sql.append("'");
			}
		}
		item = null;
		keys = null;
		return sql.toString();
	}
	
	/**
	 * Description: 根据值Map与数据库字段数组生成查询条件
	 * @param map:map的value为条件值
	 * @param fields:存储数据库表字段
	 * return 查询条件: AND 1 = 1 AND 2 = 2
	 */
	public static String getCondition(Map map, String [] fields) {
		StringBuffer sql = new StringBuffer();
		int i = 0;
		int len = 0;
		if (fields != null) len = fields.length;
		String mapKey;

		if ((map == null) || (fields == null) || (map.size() == 0) || (fields.length == 0)) {
			return sql.toString();
		}
		Set keys = map.keySet();
		Iterator item = keys.iterator();
		while (item.hasNext()) {
			if (i == len) {
				return sql.toString();
			}
			mapKey = (String)item.next();
			if ((map.get(mapKey) != null) && (!"".equals(map.get(mapKey).toString()))){
				sql.append(" AND ").append(fields[i].toUpperCase()).append(" = '");
				sql.append(map.get(mapKey));
				sql.append("'");
			}
			i++;
		}
		item = null;
		keys = null;
		return sql.toString();
	}
}
