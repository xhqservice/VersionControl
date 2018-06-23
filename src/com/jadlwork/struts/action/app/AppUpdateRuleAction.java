package com.jadlwork.struts.action.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlwork.business.app.IAppManager;
import com.jadlwork.model.ResultBean;
import com.jadlwork.model.app.AppUpdateruleBean;
import com.jadlwork.model.app.AppVersionBean;
import com.sun.swing.internal.plaf.basic.resources.basic;
/**
 * 
 * 用户更新规则
 * @author wujiaxu
 * @Time 2017-12-22 下午2:28:10
 *
 */
public class AppUpdateRuleAction extends BaseAction{
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(AppUpdateRuleAction.class);

	private AppUpdateruleBean updateruleBean;
	private IAppManager appManager;



	/**
	 * 进行规则编辑界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:07:06
	 */
	public String edit()  {

 		if(updateruleBean != null && updateruleBean.getId() != null && !"".equals(updateruleBean.getId())){
 			updateruleBean = (AppUpdateruleBean) appManager.getUpdateRuleById(updateruleBean.getId());
		}

		return OK;
	}

	/**
	 * 添加新规则
	 * @return
	 * @throws Exception
	 * @author wujiaxu
	 * @Time 2017-12-22 下午2:46:16
	 */
	public String save() throws Exception  {	
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		int r = appManager.saveUpdateRule(updateruleBean);
		if(r <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "保存下载规则出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;
	}


	/**
	 * 修改规则
	 * @return
	 * @author wujiaxu
	 * @throws Exception 
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String update() throws Exception  {
	
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		//定义要更新的字段
		String fields = "targetlx,target,version,kssj,jssj,zhxgsj";
		
		
		int re = appManager.updateRuleByFields(updateruleBean, fields);
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新下载规则出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;

	
	}

	
	/**
	 * 
	 * 注销规则
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:09:51
	 */
	public String remove()  {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "注销失败！");
	
		int i = appManager.deleteRule(updateruleBean);
		if(i > 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "注销成功！");
		}
		
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			logger.error("注销APP异常！", e);
		}
		return null;
	}



	public AppUpdateruleBean getUpdateruleBean() {
		return updateruleBean;
	}


	public void setUpdateruleBean(AppUpdateruleBean updateruleBean) {
		this.updateruleBean = updateruleBean;
	}


	public IAppManager getAppManager() {
		return appManager;
	}


	public void setAppManager(IAppManager appManager) {
		this.appManager = appManager;
	}

	
}
