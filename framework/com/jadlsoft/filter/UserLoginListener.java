package com.jadlsoft.filter;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.jadlsoft.model.xtgl.BaseUserSession;
import com.jadlsoft.struts.action.UserUtils;

public class UserLoginListener implements ServletContextListener,
		HttpSessionListener {
	
	private Map loginedUsers;  

	public void contextDestroyed(ServletContextEvent arg0) {
		arg0.getServletContext().removeAttribute("loginedusers");
		loginedUsers.clear();
		loginedUsers = null;
	}

	public void contextInitialized(ServletContextEvent arg0) {
		loginedUsers = new Hashtable();
		arg0.getServletContext().setAttribute("loginedusers", loginedUsers);
	}
	
	public void sessionCreated(HttpSessionEvent se) {
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		Object obj = arg0.getSession().getAttribute(UserUtils.USER_SESSION);
		if(obj!=null && obj instanceof BaseUserSession) {
			BaseUserSession userBean = (BaseUserSession)obj;
			if(loginedUsers != null && loginedUsers.containsKey(userBean.getUserId())) {
				loginedUsers.remove(userBean.getUserId());
			}
		}
	}
 
	
}
