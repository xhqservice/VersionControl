package com.jadlwork.business.app.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadlsoft.utils.*;
import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.dbutils.SqlMapUtils;
import com.jadlwork.business.app.IAppManager;
import com.jadlwork.model.app.AppAutotaskBean;
import com.jadlwork.model.app.AppJbxxBean;
import com.jadlwork.model.app.AppUpdatelogBean;
import com.jadlwork.model.app.AppUpdateruleBean;
import com.jadlwork.model.app.AppVersionBean;
/**
 * 
 * app管理业务实现类
 * @author wujiaxu
 * @Time 2017-12-20 上午9:13:32
 *
 */
public class AppManager extends BaseManager implements IAppManager {
	private static Logger logger = Logger.getLogger(AppManager.class);

	

	@Override
	public int saveApp(AppJbxxBean appJbxxBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_APP_JBXX"));
		appJbxxBean.setId(id);
		appJbxxBean.setCjsj(DateUtils.createCurrentDate());
		appJbxxBean.setZt(SystemConstants.ZT_TRUE);
		return this.daoUtils.save(appJbxxBean);
	}
	
	@Override
	public int updateAppByFields(AppJbxxBean appJbxxBean, String fields) {
		return this.daoUtils.update(appJbxxBean,fields);
	}

	@Override
	public AppJbxxBean getAppBeanById(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (AppJbxxBean) this.daoUtils.findObject(AppJbxxBean.class, condition);
	}

	@Override
	public int deleteApp(AppJbxxBean jbxxBean) {
		jbxxBean.setZxsj(DateUtils.createCurrentDate());
		jbxxBean.setZt(SystemConstants.ZT_FALSE);
		return this.updateAppByFields(jbxxBean, "zxsj,zt");
	}

