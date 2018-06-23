package com.jadlsoft.business.xtgl;

import java.util.HashMap;
import java.util.Map;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.model.xtgl.UserSessionBean;
import com.jadlsoft.utils.DateUtils;

public class AopManager extends BaseManager {
	
	String sql = "insert into t_wh_xtrz values(Q_WH_XTRZSXH.nextval, :userid, :username, :czdate, :qgdw, :gncode, :sm)";
	/**
	 * 记录系统日志
	 * @param user
	 * @param gncode
	 * @param sm
	 */
	public void saveXtrz(UserSessionBean user, String gncode, String sm) {
		Map condition = new HashMap();
		condition.put("userid", user.getUserId());
		condition.put("username", user.getUserName());
		condition.put("czdate", DateUtils.createCurrentDate());
		condition.put("qgdw", user.getQydm());
		condition.put("gncode", gncode);
		condition.put("sm", sm);
		daoUtils.execSql(sql, condition);
	//	daoUtils.execSql("#xtgl.saveXtrz", condition);
	}
}
