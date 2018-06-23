package com.jadlwork.business.qrgl;

import java.util.List;

/**
 * 
 * 字典操作类
 * @author wujiaxu
 * @Time 2018-1-30 上午10:02:07
 *
 */
public interface IDmglManager {

	/**
	 * 查询字典表数据列表
	 * @param table
	 * @return
	 */
	List getDmContent(String table);
	/**
	 * 查询字典表标题、主键、列名等信息列表
	 * @param tableName
	 * @return
	 */
	List getDmComments(String tablename);
	
	/**
	 * 字典插入和修改
	 * @param type
	 * @param table
	 * @param pkcolumn
	 * @param columnname
	 * @param columnvalue
	 */
	void updateDic(String type, String table, String columnname,
			String columnvalue, String pkcolumn,String columntype);

	/**
	 * 字典删除操作
	 * @param table
	 * @param pkcolumn
	 * @param columnname
	 * @param columnvalue
	 */
	public void deleteDic(String table, String pkcolumn, String columnname,
			String columnvalue,String columntype);

	/**
	 * 根据条件，模糊查询字典列表 
	 * @param table
	 * @param searchkey
	 * @param columnname
	 * @return
	 */
	public List queryDic(String table, String searchkey, String columnname);

	
	public List getDic(String table, String pkcolumn, String columnname, String columnvalue,String columntype);
	
}
