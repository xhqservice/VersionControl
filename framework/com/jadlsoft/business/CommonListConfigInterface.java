package com.jadlsoft.business;

import java.util.List;

import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.model.xtgl.BaseUserSession;

public interface CommonListConfigInterface {
	public String transTableName(String tableName , List conditions , DaoUtils daoUtils);
	public String transTableName(String tableName , List conditions , DaoUtils daoUtils,BaseUserSession userSessionBean);
}
