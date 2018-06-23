package com.jadlsoft.dbutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jadlsoft.utils.ArrayUtils;
import com.jadlsoft.utils.DomainBeanUtils;
import com.jadlsoft.utils.SystemConstants;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * <p>
 * Title: SQLHelper
 * </p>
 * <p>
 * Description: 生成各种SQL语句的助手类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: 京安丹灵
 * </p>
 * 
 * @author Administrator
 * @version 1.0 2007-6-19
 */
public class SQLHelper {

	/**
	 * 
	 */
	public static String getSequencesSQL(String seqName) {
		String sql = "";
		sql = "select " + seqName.trim() + ".nextval from dual";
		return sql;
	}

	public static String getExcuteTableName(Class cls_pojo) {
		Object tableobj = DomainBeanUtils.getStaticPropertyValue(cls_pojo,
				SystemConstants.DB_TABLENAME);
		if (tableobj != null && tableobj.getClass() == String.class) {
			return (String) tableobj;
		}
		String className = cls_pojo.getName();
		String[] className_ = className.split("\\.");
		String table = null;
		if (className_.length > 1) {
			table = "T_" + className_[className_.length - 2].toLowerCase()
					+ "_" + className_[className_.length - 1].toLowerCase();
		} else {
			table = ((String) className_[className_.length - 1]).toLowerCase();
		}
		return table;
	}

	public static String getExcuteTableName(Object cls_pojo) {
		if(cls_pojo instanceof Class){
			return getExcuteTableName((Class)cls_pojo);
		}
		Object table = DomainBeanUtils.getPropertyValue(cls_pojo,
				SystemConstants.DB_TABLENAME);
		if (table != null && table.getClass() == String.class) {
			return (String) table;
		} else {
			return getExcuteTableName(cls_pojo.getClass());
		}
	}

	public static String getExcuteTableName(Map map) {
		String table = null;
		if (map.containsKey(SystemConstants.DB_TABLENAME)) {
			Object tableobj = map.get(SystemConstants.DB_TABLENAME);
			if(tableobj instanceof String){
				return (String)tableobj;
			}else{
				table = getExcuteTableName(tableobj);
			}
		}
		return table;
	}

	public static String getObjectPkFields(Object cls_pojo) {
		Object pkfields = DomainBeanUtils.getPropertyValue(cls_pojo,
				SystemConstants.DB_TABLEPKFIELDS);
		if (pkfields != null && pkfields.getClass() == String.class) {
			return (String) pkfields;
		} else {
			return null;
		}
	}
	
	public static String getObjectPkFields(Class cls_pojo) {
		Object pkfields = DomainBeanUtils.getStaticPropertyValue(cls_pojo,
				SystemConstants.DB_TABLEPKFIELDS);
		if (pkfields != null && pkfields.getClass() == String.class) {
			return (String) pkfields;
		} else {
			return null;
		}
	}

	public static String getCountSQL(String sql) {
		return "select count(*) as count_ from ( " + sql + " ) ";
	}

	public static String getInsertSQL(Object cls_pojo) {
		return getInsertSQL(cls_pojo, (String[]) null);
	}

	public static String getInsertSQL(Object cls_pojo, String[] insertFields) {
		StringBuffer ins_sql = new StringBuffer();
		StringBuffer value_sql = new StringBuffer();
		String table_name = getExcuteTableName(cls_pojo);
		try {
			Field[] fields = cls_pojo.getClass().getDeclaredFields();

			ins_sql.append("insert into " + table_name + "(");
			value_sql.append("(");

			for (int i = 0; i < fields.length; i++) {
				String columnName = fields[i].getName();
				if (!columnName.toLowerCase().equals("serialversionuid")
						&& !isSystemIdentity(columnName)
						&& (insertFields == null || ArrayUtils.containValue(
								insertFields, columnName))) {
					Object value = DomainBeanUtils.getPropertyValue(cls_pojo,
							columnName);
					if (value != null && !value.toString().equals("")) {
						ins_sql.append(columnName + ",");
						value_sql.append(":" + columnName + ",");
					}
				}
			}
			if (ins_sql.charAt(ins_sql.length() - 1) == ',') {
				ins_sql.replace(ins_sql.length() - 1, ins_sql.length(), ")");
				value_sql.replace(value_sql.length() - 1, value_sql.length(),
						")");
			}

			ins_sql.append(" values " + value_sql.toString());
		} catch (Exception e) {
		}
		return ins_sql.toString();
	}

