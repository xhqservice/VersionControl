package com.jadlwork.struts.action.app;

import org.apache.log4j.Logger;

import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlwork.business.app.IAppManager;
import com.jadlwork.model.ResultBean;
import com.jadlwork.model.app.AppAutotaskBean;
/**
 * 
 * 定时升级任务管理
 * @author wujiaxu
 * @Time 2017-12-21 下午3:54:28
 *
 */
public class AutoTaskAction extends BaseAction{
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(AutoTaskAction.class);

	private AppAutotaskBean autotaskBean;
	private IAppManager appManager;
	


	/**
	 * 进入应用编辑界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午4:09:17
	 */
	public String edit()  {

 		if(autotaskBean != null && autotaskBean.getId() != null && !"".equals(autotaskBean.getId())){
 			autotaskBean = (AppAutotaskBean) appManager.getAutoTaskBeanById(autotaskBean.getId());
		}

		return OK;
	}


	/**
	 * 新增任务
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午4:11:43
	 */
	public String save()  {	
		int i = appManager.saveAppAutotask(autotaskBean);
		return "list";
		
	}

	/**
	 * 任务修改
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String update()  {
		String fields = "appid,appname,version,todotime,zhxgsj";
		int re = appManager.updateAutoTaskByFields(autotaskBean, fields);
	
		return "list";
	}
	
	/**
	 * 
	 * 注销任务
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:09:51
	 */
	public void remove()  {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "注销失败！");
	
		int i = appManager.deleteAutotask(autotaskBean);
		if(i > 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "注销成功！");
		}
		
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			logger.error("注销定时任务异常！", e);
		}
	}


	public AppAutotaskBean getAutotaskBean() {
		return autotaskBean;
	}


	public void setAutotaskBean(AppAutotaskBean autotaskBean) {
		this.autotaskBean = autotaskBean;
	}


	public IAppManager getAppManager() {
		return appManager;
	}


	public void setAppManager(IAppManager appManager) {
		this.appManager = appManager;
	}

	
	
}
