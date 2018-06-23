/**
 * <p>Title: LowcaseColumnRowMapper</p>
 * <p>Description: 实现小写列名的RowMapper接口 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-15
*/

package com.jadlsoft.dbutils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class LowcaseColumnRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map mapOfColValues = new LinkedHashMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(rsmd.getColumnName(i));
			Object obj = getColumnValue(rs, i);
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}
	
	protected String getColumnKey(String columnName) {
		return columnName.toLowerCase();
	}
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		Object value = rs.getObject(index);
		if(value instanceof String) {
			return ((String)value).trim();
		} else {
			//if(value instanceof Date) {
			//	return new SimpleDateFormat("yyyy-MM-dd").format(value);
			//} else {
				return value;
			//}
		}
	}

}