	public static String getInsertSQL(Map data) {
		StringBuffer ins_sql = new StringBuffer();
		StringBuffer value_sql = new StringBuffer();
		String table_name = getExcuteTableName(data);
		try {
			ins_sql.append("insert into " + table_name + "(");
			value_sql.append("(");

			Iterator item = data.keySet().iterator();
			while (item.hasNext()) {
				String columnName = (String) item.next();
				if (!isSystemIdentity(columnName)) {
					// Object value = data.get(columnName);
					// if (value != null && !value.toString().equals("")) {
					ins_sql.append(columnName + ",");
					value_sql.append(":" + columnName + ",");
					// }
				}
			}
			if (ins_sql.charAt(ins_sql.length() - 1) == ',') {
				ins_sql.replace(ins_sql.length() - 1, ins_sql.length(), ")");
				value_sql.replace(value_sql.length() - 1, value_sql.length(),
						")");
			}

			ins_sql.append(" values " + value_sql.toString());
		} catch (Exception e) {

		}
		return ins_sql.toString();
	}

	public static String getDeleteSQL(String tableName, Map condition) {
		StringBuffer delsql = new StringBuffer("delete from " + tableName);
		if (condition != null && condition.size() > 0) {
			delsql.append(" where ");
			Set keys = condition.keySet();
			Iterator item = keys.iterator();
			while (item.hasNext()) {
				String colname = (String) item.next();
				if (!isSystemIdentity(colname)) {
					Object valuetemp = condition.get(colname);
					if (valuetemp.toString().indexOf('%') > 0) {
						delsql.append(colname + " like :" + colname + " and ");
					} else {
						delsql.append(colname + " = :" + colname + " and ");
					}
				}
			}
			delsql.delete(delsql.length() - 4, delsql.length());
		}

		return delsql.toString();
	}

	public static String getDeleteSQL(Map condition) {
		String tableName = getExcuteTableName(condition);
		if (tableName != null && tableName.trim().length() > 0) {
			// condition.remove(DbConstants.DB_TABLENAME);
			return getDeleteSQL(tableName, condition);
		} else {
			return null;
		}
	}

	public static String getDeleteSQL(Object cls_pojo, String fields) {
		String table_name = getExcuteTableName(cls_pojo);
		Map properties = DomainBeanUtils.getPropertiesMap(cls_pojo, fields);
		return getDeleteSQL(table_name, properties);
	}

	public static String getDeleteSQL(Object cls_pojo) {
		String fields = getObjectPkFields(cls_pojo);
		return getDeleteSQL(cls_pojo, fields);
	}

	public static String getUpdateSQL(Map condition) {
		String tableName = getExcuteTableName(condition);
		String condfields = (String) condition
				.get(SystemConstants.DB_CONDITIONFIELDS);
		// condition.remove(DbConstants.DB_TABLENAME);
		// condition.remove(DbConstants.DB_CONDITIONFIELDS);

		return getUpdateSQL(condition, tableName, condfields);
	}

	private static String getUpdateSQL(Map condition, String tableName,
			String condfields) {
		if (tableName != null && tableName.trim().length() > 0) {
			StringBuffer updatesql = new StringBuffer("update " + tableName);
			if (condition != null && condition.size() > 0) {
				updatesql.append(" set ");
				Set keys = condition.keySet();
				Iterator item = keys.iterator();
				while (item.hasNext()) {
					String colname = (String) item.next();
					// String valuetemp = (String)condition.get(colname);
					if (!isSystemIdentity(colname)) {
						updatesql.append(colname + " = :" + colname + ",");
					}

				}
				updatesql.delete(updatesql.length() - 1, updatesql.length());
				if (condfields != null && condfields.trim().length() > 0) {
					updatesql.append(" where ");
					String[] conds = condfields.split(",");
					for (int i = 0; i < conds.length; i++) {
						updatesql.append(conds[i] + "= :" + conds[i] + " and ");
					}
					updatesql
							.delete(updatesql.length() - 4, updatesql.length());
				}
			}

			return updatesql.toString();
		} else {
			return null;
		}
	}

	public static String getUpdateSQL(Object cls_pojo) {
		return getUpdateSQL(cls_pojo, getObjectPkFields(cls_pojo));
	}

	public static String getUpdateSQL(Object cls_pojo, String pk_names) {
		String table_name = getExcuteTableName(cls_pojo);
		return getUpdateSQL(cls_pojo, table_name, pk_names);
	}

	public static String getUpdateSQL(Object cls_pojo, String table_name,
			String pk_names) {
		return getUpdateSQL(cls_pojo, table_name, pk_names, null);
	}

