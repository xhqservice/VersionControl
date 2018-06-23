/**
 * @Description 基础Action
 * @Company 京安丹灵
 * @author zongshuai
 * @date 2013-03-06
 * @version 1.0
 */

package com.jadlsoft.struts.action;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.jadlsoft.business.CommonLogManager;
import com.jadlsoft.model.xtgl.UserSessionBean;
import com.jadlsoft.utils.DateUtils;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware, ApplicationAware {
	public static final String VIEW = "view";
	public static final String SUCCEED = "SUCCEED";
	public static final String FAILED = "FAILED";
	public static final String OK = "OK";
	public static final String SUCCESS = "success";
	public static final String INSERTORUPDATE = "INSERTORUPDATE";
	private String uploaddir = "upload";
	private static Logger log = Logger.getLogger(BaseAction.class);
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map application;
	protected List todoList = new ArrayList();
	protected Map  todoMap = null; 
	
	
	private CommonLogManager commonLogManager;

	public CommonLogManager getCommonLogManager() {
		return commonLogManager;
	}

	public void setCommonLogManager(CommonLogManager commonLogManager) {
		this.commonLogManager = commonLogManager;
	}

	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

	/**
	 * 从session中取得当前登录用户信息
	 * @return
	 */
	public UserSessionBean getUserSession() {
		return UserUtils.getUserSession(request);
	}

	/**
	 * 从session获取用户ID
	 * @return
	 * @author wujaixu
	 * @Time 2016-12-6 下午06:43:33
	 */
	public String getUserId() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getUserId();
		}
		return null;
	}

	/**
	 * 从session获取角色ID
	 * @return
	 * @author wujaixu
	 * @Time 2016-12-6 下午06:43:51
	 */
	public String getRoleId() {
		UserSessionBean userSessionBean =  getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getRoleid();
		}
		return null;
	}
	
	/**
	 * 从session获取单位名称
	 * @return
	 * @author wujaixu
	 * @Time 2016-12-6 下午06:44:08
	 */
	public String getDwmc() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getDwmc();
		}
		return null;
	}

	/**
	 * 从session获取用户类型
	 * @return
	 * @author wujaixu
	 * @Time 2016-12-6 下午06:44:22
	 */
	public String getYhlx() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getYhlx();
		}
		return null;
	}

	/**
	 * 从session获取username
	 * @return
	 * @author wujaixu
	 * @Time 2016-12-6 下午06:44:39
	 */
	public String getUserName() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getUserName();
		}
		return null;
	}

	/**
	 * 从session获取企业代码
	 * @return
	 * @author wujaixu
	 * @Time 2016-12-6 下午06:44:54
	 */
	public String getQydm() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getQydm();
		}
		return null;
	}

	/**
	 * 从session获取单位代码
	 * @return
	 * @author wujiaxu
	 * @Time 2016-12-6 下午06:45:26
	 */
	public String getDwdm() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getDwdm();
		}
		return null;
	}

	/**
	 * 从session获取行政区划
	 * @return
	 * @author wujiaxu
	 * @Time 2016-12-6 下午06:45:38
	 */
	public String getXzqh() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getXzqh();
		}
		return null;
	}
	/**
	 * @功能 获取session里的行政区划名称
	 * @参数 @return
	 * @作者 zhangsanjie add 2017-1-10 下午3:55:59
	 * @返回值类型 String
	 */
	public String getXzqhmc() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getXzqh_cn();
		}
		return null;
	}
	public String getBpzydwlb() {
		UserSessionBean userSessionBean = getUserSession();
		if (userSessionBean != null) {
			return userSessionBean.getBpzydwlb();
		}
		return null;
	}

	/**
	 * 判断操作系统
	 * @param mc
	 * @return
	 * @author jiangchunyu
	 * @Time 2017-5-19 上午10:34:50
	 */
	public boolean isLinux( ) {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("linux")) {
		      return true;
		    }
		return false;
	}	
	
	/**
	 * @功能  从oracle里读取二进制文件
	 * @参数 @param objs
	 * @作者 zhangsanjie add 2016-9-21 下午4:08:41
	 * @返回值类型 void
	 */
	public <T> void uploadFilefromOracle(List<T> objs) {
		String path = BaseAction.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(1, path.lastIndexOf("WEB-INF"))+"upload"+File.separator+"myfiles";
		boolean isLinux = isLinux();
		//如果是liunx操作系统
		if(isLinux){
			path = File.separator + path;
		}			    
		OutputStream out = null;
		try {
			File dirpath = new File(path);
			if (!dirpath.exists()) {
				FileUtils.forceMkdir(dirpath);
			}
			for (int i = 0; i < objs.size(); i++) {
				Object object = objs.get(i);
				String url = "";
				byte[] byteArray = null;
				String fjmc = "";
				Class cls = object.getClass();
				BeanInfo beanInfo = Introspector.getBeanInfo(cls, cls.getSuperclass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				if (propertyDescriptors != null && propertyDescriptors.length > 0) {
					for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
						String propertyName = propertyDescriptor.getName();
						Method readMethod = propertyDescriptor.getReadMethod();
						if ("url".equals(propertyName)) {
							url = readMethod.invoke(object).toString();
						}else if("fj".equals(propertyName)){
							byteArray = (byte[]) readMethod.invoke(object);
						}else if("fjmc".equals(propertyName)){
							fjmc = readMethod.invoke(object).toString();
						}
					}
				}
				if(url.length() > 0 && url.indexOf(".") > 0){					
					if(isLinux){
						if(url.indexOf("\\") > -1){
							url = url.replaceAll("\\\\", "/");
						}
					}else{
						if(url.indexOf("/") > -1){
							url =  url.replaceAll("/", "\\\\");
						}
					}
										
					fjmc = url.trim().substring(url.lastIndexOf(File.separator)+1);
					File uploadFile = new File(path, fjmc);
					if(!uploadFile.exists()){
						out = new FileOutputStream(uploadFile);
						if(byteArray!=null && byteArray.length > 0){
					        out.write(byteArray);
						}
					}
				}
			}
		}catch (Exception ex) {
			log.error(ex.toString());
		}finally{
            if (null != out) {  
                try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
            } 
		}
	}

	/**
	 * 
	 * @功能：对爆破作业每个功能操作都加入日志记录
	 * @author:machao @date 2016-7-13 上午11:25:22
	 * @param fwId 每个服务的名称
	 * @param nr 数据存入的内容
	 * @return
	 * @return String
	 */
	public void addLog(String fwId, String nr) {
		String userId = getUserId();
		String userName = getUserName();
		String ip = request.getRemoteAddr();
		String Url = request.getRequestURI();
		commonLogManager.saveOperateLog(fwId, userId, ip, Url, "用户【" + userName+"】"+nr, DateUtils.createCurrentDate());
	}
	
	/**
	 * 添加可操作列表
	 * @param msg
	 * @param url
	 * @param target
	 * @author wujiaxu
	 * @Time 2016-12-6 下午06:52:09
	 */
	public void addTodoList(String msg, String url,String target) {
		todoMap = new HashMap();
		todoMap.put("ms", msg);
		todoMap.put("url", url);
		todoMap.put("target", target);
		todoList.add(todoMap);
	}
	
}
