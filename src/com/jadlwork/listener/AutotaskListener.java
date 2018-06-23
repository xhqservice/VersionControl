package com.jadlwork.listener;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jadlwork.business.app.IAppManager;
/**
 * 
 * 定时任务监听器
 * @author wujiaxu
 * @Time 2017-12-22 上午10:38:52
 *
 */
public class AutotaskListener implements ServletContextListener {

	private WebApplicationContext wac = null;
	private IAppManager appManager = null;
	
	private Timer timer;
	private int delay = 10;  		//定时器延时执行时间(秒)
	private int period = 60;		//间隔时间
	
	private Logger logger = Logger.getLogger(AutotaskListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		timer = new Timer();
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());	//设置wac对象
		appManager = (IAppManager) wac.getBean("appManager");
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				todoTask();
			}
		}, delay * 1000, period * 1000);
	}
	
	/**
	 * 
	 * 执行定时任务
	 * @author wujiaxu
	 * @Time 2017-12-22 上午10:40:39
	 */
	private void todoTask() {
		List list = appManager.getAutoTaskList();
		if(list != null && list.size() > 0){
			logger.info("【执行定时任务】开始执行，本次执行("+list.size()+")个！");
			
			for (int i = 0; i < list.size(); i++) {
				Map taskMap = (Map) list.get(i);
				String taskid = (String) taskMap.get("id");
				String appid = (String) taskMap.get("appid");
				String version = (String) taskMap.get("version");
				appManager.upgradeVersion(taskid,appid,version);
			}
			logger.info("【执行定时任务】执行结束！");
		}
	}

}