	public static String getUpdateSQL(Object cls_pojo, String table_name,
			String pk_names, String update_fields) {
		StringBuffer up_sql = new StringBuffer();
		StringBuffer condition_ = new StringBuffer(" where 1=1 ");

		if (table_name == null || table_name.trim().length() == 0) {
			table_name = getExcuteTableName(cls_pojo);
		}

		if (pk_names == null || pk_names.trim().length() == 0) {
			pk_names = getObjectPkFields(cls_pojo);
		}
		String update_fieldsstemp = null;

		if (update_fields == null || update_fields.trim().length() == 0) {
			update_fields = ArrayUtils.join(DomainBeanUtils
					.getProperties(cls_pojo), ",");
		} else {
			update_fieldsstemp = "," + update_fields + ",";
		}
		String pk_namestemp = "," + pk_names + ",";

		try {
			String[] fields = (update_fields + "," + pk_names).split(",");

			for (int i = 0; i < fields.length; i++) {
				if (pk_namestemp.indexOf("," + fields[i] + ",") >= 0) {
					condition_.append(" and " + fields[i] + "=:" + fields[i]);
				} else {
					if (!isSystemIdentity(fields[i])
							&& (update_fieldsstemp == null || update_fieldsstemp
									.indexOf("," + fields[i] + ",") >= 0)) {
						Object value = DomainBeanUtils.getPropertyValue(
								cls_pojo, fields[i]);
						if (update_fieldsstemp != null || value != null) {
							up_sql.append(fields[i] + "=:" + fields[i] + ",");
						}else{
							String classTypeName = ((Class)(DomainBeanUtils.getPropertiesClassMap(cls_pojo.getClass()).get(fields[i]))).getName();
							if("java.math.BigDecimal".equalsIgnoreCase(classTypeName)){
								up_sql.append(fields[i] + "=null,");
							}
						}
					}
				}
			}

			up_sql.delete(up_sql.length() - 1, up_sql.length());
		} catch (Exception e) {
		}

		return "update " + table_name + " set " + up_sql.toString()
				+ condition_.toString();
	}

	public static String getUpdateSQL(Object cls_pojo, String table_name,
			String[] pk_names, String[] update_fields) {
		StringBuffer up_sql = new StringBuffer();
		StringBuffer condition_ = new StringBuffer(" where 1=1 ");

		try {
			String[] fields = (Arrays.toString(update_fields) + "," + Arrays.toString(pk_names)).split(",");

			for (int i = 0; i < fields.length; i++) {
				if (ArrayUtils.containValue(pk_names, fields[i])) {
					condition_.append(" and " + fields[i] + "=:" + fields[i]);
				} else {
					if (update_fields == null
							|| ArrayUtils
									.containValue(update_fields, fields[i])) {
						up_sql.append(fields[i] + "=:" + fields[i] + ",");
					}
				}
			}

			up_sql.delete(up_sql.length() - 1, up_sql.length());
		} catch (Exception e) {
		}

		return "update " + table_name + " set " + up_sql.toString()
				+ condition_.toString();
	}

	public static String getSelectSQL(Map condition, String table_name) {
		return "select * " + getConditionSQL(condition, table_name);
	}

	public static String getSelectSQL(List conditions, String table_name) {
		return "select * " + getConditionSQL(conditions, table_name);
	}

	public static String getGroupSelectSQL(List conditions, String table_name,
			String groupColumns) {
		return getGroupSelectSQL(conditions, table_name, groupColumns,
				"count(*) record_count");
	}

	public static String getGroupSelectSQL(List conditions, String table_name,
			String groupColumns, String groupedColumns) {
		return "select " + groupColumns + "," + groupedColumns
				+ " record_count " + getConditionSQL(conditions, table_name)
				+ " group by " + groupColumns;
	}

	public static String getSelectSQL(Object cls_pojo, String table_name) {
		return "select * " + getConditionSQL(cls_pojo, table_name);
	}

	public static String getCountSQL(Map condition, String table_name) {
		return "select count(*) " + getConditionSQL(condition, table_name);
	}

	public static String getConditionSQL(Map condition, String table_name) {
		return getConditionSQL(condition, table_name, false);
	}

	public static String getConditionSQL(Map condition, String table_name,
			boolean ignorNull) {
		StringBuffer conditionstr = new StringBuffer(" where 1=1 ");
		String orderby = "";
		try {
			Set keys = condition.keySet();
			Iterator item = keys.iterator();
			while (item.hasNext()) {
				String colname = (String) item.next();
				Object valuetemp = condition.get(colname);
				if (colname.equals(SystemConstants.DB_ORDERBY)) {
					orderby = " order by " + valuetemp;
				} else {
					if ((valuetemp != null || !ignorNull)
							&& !isSystemIdentity(colname)) {
						conditionstr.append(" and "
								+ getOneCondition(colname, valuetemp));
					}
				}
			}

		} catch (Exception e) {
		}

		return "from " + table_name + conditionstr.toString() + orderby;
	}

