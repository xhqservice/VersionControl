/**
 * com.jadlsoft.struts.action UserUtils.java Dec 26, 2007 9:31:28 AM
 */
package com.jadlsoft.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jadlsoft.model.xtgl.UserSessionBean;
import com.jadlsoft.utils.XzhqhUtils;

/**
 * @author sky 功能：
 * 
 */
public class UserUtils { 
	
	public static final String USER_SESSION = "userSessionBean"; 
	/**
	 * getUserSessionBean() 功能：获取session中用户信息
	 * 
	 * @param request
	 * @return UserSessionBean
	 */
	public static UserSessionBean getUserSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		} else {
			return (UserSessionBean) session.getAttribute(USER_SESSION);
		}
	}

	/**
	 * checkXzqh() 功能：判断用户是否为该行政区划下的用户
	 * 
	 * @param userSessionBean
	 *            登录用户基本信息
	 * @param xzqh
	 *            被操作记录所属行政区划
	 * @param type
	 *            验证类型 1：本行政区划只能操作本行政区划数据，本行政区划下的数据只能由本行政区划下的用户操作
	 *            2：上级可以操作下级数据，下级只能查看上级数据（省、市、县三级）
	 * @return boolean true:无修改权限 false:有修改权限
	 */
	public static boolean checkXzqh(UserSessionBean userSessionBean,
			String xzqh, int type) {
		boolean boolReturn = false;
		String userXzqh = userSessionBean.getXzqh();
		if (type == 1) {
			boolReturn = !userXzqh.equals(xzqh);
		} else if (type == 2) {
			boolReturn = !xzqh.startsWith(getShortXZQH(userXzqh));
		}
		return boolReturn;
	}

	/**
	 * getShortXZQH() 功能：
	 * 
	 * @param xzqh
	 * @return String
	 */
	private static String getShortXZQH(String xzqh) {
		return XzhqhUtils.getXZHQH(xzqh);
	}
	
	
	 
}
