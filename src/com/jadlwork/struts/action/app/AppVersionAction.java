package com.jadlwork.struts.action.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
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
import com.jadlwork.model.app.AppVersionBean;
import com.jadlwork.utils.APKUtils;
import com.jadlwork.utils.FileUtils;
/**
 * 
 * app版本管理
 * @author wujiaxu
 * @Time 2017-12-20 上午10:00:48
 *
 */
public class AppVersionAction extends BaseAction{
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(AppVersionAction.class);

	private AppVersionBean appVersionBean;
	private IAppManager appManager;
	// myFile属性用来封装上传的文件  
    private File myFile;  
    // myFileContentType属性用来封装上传文件的类型  
    private String myFileContentType;  
    // myFileFileName属性用来封装上传文件的文件名  
    private String myFileFileName;  


	/**
	 * 进入app版本编辑界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:07:06
	 */
	public String edit()  {

 		if(appVersionBean != null && appVersionBean.getId() != null && !"".equals(appVersionBean.getId())){
 			appVersionBean = (AppVersionBean) appManager.getAppVersionBeanById(appVersionBean.getId());
		}

		return OK;
	}


	/**
	 * 上传新版本
	 * @return
	 * @author wujiaxu
	 * @throws Exception 
	 * @Time 2017-12-20 上午10:07:52
	 */
	public String save() throws Exception  {	
		ResultBean resultBean = this.checkVersion();
		if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			return null;
		}
		
		int r = appManager.saveAppVersion(appVersionBean);
		if(r <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "上传APP版本出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;
	}