	public static boolean isSystemIdentity(String column) {
		return column.equals(SystemConstants.DB_TABLENAME)
				|| column.equals(SystemConstants.DB_TABLEPKFIELDS)
				|| column.toLowerCase().equals("serialversionuid")
				|| column.equals(SystemConstants.DB_ORDERBY)
				|| column.equals(SystemConstants.DB_BLOBFIELD)
				|| column.equals(SystemConstants.DB_CONDITIONFIELDS);
	}

	/**
	 * @param conditions
	 *            条件中，如果操作符中包括逗号，则继续处理
	 * @param table_name
	 * @return
	 */
	public static String getConditionSQL(List conditions, String table_name) {
		StringBuffer conditionstr = new StringBuffer(" where 1=1 ");
		String orderby = "";
		List keylist = new ArrayList();
		List appendCondition = new ArrayList();
		Object removeObject = new Object();
		
		try {
			Iterator item = conditions.iterator();
			while (item.hasNext()) {
				Map condition = (Map) item.next();
				String colname = (String) condition.get("column");
				String operation = (String) condition.get("operation");
				if (!isSystemIdentity(colname)) {
					String colvalue = colname;
					if (keylist.contains(colname)) {
						for (int i = 0; i < 100; i++) {
							if (!keylist.contains(colname + "_" + i + "_")) {
								colvalue = colname + "_" + i + "_";
								break;
							}
						}
					}
					keylist.add(colvalue);
					// danger: ',' could be inputed by user
					if (operation.indexOf(',') > 0) {
						String[] ops = operation.split(",");
						String cond = colname + " " + ops[0] + " :" + colvalue;
						for (int i = 1; i < ops.length; i++) {
							cond += " or " + colname + " " + ops[i];
						}
						conditionstr.append(" and (" + cond + ")");
					} else if("or".equalsIgnoreCase(operation)){
						/*
						 * 张方俊 2010-09-08 增加对or操作符号的支持。目前仅支持list中的requirecondition
						 * ZFJ_TODO 注意：','可能导致问题
						 */
						String value = (String) condition.get("value");
						String[] values = value.split(",");
						StringBuffer cond = new StringBuffer();
						//cond.append(colname).append(" like '").append(values[0]).append("'");
						
						cond.append(colname).append(" like :").append(colvalue);
						appendCondition.add(DaoUtils.createCondition(colvalue, "or", values[0] + "%"));
						
						for (int i = 1; i < values.length; i++) {
							for (int j = 0; j < 100; j++) {
								if (!keylist.contains(colname + "_" + j + "_")) {
									colvalue = colname + "_" + j + "_";
									keylist.add(colvalue);
									break;
								}
							}
							cond.append(" or ").append(colname).append(" like :").append(colvalue);
							appendCondition.add(DaoUtils.createCondition(colvalue, "or", values[i] + "%"));
						}
						conditionstr.append(" and (" + cond + ")");
						removeObject = condition;
					}else {
						conditionstr.append(" and " + colname + " " + operation
								+ " :" + colvalue);
					}
				} else {
					if (colname.equals(SystemConstants.DB_ORDERBY)) {
						orderby = " order by " + condition.get("value");
					}
				}
			}
		} catch (Exception e) {
		}
		for(int i=0;i<appendCondition.size();i++){
			conditions.add(appendCondition.get(i));
		}
		conditions.remove(removeObject);
		
		String local = "from " + table_name + conditionstr.toString() + orderby;
		return local;
	}

	public static String getConditionSQL(Object cls_pojo, String table_name) {
		Map condition = DomainBeanUtils.getPropertiesMap(cls_pojo);
		return getConditionSQL(condition, table_name, true);
	}