	@Override
	public Map getBeanByAppid(String appid) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = daoUtils.find(AppJbxxBean.class, condition);
		if (list == null || list.size()==0) {
			return null;
		}
		return (Map) list.get(0);
	}

	@Override
	public AppVersionBean getAppVersionBeanById(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (AppVersionBean) this.daoUtils.findObject(AppVersionBean.class, condition);
	}

	@Override
	public int saveAppVersion(AppVersionBean appVersionBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_APP_VERSION"));
		appVersionBean.setId(id);
		appVersionBean.setCjsj(DateUtils.createCurrentDate());
		appVersionBean.setZhxgsj(DateUtils.createCurrentDate());
		appVersionBean.setZt(SystemConstants.ZT_TRUE);
		appVersionBean.setType(SystemConstants.APPTYPE_ANDROID);
		appVersionBean.setIsdefault(SystemConstants.APPVERSION_NODEFAULT);
		return this.daoUtils.save(appVersionBean);
	}

	@Override
	public int updateAppVersionByFields(AppVersionBean appVersionBean,
			String fields) {
		appVersionBean.setZhxgsj(DateUtils.createCurrentDate());
		return this.daoUtils.update(appVersionBean,fields);
	}

	@Override
	public int deleteAppVersion(AppVersionBean appVersionBean) {
		appVersionBean.setZxsj(DateUtils.createCurrentDate());
		appVersionBean.setZt(SystemConstants.ZT_FALSE);
		return this.updateAppVersionByFields(appVersionBean, "zxsj,zt");
	}

	@Override
	public boolean checkAppVersion(String appid,String versionid, String version) {
		if(StringUtils.isEmpty(versionid)){
			versionid = "isnull";		
		}
		Map condition = new HashMap();
		condition.put("id", versionid);
		condition.put("appid", appid);
		condition.put("version", version);
		condition.put("type", SystemConstants.APPTYPE_ANDROID);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = daoUtils.find("#app.checkAppVersion", condition);
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean appVersionToDefault(String appid,String version ) {
		try {
			//取消默认
			this.qxDefault(appid);
			//重新设置默认
			this.setDefault(appid, version);
			
		} catch (Exception e) {
			logger.error("设置默认处理出错！", e);
			return false;
		}
		return true;
	}


	/**
	 * 取消默认
	 * @param appid
	 * @author wujiaxu
	 * @Time 2017-12-22 上午11:24:39
	 */
	private void qxDefault(String appid) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("nodefault", SystemConstants.APPVERSION_NODEFAULT);
		condition.put("type", SystemConstants.APPTYPE_ANDROID);
		condition.put("zt", SystemConstants.ZT_TRUE);
		this.daoUtils.execSql("#app.removeDefaultVersion", condition);
	}
	
	/**
	 * 设置默认
	 * @param appid
	 * @author wujiaxu
	 * @Time 2017-12-22 上午11:24:39
	 */
	private void setDefault(String appid,String version) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("version", version);
		condition.put("type", SystemConstants.APPTYPE_ANDROID);
		condition.put("default", SystemConstants.APPVERSION_DEFAULT);
		condition.put("zt", SystemConstants.ZT_TRUE);
		this.daoUtils.execSql("#app.upgradeVersion", condition);
	}

	@Override
	public int saveAppVersionByFileds(AppVersionBean appVersionBean,
			String fields) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_APP_VERSION"));
		appVersionBean.setId(id);
		appVersionBean.setCjsj(DateUtils.createCurrentDate());
		appVersionBean.setZt(SystemConstants.ZT_TRUE);
		return this.daoUtils.save(appVersionBean, fields);
	}

	@Override
	public Map getIOSAppByAppid(String appid) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("type", SystemConstants.APPTYPE_IOS);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = this.daoUtils.find(AppVersionBean.class, condition);
		if(list != null && list.size() > 0){
			return (Map) list.get(0);
		}
		return null;
	}

	@Override
	public AppAutotaskBean getAutoTaskBeanById(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (AppAutotaskBean) this.daoUtils.findObject(AppAutotaskBean.class, condition);
	}

	@Override
	public int saveAppAutotask(AppAutotaskBean autotaskBean) {
		String id = String.valueOf(this.daoUtils.getNextval("Q_APP_AUTOTASK"));
		autotaskBean.setId(id);
		autotaskBean.setCjsj(DateUtils.createCurrentDate());
		autotaskBean.setZhxgsj(DateUtils.createCurrentDate());
		autotaskBean.setZt(SystemConstants.ZT_TRUE);
		autotaskBean.setTodozt(SystemConstants.APPTASKTODOZT_NO);
		return this.daoUtils.save(autotaskBean);
	}

	@Override
	public int updateAutoTaskByFields(AppAutotaskBean autotaskBean,
			String fields) {
		return this.daoUtils.update(autotaskBean,fields);
	}

	@Override
	public int deleteAutotask(AppAutotaskBean autotaskBean) {
		autotaskBean.setZxsj(DateUtils.createCurrentDate());
		autotaskBean.setZt(SystemConstants.ZT_FALSE);
		return this.updateAutoTaskByFields(autotaskBean, "zxsj,zt");

	}

	@Override
	public List getAutoTaskList() {
		Map condition = new HashMap();
		condition.put("todozt", SystemConstants.APPTASKTODOZT_NO);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = this.daoUtils.find("#app.getAutoTaskList", condition);
		return list;
	}


	@Override
	public void upgradeVersion(String taskid, String appid, String version) {
		boolean b = this.appVersionToDefault(appid,version);
		if(b){
			AppAutotaskBean autotaskBean = new AppAutotaskBean();
			autotaskBean.setId(taskid);
			autotaskBean.setTodozt(SystemConstants.APPTASKTODOZT_YES);
			this.updateAutoTaskByFields(autotaskBean, "todozt");
		}
		
	}

	@Override
	public AppUpdateruleBean getUpdateRuleById(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return (AppUpdateruleBean) this.daoUtils.findObject(AppUpdateruleBean.class, condition);
	}

	@Override
	public int saveUpdateRule(AppUpdateruleBean updateruleBean) {
		String id = String.valueOf(this.daoUtils.getNextval("q_app_updaterule"));
		updateruleBean.setId(id);
		updateruleBean.setCjsj(DateUtils.createCurrentDate());
		updateruleBean.setZhxgsj(DateUtils.createCurrentDate());
		updateruleBean.setZt(SystemConstants.ZT_TRUE);
		return this.daoUtils.save(updateruleBean);
	}

	@Override
	public int updateRuleByFields(AppUpdateruleBean updateruleBean,
			String fields) {
		return this.daoUtils.update(updateruleBean, fields);
	}

	@Override
	public int deleteRule(AppUpdateruleBean updateruleBean) {
		updateruleBean.setZxsj(DateUtils.createCurrentDate());
		updateruleBean.setZt(SystemConstants.ZT_FALSE);
		return this.updateRuleByFields(updateruleBean, "zxsj,zt");
	}

	@Override
	public List getAppListBySsxmAndApptype(String ssxm, String apptype) {
		Map condition = new HashMap();
		condition.put("isdefault", SystemConstants.APPVERSION_DEFAULT);
		condition.put("zt", SystemConstants.ZT_TRUE); 
		condition.put("apptype", apptype); 
		condition.put("ssxm", ssxm); 
		List list = this.daoUtils.find("#app.getAppListBySsxmAndApptype", condition);
		return list;
	}

	
	@Override
	public Map getUploadVersionInfo(String appid, String sbid, String dwdm,
			String xzqh) {
		Map ruleVersionMap = this.getRuleVersion(appid, sbid, dwdm,
				xzqh);
		//根据规则找到版本直接返回
		if(ruleVersionMap != null && ruleVersionMap.size() > 0){
			return ruleVersionMap;
		}
		
		Map defaultVersionMap = this.getDefaultVersionIno(appid);
		return defaultVersionMap;
	}

	/**
	 * 根据规则获取版本
	 * @param appid
	 * @param sbid
	 * @param dwdm
	 * @param xzqh
	 * @return
	 * @author wujiaxu
	 * @Time 2017-12-25 下午4:19:29
	 */
	private Map getRuleVersion(String appid, String sbid, String dwdm,
			String xzqh) {
		//设备ID、单位代码、行政区划至少存在1个
		if((sbid == null || "".equals(sbid))
				&&(dwdm == null || "".equals(dwdm))
				&&(xzqh == null || "".equals(xzqh))){
			return null;
		}
		
		String sql = SqlMapUtils.getSql("select", "#app.getRuleVersion");
		
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("zt", SystemConstants.ZT_TRUE); 
		StringBuffer whereCond = new StringBuffer();
		whereCond.append(" 1=2 ");
		if(sbid != null && !"".equals(sbid)){
			
			whereCond.append(" or (target = :target_sbid and targetlx = '"+SystemConstants.TARGETLX_SB+"')");
			condition.put("target_sbid", sbid);
		} 
		
		if(dwdm != null && !"".equals(dwdm)){
			whereCond.append(" or (target = :target_dwdm and targetlx = '"+SystemConstants.TARGETLX_DW+"')");
			condition.put("target_dwdm", dwdm);
		} 
		
		if(xzqh != null && !"".equals(xzqh)){
			whereCond.append(" or (target = :target_xzqh and targetlx = '"+SystemConstants.TARGETLX_DQ+"')");
			condition.put("target_xzqh", xzqh);
		}
	
		sql = sql.replaceAll(":whereCond","(" +whereCond.toString()+ ")");
		List list = this.daoUtils.find(sql, condition);
		if(list != null && list.size() > 0){
			return (Map) list.get(0);
		}
		return null;
	}

	/**
	 * 根据行政区划获取使用行政区划判断规则的SQL
	 * @param xzqh	传递的行政区划
	 * @return
	 */
	private String getXzqhSql(String xzqh) {
		int xzhqhLevel = XzhqhUtils.getXZHQHLevel(xzqh);


//		if ("000000".equals(xzqh)) {
//			return IConstants.MINISTRY; // 公安部级
//		} else if (xzqh.substring(2, 6).equals("0000")) {
//			return IConstants.PROVINCE; // 省级
//		} else if (xzqh.substring(4, 6).equals("00")) {
//			return IConstants.CITY; // 地市级
//		} else {
//			return IConstants.COUNTRY; // 县级
//		}

		String sql = "";
		switch (xzhqhLevel) {
			case IConstants.MINISTRY:	//公安部000000  必须一致
				sql = "";
				break;
			case IConstants.PROVINCE:	//省级，必须一致
				sql = "";
				break;
			case IConstants.CITY:	//地市级，前两位必须相同
				sql = "";
				break;
			case IConstants.COUNTRY:
				sql = "";
				break;
		}

		return sql;
	}

	@Override
	public Map getDefaultVersionIno(String appid) {
		Map condition = new HashMap();
		condition.put("appid", appid);
		condition.put("isdefault", SystemConstants.APPVERSION_DEFAULT);
		condition.put("apptype", SystemConstants.APPTYPE_ANDROID);
		condition.put("zt", SystemConstants.ZT_TRUE);
		List list = this.daoUtils.find("#app.getDefaultVersionIno", condition);
		if(list != null && list.size() > 0){
			return (Map) list.get(0);
		}
		return null;
	}
	
	/**
	  * 获取APP中心要显示的数据集合
	 * @param consoleType 终端类型  ANDROID、IOS
	 * @param ssxm 所属平台
	 * @return
	 * @see com.jadlwork.business.app.IAppManager#getToShowList(java.lang.String, java.lang.String)
	 */
	@Override
	public List getToShowList(String consoleType, String ssxm) {
		Map condition = new HashMap();
		condition.put("consoleType", consoleType);
		condition.put("ssxm", ssxm);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return daoUtils.find("#app.getToShowList", condition);
	}

	@Override
	public void saveUpdateLog(String appid, String sbid, String dwdm,
			String xzqh, String version) {
		Map map = this.getBeanByAppid(appid);
		if(map == null || map.size() <= 0){
			throw new RuntimeException("未找到APPID【"+appid+"】对应的应用！");
		}
		
		String ssxm = (String) map.get("ssxm");
		String appname = (String) map.get("appname");
		
		AppUpdatelogBean updatelogBean = new AppUpdatelogBean();
		String id = String.valueOf(this.daoUtils.getNextval("Q_APP_UPDATELOG"));
		updatelogBean.setId(id);
		updatelogBean.setAppid(appid);
		updatelogBean.setAppname(appname);
		updatelogBean.setDwdm(dwdm);
		updatelogBean.setSbid(sbid);
		updatelogBean.setXzqh(xzqh);
		updatelogBean.setVersion(version);
		updatelogBean.setSsxm(ssxm);
		updatelogBean.setXzsj(DateUtils.createCurrentDate());
		this.daoUtils.save(updatelogBean);
	}

}
