package com.jadlwork.business.qrgl.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlwork.business.app.impl.AppManager;
import com.jadlwork.business.qrgl.IDmglManager;

/**
 * 
 * 字典操作业务类
 * @author wujiaxu
 * @Time 2018-1-30 上午10:05:29
 *
 */
public class DmglManager extends BaseManager implements IDmglManager {
	private static Logger logger = Logger.getLogger(AppManager.class);
	
	/**
	 * 查询字典表数据列表
	 * @param table
	 * @return
	 */
	public List getDmContent(String table) {
		String sql = "select * from " + table;
		return this.daoUtils.find(sql, new HashMap());
	}

	/**
	 * 查询字典表标题、主键、列名等信息列表
	 * @param tableName
	 * @return
	 */
	public List getDmComments(String tablename) {
		Map condition = new HashMap();
		condition.put("tablename", tablename);
		return daoUtils.find("#dmgl.getDmComments", condition);
	}
	
	/**
	 * 字典插入和修改
	 * @param type
	 * @param table
	 * @param pkcolumn
	 * @param columnname
	 * @param columnvalue
	 */
	public void updateDic(String type, String table, String columnname,
			String columnvalue, String pkcolumn,String columntype) {
		String[] columnnameArr = columnname.split("@@");
		String[] columnvalueArr = columnvalue.split("@@");
		String[] columntypeArr = columntype.split("@@");
		//插入新纪录
		if ("insert".equals(type)) {
			StringBuffer columnnameBuff = new StringBuffer();
			StringBuffer columnvalueBuff = new StringBuffer();
			for (int i = 0; i < columnnameArr.length; i++) {
				if ("null".equals(columnvalueArr[i])) {
					continue;
				}
				if (columnnameBuff.length() == 0) {
					columnnameBuff.append(columnnameArr[i]);
					if("varchar2".equalsIgnoreCase(columntypeArr[i]) || "char".equalsIgnoreCase(columntypeArr[i])){
						columnvalueBuff.append("'");
						columnvalueBuff.append(columnvalueArr[i]);
						columnvalueBuff.append("'");
					}else{
						columnvalueBuff.append(columnvalueArr[i]);
					}
				} else {
					columnnameBuff.append(",");
					columnnameBuff.append(columnnameArr[i]);

					columnvalueBuff.append(",");
					if("varchar2".equalsIgnoreCase(columntypeArr[i]) || "char".equalsIgnoreCase(columntypeArr[i])){
						columnvalueBuff.append("'");
						columnvalueBuff.append(columnvalueArr[i]);
						columnvalueBuff.append("'");
					}else{
						columnvalueBuff.append(columnvalueArr[i]);
					}
				}
			}
			String sql = "insert into " + table + " (" + columnnameBuff.toString()
					+ ") values (" + columnvalueBuff.toString() + ")";
			this.daoUtils.execSql(sql);
		}
		//更新纪录
		if ("update".equals(type)) {
			StringBuffer columnValueBuff = new StringBuffer();
			String pkValue = "";
			for (int i = 0; i < columnnameArr.length; i++) {
				if ("null".equals(columnvalueArr[i])) {
					continue;
				}
				//只支持一个主键情况
				if (pkcolumn.equals(columnnameArr[i])) {
					if("varchar2".equalsIgnoreCase(columntypeArr[i]) || "char".equalsIgnoreCase(columntypeArr[i])){
						pkValue = pkcolumn + "='" + columnvalueArr[i] + "'";
					}else{
						pkValue = pkcolumn + "=" + columnvalueArr[i];
					}
				}
				if (columnValueBuff.length() == 0) {
					columnValueBuff.append(columnnameArr[i]);
					columnValueBuff.append("=");
					if("varchar2".equalsIgnoreCase(columntypeArr[i]) || "char".equalsIgnoreCase(columntypeArr[i])){
						columnValueBuff.append("'");
						columnValueBuff.append(columnvalueArr[i]);
						columnValueBuff.append("'");
					}else{
						columnValueBuff.append(columnvalueArr[i]);
					}
					columnValueBuff.append(" ");
				} else {
					columnValueBuff.append(",");
					columnValueBuff.append(columnnameArr[i]);
					columnValueBuff.append("=");
					if("varchar2".equalsIgnoreCase(columntypeArr[i]) || "char".equalsIgnoreCase(columntypeArr[i])){
						columnValueBuff.append("'");
						columnValueBuff.append(columnvalueArr[i]);
						columnValueBuff.append("'");
					}else{
						columnValueBuff.append(columnvalueArr[i]);
					}
					columnValueBuff.append(" ");
				}
			}
			String sql = "update " + table + " set " + columnValueBuff.toString() + " where "
					+ pkValue;

			this.daoUtils.execSql(sql);
		}
	}

	/**
	 * 字典删除操作
	 * @param table
	 * @param pkcolumn
	 * @param columnname
	 * @param columnvalue
	 */
	public void deleteDic(String table, String pkcolumn, String columnname,
			String columnvalue,String columntype) {
		String[] columnnameArr = columnname.split("@@");
		String[] columnvalueArr = columnvalue.split("@@");
		String[] columntypeArr = columntype.split("@@");
		String pkValue = "";
		for (int i = 0; i < columnnameArr.length; i++) {
			if (pkcolumn.equals(columnnameArr[i])) {
				if("varchar2".equals(columntypeArr[i]) || "char".equals(columntypeArr[i])){
					pkValue = pkcolumn + "='" + columnvalueArr[i] + "'";
				}else{
					pkValue = pkcolumn + "=" + columnvalueArr[i] + "";
				}
			}
		}
		String sql = "delete from " + table + " where " + pkValue;
		this.daoUtils.execSql(sql);
	}

	/**
	 * 根据条件，模糊查询字典列表 
	 * @param table
	 * @param searchkey
	 * @param columnname
	 * @return
	 */
	public List queryDic(String table, String searchkey, String columnname) {
		searchkey = searchkey.trim();
		String[] columnnameArr = columnname.split("@@");
		String condition = "";
		for (int i = 0; i < columnnameArr.length; i++) {
			String column = columnnameArr[i];
			if ("".equals(column)) {
				continue;
			}
			if ("".equals(condition)) {
				condition += (column + " like '%" + searchkey + "%' ");
			} else {
				condition += (" or " + column + " like '%" + searchkey + "%' ");
			}

		}
		String sql = "select * from " + table + " where " + condition;
		return this.daoUtils.find(sql, new HashMap());
	}

	public List getDic(String table, String pkcolumn, String columnname, String columnvalue,String columntype) {
		String[] columnnameArr = columnname.split("@@");
		String[] columnvalueArr = columnvalue.split("@@");
		String[] columntypeArr = columntype.split("@@");
		String pkvalue = "";
		String pktype = "";
		for(int i=0; i<columnnameArr.length; i++){
			if(pkcolumn.equals(columnnameArr[i])){
				pkvalue = columnvalueArr[i];
				pktype = columntypeArr[i];
			}
		}
		String sql = "";
		if("varchar2".equalsIgnoreCase(pktype) || "char".equalsIgnoreCase(pktype)){
			sql = "select * from " + table + " where " + pkcolumn + "='" + pkvalue + "'";
		}else{
			sql = "select * from " + table + " where " + pkcolumn + "=" + pkvalue;
		}
		return this.daoUtils.find(sql, new HashMap());
	}
	
	
	
}