	public static String getListSqlFromSql(String sql, int skip, int count) {
		if (sql == null || sql.length() == 0) {
			return null;
		}

		sql = "SELECT * "
			  + " FROM (SELECT ROWNUM row_id, tab.*  "
			  + "     FROM (SELECT   tab2.* "
			  + "               FROM (SELECT tab1.*, ROWNUM rowidd "
			  + "                       FROM ( " + sql + " ) tab1) tab2 "
			  + "           ORDER BY rowidd) tab "
			  + "    WHERE ROWNUM <= :page_maxcount) ";
		
		if (skip > 0) {
			/*
			 * 张方俊 于 2008-07-30 增加。
			 * 分页查询的语句如下：
			 * SELECT * FROM (SELECT ROWNUM ROW_ID,  TAB.* FROM ( YOURSQL ) TAB )  WHERE ROW_ID <= ? AND ROW_ID > ?
			 */
			/*
			 * 张方俊 于 2009-11-12 修改：在sql语句中增加查询条数的限制。 
			 */
			/**modified by zhaohuibin 2013-06-17 解决未排序导致翻页时记录顺序错乱的问题。
			return "SELECT * FROM (SELECT ROWNUM ROW_ID,  TAB.* FROM ( " + sql + "  ) TAB where ROWNUM <= :page_maxcount order by rowid) " 
					+ "  WHERE ROW_ID <= :page_maxcount AND ROW_ID > :page_skipcount ";*/
			return sql + " WHERE ROW_ID <= :page_maxcount AND ROW_ID > :page_skipcount ";
			
			/*
			 * 张方俊 于 2008-07-30 屏蔽
			String ordersql = null;
			String mysql = null;
			if (sql.indexOf("ORDER BY") > 0) {
				ordersql = sql.substring(sql.indexOf("ORDER BY"));
				mysql = sql.substring(0, sql.indexOf("ORDER BY"));
			} else {
				ordersql = "";
				mysql = sql;
			}
			if (ordersql.length() == 0) {
				if (mysql.toUpperCase().indexOf("WHERE") > 0) {
					mysql = mysql + " AND ";
				} else {
					mysql = mysql + " WHERE ";
				}
			}
			if (ordersql.length() == 0) {
				return "SELECT * FROM (SELECT ROWNUM row_id," + "TAB.* FROM ("
						+ mysql + "ROWNUM<=:page_maxcount) TAB) "
						+ "where row_id>:page_skipcount";
			} else {
				return "SELECT * FROM (SELECT ROW_NUMBER() OVER("
						+ ordersql
						+ ") row_id,"
						+ "TAB.* FROM ("
						+ mysql
						+ ") TAB) WHERE row_id<=:page_maxcount AND row_id>:page_skipcount";
			}
			*/
		} else {
//			return "SELECT * FROM (" + sql + ") WHERE ROWNUM<=:page_maxcount";
			/**modified by zhaohuibin 2013-06-17 解决未排序导致翻页时记录顺序错乱的问题。
			return "SELECT * FROM (SELECT ROWNUM ROW_ID,  TAB.* FROM ( " + sql + " ) TAB where ROWNUM <= :page_maxcount order by rowid) " 
			+ "  WHERE ROW_ID <= :page_maxcount ";*/
			return sql + " WHERE ROW_ID <= :page_maxcount ";
		}
		
		/**
		 * Mysql SQL 写法
		 * 
		 */
		/*if (skip > 0) {
			return "SELECT * FROM (SELECT  TAB.* FROM ("
					+ sql
					+ ") TAB) TAB_  limit  :page_skipcount ,:page_maxcount";
			
		} else {
			//return "SELECT * FROM (" + sql + ") WHERE ROWNUM<=:page_maxcount";
			// 2011年8月18日15:02:43 上次只改了skip>0的，造成第一页数据重复
			return "SELECT * FROM (SELECT   TAB.* FROM ( " + sql + " ) TAB) " 
			+ " TAB_ limit 0,:page_maxcount ";
		}*/
	}

	private static String getOneCondition(String columnName, Object value) {
		return columnName + "=:" + columnName;
	}

	public static String getJoinedTables(String table1, String table2,
			String fields1, String fields2) {
		String s = table1 + " a join " + table2 + " b on 1=1";
		if (fields1 != null && fields1.trim().length() > 0 && fields2 != null
				&& fields2.trim().length() > 0) {
			String[] fieldarr1 = fields1.split(",");
			String[] fieldarr2 = fields2.split(",");
			if (fieldarr1.length == fieldarr2.length) {
				for (int i = 0; i < fieldarr1.length; i++) {
					s += " AND a." + fieldarr1[i] + "=b." + fieldarr2[i];
				}
			}
		}
		return "(" + s + ")";
	}
	
	/**
	 * 根据字段、操作符、参数值创建条件Map 
	 * @param column 字段
	 * @param operation 操作符
	 * @param value 参数值
	 * @return 单个条件Map
	 * May 19, 2008
	 */
	public static Map createCondition(String column, String operation, Object value) {
		Map condition = new HashMap();
		condition.put("column", column);
		condition.put("operation", operation);
		condition.put("value", value);
		return condition;
	}
}