	/**
	 * 修改版本
	 * @return
	 * @author wujiaxu
	 * @throws Exception 
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String update() throws Exception  {
		ResultBean resultBean = this.checkVersion();
		if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			return null;
		}
		
		
		//定义要更新的字段
		String fields = "versiondesc,zhxgsj";
		if(myFile != null){
			fields += ",apk,apksrc,apksize";
		}
		
		
		int re = appManager.updateAppVersionByFields(appVersionBean, fields);
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新版本出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;

	
	}
	
	/**
	 * 检查版本
	 * @throws Exception 
	 * @参数：@return
	 * @返回值：String
	 */
	public ResultBean checkVersion() {
		
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		if(myFile != null){
			String tmpApkFilename = request.getParameter("tmpApkFilename");
			if (StringUtils.isEmpty(tmpApkFilename)) {
				return new ResultBean(SystemConstants.STATUSCODE_FALSE, "文件参数错误！");
			}
			
			/**
			 * 1、校验版本是否已存在
			 */
			String appid = appVersionBean.getAppid();
			String version = appVersionBean.getVersion();
			String id = appVersionBean.getId();
			boolean b = appManager.checkAppVersion(appid, id, version);
			if(b){
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "版本已存在！");
				return resultBean;
			}
			
			/**
			 * 2、上传APK到临时文件夹	从临时目录转移到正式目录
			 */
			String backupDir = SysConfigUtils.getProperty("war.dir.backup","appbackup");
			//文件存储在VersionControlCenter/appbackup/应用ID/版本
			String fromDirStr = File.separator + backupDir + File.separator + appid + File.separator + "tmp";
			String toDirStr = File.separator + backupDir + File.separator + appid + File.separator + version;
			
			String fromDir = request.getSession().getServletContext().getRealPath(fromDirStr);
			String toDir = request.getSession().getServletContext().getRealPath(toDirStr);
			
			File fromFile = new File(fromDir, tmpApkFilename);
			File toFile = new File(toDir, this.myFileFileName);
			
			if (!fromFile.isFile()) {
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "未找到上传的文件！");
				return resultBean;
			}
			try {
				org.apache.commons.io.FileUtils.copyFile(fromFile, toFile);
			} catch (IOException e) {
				resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "文件复制出错！");
				return resultBean;
			}
			fromFile.delete();

			/**
			 * 3、设置APK、存储路径
			 */
			if(resultBean != null && resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				appVersionBean.setApk(this.myFileFileName);
				appVersionBean.setApksrc(toDirStr+File.separator+this.myFileFileName);
			}
		}
		return resultBean;
		
	}
	
	/**
	 * 检测版本
	 * @throws Exception 
	 * @参数：@return
	 * @返回值：String
	 */
	public void detectVersion() throws Exception {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		
		if(myFile != null){
			
			String appid = appVersionBean.getAppid();
			String version = appVersionBean.getVersion();
			String versionid = appVersionBean.getId();
			boolean isEdit = StringUtils.isEmpty(versionid) ? false : true;
			
			/**
			 * 1、将文件上传到临时目录
			 */
			String backupDir = SysConfigUtils.getProperty("war.dir.backup","appbackup");
			String saveDir = File.separator + backupDir + File.separator + appid + File.separator + "tmp";
			String savePath = request.getSession().getServletContext().getRealPath(saveDir);
			
			resultBean = this.uploadApk(savePath);
			if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "上传apk失败！");
				return;
			}
			
			/**
			 * 2、解析版本号
			 */
			File apkFile = new File(savePath, resultBean.getArg1().toString());
			if (apkFile == null || !apkFile.isFile()) {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "上传apk失败！");
				return;
			}
			
			//获取apk信息
			Map<String, Object> apkInfo = APKUtils.getAPKInfo(apkFile.getAbsolutePath());
			String versionName = apkInfo.get("versionName") == null ? "" : apkInfo.get("versionName").toString();
			String versionCode = apkInfo.get("versionCode") == null ? "" : apkInfo.get("versionCode").toString();
			
			if (StringUtils.isEmpty(versionName)) {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "apk版本获取失败！");
				//删除临时目录
				apkFile.delete();
				return;
			}
			
			/**
			 * 3、校验版本号
			 */
			boolean b = appManager.checkAppVersion(appid,versionid,versionName);
			if (b) {
				//已经存在同样的接口id
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "版本已存在！");
				//删除临时目录
				apkFile.delete();
				return;
			}
			
			
			/**
			 * 4、根据不同场景处理
			 */
			if (isEdit) {
				//编辑界面，需要判断解析出来的版本号跟当前版本号是否一致
				if (!versionName.equalsIgnoreCase(version)) {
					ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "版本不一致！");
					//删除临时目录
					apkFile.delete();
					return;
				}else {
					//检测版本成功，需要将临时文件名以及文件大小返回
					resultBean.setArg3(resultBean.getArg2());
					resultBean.setArg2(resultBean.getArg1());
				}
				ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
				return;
			}
			//新增界面，需要将版本号作为参数返回，临时文件随机名称也返回
			resultBean.setArg3(resultBean.getArg2());
			resultBean.setArg2(resultBean.getArg1());
			resultBean.setArg1(versionName);
			resultBean.setArg4(versionCode);
		}
		ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
	}
	
	/**
	 * APK上传到主机
	 * @参数：
	 * @返回值：void
	 */
	private ResultBean uploadApk(String uploadPath) {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"success");
		if(myFile == null){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "上传的文件不存在！");
			return resultBean;
		}
		
		
		try {
			//1.创建文件夹
			File f = new File(uploadPath);
			if(!f.exists()){
				f.mkdirs();
			}
			
			//2.上传文件
			String filename = StringUtils.getRandomUUID();
			InputStream is = new FileInputStream(myFile);
			File toFile = new File(uploadPath, filename);
			OutputStream os = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int length = 0;

			// 读取myFile文件输出到toFile文件中
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
			
			//上传成功，保存文件名以及文件大小
			String fileSize = FileUtils.getFileSize(toFile.getAbsolutePath(), FileUtils.SIZEFORMAT_M, 2);
			resultBean.setArg2(fileSize);
			resultBean.setArg1(filename);
			return resultBean;
		} catch (Exception e) {
			logger.error("上传war包失败！", e);
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "WAR包上传出错！");
			return resultBean;
		}
		
	}
	
	
	/**
	 * 
	 * 注销版本
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:09:51
	 */
	public String remove()  {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "注销失败！");
	
		int i = appManager.deleteAppVersion(appVersionBean);
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

	/**
	 * 
	 * 设为默认版本
	 * @author wujiaxu
	 * @throws Exception 
	 * @Time 2017-12-20 上午10:09:51
	 */
	public String setdefault()   {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "设置失败！");
		try {
			if(appVersionBean == null 
					|| appVersionBean.getVersion() == null || "".equals(appVersionBean.getVersion())
					|| appVersionBean.getAppid() == null || "".equals(appVersionBean.getAppid())){
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "参数错误！");
				return null;
			}
			 
			boolean b = appManager.appVersionToDefault(appVersionBean.getAppid(),appVersionBean.getVersion());
			if(b){
				resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "设置成功！");
			}
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			logger.error("设置默认版本出错！", e);
		}
		return null;
	}
	
	/**
	 * 校验版本是否存在
	 */
	public String checkAppVersion() throws Exception {
		ResultBean resultBean = null;
		String appid = appVersionBean.getAppid();
		String versionid = appVersionBean.getId();
		String version = appVersionBean.getVersion();
		/**
		 * 1、非空校验
		 */
		if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(version)) {
			
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "参数错误！");
			return null;
		}
		
		/**
		 * 2、版本是否存在
		 */
		boolean b = appManager.checkAppVersion(appid,versionid,version);
		if (b) {
			//已经存在同样的接口id
			ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "版本已存在！");
			return null;
		}
		
		ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_OK, "检测通过！");
		return null;
	}
	
	/**
	 * 进入IOS编辑界面
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午10:07:06
	 */
	public String edit_ios()  {
		
 		if(appVersionBean != null && appVersionBean.getAppid() != null && !"".equals(appVersionBean.getAppid())){
 			Map map  = (Map) appManager.getIOSAppByAppid(appVersionBean.getAppid());
 			request.setAttribute("appVersionBean", map);
		}

		return OK;
	}


	
	/**
	 * 保存IOS版本
	 * @return
	 * @author wujiaxu
	 * @throws Exception 
	 * @Time 2017-12-20 上午10:07:52
	 */
	public String save_ios() throws Exception  {	
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"保存成功！");
		if(resultBean == null || !resultBean.getStatusCode().equals(SystemConstants.STATUSCODE_OK)){
			ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
			return null;
		}
		
		String fields = "id,appid,version,apksrc,isdefault,cjsj,type,zt";
		appVersionBean.setType(SystemConstants.APPTYPE_IOS);
		appVersionBean.setIsdefault(SystemConstants.APPVERSION_DEFAULT);
		int r = appManager.saveAppVersionByFileds(appVersionBean,fields);
		if(r <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "保存IOS版本出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;
	}


	/**
	 * 修改IOS版本
	 * @return
	 * @author wujiaxu
	 * @throws Exception 
	 * @Time 2017-12-20 上午10:08:36
	 */
	public String update_ios() throws Exception  {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK,"更新成功！");
		//定义要更新的字段
		String fields = "version,apksrc,zhxgsj";

		int re = appManager.updateAppVersionByFields(appVersionBean, fields);
		if(re <= 0){
			resultBean = new ResultBean(SystemConstants.STATUSCODE_FALSE, "更新IOS版本出错！");
		}
		ResponseUtils.render(response,JsonUtil.bean2json(resultBean));
		return null;
	}



	public AppVersionBean getAppVersionBean() {
		return appVersionBean;
	}


	public void setAppVersionBean(AppVersionBean appVersionBean) {
		this.appVersionBean = appVersionBean;
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
