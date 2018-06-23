package com.jadlsoft.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jadlsoft.model.xtgl.BaseUserSession;

public class CommonListManager extends BaseManager {

	public int getDataCount(Map condition) {
		return daoUtils.getCount(condition);
	}
	public int getCount(List conditions, String tableName) {
		return daoUtils.getCount(conditions, tableName);
	}
	public int getCount(List conditions, String tableName,BaseUserSession userSessionBean, String className ) {
		tableName = transTableName(tableName , className , conditions,userSessionBean);
		return daoUtils.getCount(conditions, tableName);
	}
	public int getCount(List conditions, String tableName, String className ) {
		tableName = transTableName(tableName , className , conditions);
		return daoUtils.getCount(conditions, tableName);
	}
	public Map getDataCountAndList(Map condition, int pageSize, int pageNo) {
		int skip = (pageNo-1) * pageSize;
		return daoUtils.getCountAndList(condition, skip, pageSize);
	}

	public List getDataList(Map condition, int skip, int count) {
		return daoUtils.find(condition, skip, count);
	}
	
	public Map getDataCountAndList(Map condition, String tableName, int pageSize, int pageNo) {
		int skip = (pageNo-1) * pageSize;
		return daoUtils.getCountAndList(condition, tableName, skip, pageSize);
	}

	public Map getDataCountAndList(List conditions, String tableName, int pageSize, int pageNo) {
		int skip = (pageNo-1) * pageSize;
		return daoUtils.getCountAndList(conditions, tableName, skip, pageSize);
	}
	
	public Map getDataCountAndList(Map condition, List conditions, String tableName, String className, int pageSize, int pageNo) {
		
		tableName = transTableName(tableName , className , conditions);
		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(daoUtils.createCondition(conds[0], conds[1], conds[2]));
			}
			return daoUtils.getCountAndList(realConditions, tableName, skip, pageSize);
		} else {
			return daoUtils.getCountAndList(condition, tableName, skip, pageSize);
		}
	}
	public Map getDataCountAndList(Map condition, List conditions,BaseUserSession userSessionBean, String tableName, String className, int pageSize, int pageNo) {
		
		tableName = transTableName(tableName , className , conditions,userSessionBean);
		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(daoUtils.createCondition(conds[0], conds[1], conds[2]));
			}
			return daoUtils.getCountAndList(realConditions, tableName, skip, pageSize);
		} else {
			return daoUtils.getCountAndList(condition, tableName, skip, pageSize);
		}
	}
	/**
	 * 张方俊 2008-09-23 仅查询一次记录总数。此方法查询记录的列表
	 * @param condition
	 * @param conditions
	 * @param tableName
	 * @param className
	 * @param pageSize
	 * @param pageNo
	 * @param count
	 * @return
	 */
	public Map getDataList(Map condition, List conditions, String tableName, String className, int pageSize, int pageNo, int count) {
		tableName = transTableName(tableName , className , conditions);		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(daoUtils.createCondition(conds[0], conds[1], conds[2]));
			}
			
			return daoUtils.getList(realConditions, tableName, skip, pageSize, count);
		} 
		throw new RuntimeException("查询错误！");
	}
	/**
	 * 张方俊 2008-09-23 查询记录总数
	 * @param condition
	 * @param conditions
	 * @param tableName
	 * @param className
	 * @param pageSize
	 * @param pageNo
	 * @param count
	 * @return
	 */
	public Map getDataCount(Map condition, List conditions, String tableName, String className, int pageSize, int pageNo, int count) {
		tableName = transTableName(tableName , className , conditions);		
		int skip = (pageNo-1) * pageSize;
		if(condition==null || condition.size()==0) {
			List realConditions = new ArrayList();
			for(int i=0;i<conditions.size();i++) {
				String[] conds = ((String)conditions.get(i)).split("~");
				realConditions.add(daoUtils.createCondition(conds[0], conds[1], conds[2]));
			}			
			return daoUtils.getDataCount(realConditions, tableName, skip, pageSize, count);
		} 
		throw new RuntimeException("查询错误！");
	}
	
	private String transTableName(String tableName , String className , List conditions){		
		if(className == null || className.equals("")){
			return tableName;
		}
		if(className.equalsIgnoreCase("default")){
			className = "com.jadlsoft.business.CommonListConfigDefault";
		}
		Class clazz = null;
		CommonListConfigInterface commonListConfigInterface = null;
		try{
			clazz = Class.forName(className);
			commonListConfigInterface = (CommonListConfigInterface) clazz.newInstance();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			return tableName;
		} catch (InstantiationException e){
			e.printStackTrace();
			return tableName;
		} catch (IllegalAccessException e){			
			e.printStackTrace();
			return tableName;
		}
		return commonListConfigInterface.transTableName(tableName , conditions , this.daoUtils);
	}
	
	private String transTableName(String tableName , String className , List conditions,BaseUserSession userSessionBean){		
		if(className == null || className.equals("")){
			return tableName;
		}
		if(className.equalsIgnoreCase("default")){
			className = "com.jadlsoft.business.CommonListConfigDefault";
		}
		Class clazz = null;
		CommonListConfigInterface commonListConfigInterface = null;
		try{
			clazz = Class.forName(className);
			commonListConfigInterface = (CommonListConfigInterface) clazz.newInstance();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			return tableName;
		} catch (InstantiationException e){
			e.printStackTrace();
			return tableName;
		} catch (IllegalAccessException e){			
			e.printStackTrace();
			return tableName;
		}
		return commonListConfigInterface.transTableName(tableName , conditions , this.daoUtils,userSessionBean);
	}
	
}
