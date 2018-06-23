package com.jadlwork.struts.action.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlwork.business.app.IAppManager;
import com.jadlwork.model.ResultBean;
import com.jadlwork.model.app.AppJbxxBean;
import com.jadlwork.model.app.AppVersionBean;
/**
 * 
 * app管理
 * @author wujiaxu
 * @Time 2017-12-20 上午10:00:48
 *
 */
public class AppAction extends BaseAction{
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(AppAction.class);

	private AppJbxxBean appJbxxBean;
	private IAppManager appManager;
	// myFile属性用来封装上传的文件  
    private File myFile;  
    // myFileContentType属性用来封装上传文件的类型  
    private String myFileContentType;  
    // myFileFileName属性用来封装上传文件的文件名  
    private String myFileFileName;  
	


	/**
	 * 进入app编辑界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:07:06
	 */
	public String edit()  {

 		if(appJbxxBean != null && appJbxxBean.getId() != null && !"".equals(appJbxxBean.getId())){
 			appJbxxBean = (AppJbxxBean) appManager.getAppBeanById(appJbxxBean.getId());
		}

		return OK;
	}

	/**
	 * 异步上传图标
	 */
	public void uploadIcon() {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "success");
		String appid = appJbxxBean.getAppid();
		if (myFile != null) {
			String backupDir = SysConfigUtils.getProperty("war.dir.backup","appbackup");
			//文件存储在VersionControlCenter/appbackup/  命名规则：appidIcon.后缀
			
			String toDirStr = File.separator + backupDir + File.separator + "tmp";
			String toDir = request.getSession().getServletContext().getRealPath(toDirStr);
			
			String filename = StringUtils.getRandomUUID();
			File toFile = new File(toDir, filename);
			
			try {
				FileUtils.copyFile(myFile, toFile);
			} catch (IOException e) {
				try {
					ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "上传文件失败！");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			resultBean.setArg1(filename);
			resultBean.setArg2(toDirStr+File.separator+filename);
		}
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增APP
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:07:52
	 */
	public String save()  {	
		saveIcon();
		int i = appManager.saveApp(appJbxxBean);
		return "list";
		
	}

	/**
	 * app修改
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String update()  {
		saveIcon();
		String fields = "appid,appname,appdesc,ssxm,sort";
		if (myFile != null) {
			fields += ",iconsrc";
		}
		int re = appManager.updateAppByFields(appJbxxBean, fields);
	
		return "list";
	}
	
	/**
	 * 将icon文件保存到指定目录下，并且规范命名
	 */
	private void saveIcon() {
		if (myFile != null) {
			String tmpFilename = request.getParameter("tmpIconFilename");
			if (StringUtils.isEmpty(tmpFilename)) {
				throw new RuntimeException("临时文件名称获取失败！");
			}
			
			String backupDir = SysConfigUtils.getProperty("war.dir.backup","appbackup");
			//文件存储在VersionControlCenter/appbackup/  命名规则：appidIcon.后缀
			
			String fromDirStr = File.separator + backupDir + File.separator + "tmp";
			String fromDir = request.getSession().getServletContext().getRealPath(fromDirStr);
			
			String toDirStr = File.separator + backupDir + File.separator + appJbxxBean.getAppid();
			String toDir = request.getSession().getServletContext().getRealPath(toDirStr);
			
			File fromFile = new File(fromDir, tmpFilename);
			if (!fromFile.isFile()) {
				throw new RuntimeException("文件获取失败！");
			}
			
			String suffix = FilenameUtils.getExtension(this.getMyFileFileName());
			String toFileName = SystemConstants.APPICONNAME
						.replace("{@appid}", appJbxxBean.getAppid()).replace("{@suffix}", suffix);
			File toFile = new File(toDir, toFileName);
			
			try {
				FileUtils.copyFile(fromFile, toFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/**
			 * 上传成功后处理信息
			 */
			//设置图标地址字段
			String iconsrc = toDirStr+File.separator+toFileName;
			appJbxxBean.setIconsrc(iconsrc);
			
			//删除临时文件
			fromFile.delete();
		}
	}
	
	/**
	 * 
	 * 注销APP
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:09:51
	 */
	public void remove()  {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "注销失败！");
	
		int i = appManager.deleteApp(appJbxxBean);
		if(i > 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "注销成功！");
		}
		
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			logger.error("注销APP异常！", e);
		}
	}

	
	/**
	 * 校验应用ID是否可用
	 */
	public String checkAppid() throws Exception {
		ResultBean resultBean = null;
		String appid = request.getParameter("appid");
		/**
		 * 1、非空校验
		 */
		if (StringUtils.isEmpty(appid)) {
			
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "应用ID不能为空！");
			return null;
		}
		
		/**
		 * 2、接口id不能重复
		 */
		Map appMap = appManager.getBeanByAppid(appid);
		if (appMap != null && appMap.size()>0) {
			//已经存在同样的接口id
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "应用ID已存在！");
			return null;
		}
		
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "验证成功！");
		return null;
	}
	
	/**
	 * app版本维护界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String version()  {
		String id = appJbxxBean.getId();
		appJbxxBean = appManager.getAppBeanById(id);
		return OK;
	}
	
	/**
	 * app更新规则维护界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String updaterule()  {
		String id = appJbxxBean.getId();
		appJbxxBean = appManager.getAppBeanById(id);
		return OK;
	}

	public AppJbxxBean getAppJbxxBean() {
		return appJbxxBean;
	}


	public void setAppJbxxBean(AppJbxxBean appJbxxBean) {
		this.appJbxxBean = appJbxxBean;
	}


	public IAppManager getAppManager() {
		return appManager;
	}


	public void setAppManager(IAppManager appManager) {
		this.appManager = appManager;
	}


	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getMyFileContentType() {
		return myFileContentType;
	}


	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}


	public String getMyFileFileName() {
		return myFileFileName;
	}


	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	

	
}
