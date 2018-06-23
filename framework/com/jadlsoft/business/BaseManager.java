package com.jadlsoft.business;

import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.utils.StringUtils;

public class BaseManager {

	protected DaoUtils daoUtils;
	
	protected LogUtils logUtils;

	public BaseManager() {
		super();
	}

	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}

	public void setLogUtils(LogUtils logUtils){
		this.logUtils = logUtils;
	}
	/**
	 * 查询代码所对应的文字,主要用于查询没有缓冲的字典表,如t_dm_xzhqh
	 * @param tableName:代码表
	 * @param textField:文字字段
	 * @param codeField:代码字段
	 * @param codeValue:代码值
	 * @return 代码-文字
	 */
	public String queryNameByCode(String tableName, String textField,
			String codeField, String codeValue) {
		if (StringUtils.isEmpty(codeValue)) {
			return "";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(textField).append(" FROM ")
				.append(tableName).append(" WHERE ").append(codeField)
				.append(" = '").append(codeValue.trim()).append("'");
		Object name = daoUtils.queryForObject(sql.toString(), String.class);
		return name == null ? "" : name.toString();
	}
	
	/**
	 * 根据行政区划更新sql，判断GROUP BY条件
	 * add by 江坤 2013-10-6 11:21:34
	 * @param sql
	 * @param xzqh
	 * @return
	 */
    public static String getSqlByXzqh(String sql,String xzqh,String columnName){
		if(xzqh == null || "".equals(xzqh)){
			sql = sql.replaceAll(columnName + "1", "substr(" + columnName + ",1,2)");
			sql = sql.replaceAll(columnName + "2", "substr(" + columnName + ",1,2)||'0000'");
			return sql;
		}
		if(isProvince(xzqh)){
			if(isZxs(xzqh)){
				sql = sql.replaceAll(columnName + "1", "substr(" + columnName + ",1,6)");
				sql = sql.replaceAll(columnName + "2", "substr(" + columnName + ",1,6)");
			}else{
				sql = sql.replaceAll(columnName + "1", "substr(" + columnName + ",1,4)");
				sql = sql.replaceAll(columnName + "2", "substr(" + columnName + ",1,4)||'00'");
			}
			return sql;
		}
		sql = sql.replaceAll(columnName + "1", "substr(" + columnName + ",1,6)");
		sql = sql.replaceAll(columnName + "2", "substr(" + columnName + ",1,6)");
		return sql;
	}

	/**
	 * @功能：判断当前行政区划是否为省级行政区划
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2012-6-17 下午03:01:49
	 */
    public static boolean isProvince(String xzqh) {
		return "0000".equals(xzqh.substring(2,6));
	}
	
	/**
	 * @功能：判断是否为直辖市
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2013-1-8 下午02:10:24
	 */
    public static boolean isZxs(String xzqh){
		return ("11").equals(xzqh.substring(0,2)) || ("12".equals(xzqh.substring(0,2))) || ("31".equals(xzqh.substring(0,2))) || ("50".equals(xzqh.substring(0,2)));
	}
	

	/**
	 * 获取最大的ID
	 * @参数：@return
	 * @返回值：String
	 */
	public synchronized int getMaxId(String tablename,String tablepkfields) {
		try {
			Integer maxid = (Integer) daoUtils.queryForObject("select max("+tablepkfields+") id from "+tablename,
					Integer.class);
			if (maxid == null || maxid == 0 ) 
			{	
				maxid = 1;
			} 
			else 
			{
				maxid = Integer.valueOf(maxid)+1;
			}
			return maxid;
		} catch (Exception e) {
			throw new RuntimeException("获取当前最大Iid出错！");
		}
	}
	
}