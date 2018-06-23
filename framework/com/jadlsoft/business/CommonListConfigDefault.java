package com.jadlsoft.business;

import java.util.List;

import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.model.xtgl.BaseUserSession;
import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.SysConfigUtils;
/**
 * 针对listConfig.xml中datasource进行相应的默认处理。
 * 默认的处理包括：
 * 1、替换当前的日期
 * @author 张方俊 2008-7-11 : 上午10:10:33
 *
 */
public class CommonListConfigDefault implements CommonListConfigInterface {

	public String transTableName(String tableName, List conditions, DaoUtils daoUtils)
	{
		int days = Integer.parseInt(SysConfigUtils.getProperty("jqcxts", "30"));
		tableName = tableName.replaceAll(":today", "'" + DateUtils.getCurrentDate() + "'");
		tableName = tableName.replaceAll(":nowtime", "'" + DateUtils.getCurrentDateStr() + "'");
		tableName = tableName.replaceAll(":lastMonthToday", "'" + DateUtils.getLastFewDaysAgo(days) + "'");
		tableName = tableName.replaceAll(":zhrcjcrq", "'" + DateUtils.getLastFewDaysAgo(Integer.parseInt(SysConfigUtils.getProperty("bjxx_recentday", "30"))) + "'");
		
		return tableName;
	}

	public String transTableName(String tableName, List conditions,
			DaoUtils daoUtils, BaseUserSession userSessionBean) {
		int days = Integer.parseInt(SysConfigUtils.getProperty("jqcxts", "30"));
		tableName = tableName.replaceAll(":today", "'" + DateUtils.getCurrentDate() + "'");
		tableName = tableName.replaceAll(":nowtime", "'" + DateUtils.getCurrentDateStr() + "'");
		tableName = tableName.replaceAll(":lastMonthToday", "'" + DateUtils.getLastFewDaysAgo(days) + "'");
		tableName = tableName.replaceAll(":zhrcjcrq", "'" + DateUtils.getLastFewDaysAgo(Integer.parseInt(SysConfigUtils.getProperty("bjxx_recentday", "30"))) + "'");
		
		return tableName;
	}

	

	 

}
