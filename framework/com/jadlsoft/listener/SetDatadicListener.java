/**
 * <p>Title: SetDatadicListener</p>
 * <p>Description: 加载代码表缓存</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 京安丹灵</p>
 * @author zongshuai
 * @version 1.0
 * 2011-11-23
*/
 
package com.jadlsoft.listener;



import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.jadlsoft.dbutils.DicMapUtils;

public class SetDatadicListener implements ServletContextListener  {
	Logger log = Logger.getLogger(SetDatadicListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub 
		System.out.println("系统开始加载代码表缓存...");
		if (sce.getServletContext().getAttribute("dic") == null) {
			sce.getServletContext().setAttribute("dic", DicMapUtils.getDicMapCache());
		}
		System.out.println("系统加载代码表缓存完成");
		
		/*
		 * 在系统启动时移动历史勤务数据信息 
		 */
		//QwglAction qwglAction = (QwglAction)SpringBeanFactory.getBean("qwglAction");
		//qwglAction.moveQwsj();
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		if (sce.getServletContext().getAttribute("dic") != null) {
			sce.getServletContext().removeAttribute("dic");
		}
		System.out.println("系统清除代码表缓存完成");
	}
}