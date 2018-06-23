/**
 * <p>Title: SqlTypeConvert </p>
 * <p>Description: 将对象转换成为Sql Type</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-14
*/

package com.jadlsoft.utils;

import java.sql.Types;

public class SqlTypeConvert {
	
	public static int convertClass(Class clazz) {
		if(clazz==int.class || clazz==Integer.class) {
			return Types.INTEGER;
		}
		if(clazz==String.class){
			return Types.VARCHAR;
		}
		if(clazz==java.util.Date.class || clazz==java.sql.Date.class) {
			return Types.DATE;
		}
		/*//数组返回BLOB
		if(clazz==class){
			return Types.BLOB;
		}*/
		return Types.VARCHAR;
	}
}
