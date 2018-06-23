package com.jadlwork.business.app;

import java.util.List;
import java.util.Map;

import com.jadlwork.model.app.AppAutotaskBean;
import com.jadlwork.model.app.AppJbxxBean;
import com.jadlwork.model.app.AppUpdateruleBean;
import com.jadlwork.model.app.AppVersionBean;

/**
 * 
 * app业务管理接口类
 * @author wujiaxu
 * @Time 2017-12-20 上午9:14:52
 *
 */
public interface IAppManager {

	/**
	 * 保存APP基本信息
	 * @param appJbxxBean APP对象
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午9:15:32
	 */
	int saveApp(AppJbxxBean appJbxxBean);
	
	/**
	 * 根据字段更新app
	 * @param appJbxxBean
	 * @param fields
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午9:16:39
	 */
	int updateAppByFields(AppJbxxBean appJbxxBean, String fields);
	
	
	/**
	 * 根据主键ID获取APP对象
	 * @param id
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午9:17:09
	 */
	AppJbxxBean getAppBeanById(String id);
	
	/**
	 * 注销APP
	 * @param jbxxBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 上午9:17:50
	 */
	int deleteApp(AppJbxxBean jbxxBean);

	/**
	 * 根据APPID获取应用
	 * @param appid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 下午2:20:29
	 */
	Map getBeanByAppid(String appid);

	
	/**
	 * 根据主键ID获取APP版本bean
	 * @param id
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 下午5:06:02
	 */
	AppVersionBean getAppVersionBeanById(String id);

	/**
	 * 保存app版本
	 * @param appVersionBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 下午5:07:35
	 */
	int saveAppVersion(AppVersionBean appVersionBean);

	/**
	 * 更新app版本
	 * @param appVersionBean
	 * @param fields
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 下午5:09:47
	 */
	int updateAppVersionByFields(AppVersionBean appVersionBean, String fields);

	/**
	 * 注销APP版本
	 * @param appVersionBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 下午5:12:27
	 */
	int deleteAppVersion(AppVersionBean appVersionBean);

	/**
	 * 验证版本是否存在
	 * @param versionid
	 * @param version
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-20 下午5:16:44
	 */
	boolean checkAppVersion(String appid,String versionid, String version);


	/**
	 * 根据APPID获取IOS应用
	 * @param appid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午3:41:25
	 */
	Map getIOSAppByAppid(String appid);
	
	
	
	/**
	 * 设置默认版本
	 * @param appVersionBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午2:45:38
	 */
	boolean appVersionToDefault(String appid, String version);
	
	/**
	 * 根据字段保存版本
	 * @param appVersionBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午3:35:32
	 */
	int saveAppVersionByFileds(AppVersionBean appVersionBean,String fields);

	/**
	 * 根据ID获取定时任务BEAN
	 * @param id
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午4:10:04
	 */
	AppAutotaskBean getAutoTaskBeanById(String id);

	/**
	 * 保存app定时任务
	 * @param autotaskBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午4:12:04
	 */
	int saveAppAutotask(AppAutotaskBean autotaskBean);

	/**
	 * 更新定时任务
	 * @param autotaskBean
	 * @param fields
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午4:14:34
	 */
	int updateAutoTaskByFields(AppAutotaskBean autotaskBean, String fields);

	/**
	 * 注销定时任务
	 * @param autotaskBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-21 下午4:15:18
	 */
	int deleteAutotask(AppAutotaskBean autotaskBean);

	/**
	 * 获取所有定时任务
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-22 上午10:41:30
	 */
	List getAutoTaskList();

	/**
	 * 
	 * 升级版本
	 * @author wujiaxu
	 * @param version 
	 * @param appid 
	 * @param taskid 
	 * @Time 2017-12-22 上午10:58:22
	 */
	void upgradeVersion(String taskid, String appid, String version);

	
	/**
	 * 根据ID获取更新规则
	 * @param id
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-22 下午2:44:52
	 */
	AppUpdateruleBean getUpdateRuleById(String id);

	/**
	 * 保存更新规则
	 * @param updateruleBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-22 下午2:47:20
	 */
	int saveUpdateRule(AppUpdateruleBean updateruleBean);

	/**
	 * 更新下载规则
	 * @param updateruleBean
	 * @param fields
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-22 下午2:55:02
	 */
	int updateRuleByFields(AppUpdateruleBean updateruleBean, String fields);

	/**
	 * 注销规则
	 * @param updateruleBean
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-22 下午2:56:37
	 */
	int deleteRule(AppUpdateruleBean updateruleBean);

	/**
	 * 根据所属平台和APP类型获取APP下载列表
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-25 下午2:47:35
	 */
	List getAppListBySsxmAndApptype(String ssxm,String apptype);

	/**
	 * 获取APP下载版本信息
	 * @param appid
	 * @param sbid
	 * @param dwdm
	 * @param xzqh
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-25 下午4:12:09
	 */
	Map getUploadVersionInfo(String appid, String sbid, String dwdm, String xzqh);

	/**
	 * 获取APP默认版本信息
	 * @param appid
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-25 下午5:06:44
	 */
	Map getDefaultVersionIno(String appid);

	/**
	 * 获取APP中心要显示的数据集合
	 * @param consoleType 终端类型  ANDROID、IOS
	 * @param ssxm 所属平台
	 * @return
	 */
	List getToShowList(String consoleType, String ssxm);

	/**
	 * 保存APP下载日志
	 * @param appid
	 * @param sbid
	 * @param dwdm
	 * @param xzqh
	 * @param version
	 * @author wujiaxu
	 * @Time 2017-12-29 上午10:47:00
	 */
	void saveUpdateLog(String appid, String sbid, String dwdm, String xzqh,
			String version);

	
}
