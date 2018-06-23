package com.jadlsoft.business.xtgl;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;

public class LoginManager extends BaseManager { 
	private static Logger logger = Logger.getLogger(LoginManager.class);
	public String get(String userid) {
		try {
			return (String)daoUtils.queryForObject("select password from t_xt_user where userid = '" + userid + "'" , String.class);
		} catch (Exception e) {
			logger.info("根据用户ID查询登录用户密码出错！执行的sql为:select password from t_xt_user where userid = '" + userid + "'", e);
			return "";
		}
		
	}
}
