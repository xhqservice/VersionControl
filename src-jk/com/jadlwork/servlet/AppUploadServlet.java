package com.jadlwork.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jadlsoft.utils.SpringBeanFactory;
import com.jadlwork.business.app.IAppManager;
/**
 * 
 * App下载接口
 * @author wujiaxu
 * @Time 2017-12-25 下午2:25:37
 *
 */
public class AppUploadServlet extends BaseServlet {
	private static final Logger log = Logger.getLogger(AppUploadServlet.class);
	private IAppManager appManager = (IAppManager) SpringBeanFactory
			.getBean("appManager");

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		arg1.setContentType("text/html;charset=UTF-8");
		arg1.setCharacterEncoding("UTF-8");
		PrintWriter out = arg1.getWriter();
		
		try {
			/**
			 * 1、验证
			 */
			String key = arg0.getParameter("key");
			String para = URLDecoder.decode(arg0.getParameter("para") , "UTF-8");
		

			// 1.1、验证KEY
			String checkKeyResult = checkKey(key);
			if (!checkKeyResult.equals("")) {
				out.print(buildJsonResult(checkKeyResult, null));
				return;
			}
	
			// 1.2、验证参数
			para = para.replaceAll("“", "\"");
					
			String checkParaResult = checkPara(para);
			if (!checkParaResult.equals("")) {
				out.print(buildJsonResult(checkParaResult, null));
				return;
			}

			// 1.3、处理参数
			Map paraMap = (Map) JSONObject.fromObject(para);
			
			
			/**
			 * 2、调用处理方法
			 */
			if(IsValidInterface(paraMap, "appCenter")){
				//APP商店
				this.appCenter(arg0, arg1, paraMap);
			}else if(IsValidInterface(paraMap, "appIndex")){
				//APP可下载列表
				this.appIndex(arg0,arg1,paraMap);
			}else if(IsValidInterface(paraMap, "appVersionCheck")){
				//APP版本校验
				this.appVersionCheck(arg0,arg1,paraMap);
			}else if(IsValidInterface(paraMap, "appVersionUpdate")){
				//APP版本更新
				this.appVersionUpdate(arg0,arg1,paraMap);
			}else{
				out.print(buildJsonResult("非法请求!", null));
			}

		} catch (Exception e) {
			log.error("APP下载接口处理出错!", e);
			out.print(buildJsonResult(error_sys, null));
		}

	}
	
	

	/**
	 * 前往APP商店
	 * @param request
	 * @param response
	 * @param paraMap
	 * @throws IOException
	 */
	private void appCenter(HttpServletRequest request, HttpServletResponse response,
			Map paraMap) throws IOException {
		String ssxm = (String) paraMap.get("ssxm");	//所属平台
		request.setAttribute("ssxm", ssxm);
		try {
			request.getRequestDispatcher("appCenter/appCenter.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * APP 可下载列表
	 * 
	 * @param paraMap	请求的参数
	 * @return 
	 * 	结构
	 * 	{
	 * 		returntype	:	'0',
	 * 		returnmsg	:	
	 * 		[
	 * 			{
	 * 				appid		:	'APPID',
	 * 				appname		:	'APP名称',
	 * 				version		:	'版本号',
	 * 				versiondesc	:	'版本说明',
	 * 				ssxm		:	'所属平台代码',
	 * 				ssxm_dicvalue:	'所属平台名称',
	 * 				appdesc		:	'APP简介',
	 * 				apk			:	'APK名称',
	 * 				apksrc		:	'APK地址',
	 * 				type		:	'APP类型 0：安卓 1:IOS'
	 * 			}	
	 * 		]
	 * 	}
	 * 
	 * 
	 * @author wujiaxu
	 * @Time 2017-12-25 下午2:42:01
	 */
	private void appIndex(HttpServletRequest arg0, HttpServletResponse arg1,
			Map paraMap) throws IOException {
		PrintWriter out = arg1.getWriter();
		
		String ssxm = 	(String) paraMap.get("ssxm");//所属平台
		String apptype = paraMap.get("apptype").toString();// APP类型  0:android  1:IOS
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		if (ssxm == null || "".equals(ssxm)) {
			error = "所属平台[ssxm]为空";
		}else if (apptype == null || "".equals(apptype)) {
			error = "APP类型[apptype]为空";
		}
		
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult("错误的请求", null));
			return;
		}
		/**
		 * 2、获取下载列表
		 */
		List appList = appManager.getAppListBySsxmAndApptype(ssxm,apptype);
	
		
		/**
		 * 3、返回
		 */
		out.print(buildJsonResult("", appList));
	}

	
	/**
	 * APP 版本校验
	 * @param paraMap
	 * @return 
	 * 	结构
	 * 	{
	 * 		returntype	:	'0',
	 * 		returnmsg	:	
	 * 		{
	 * 				result		:	'0:版本一致    1:版本不一致  2:无法识别',
	 * 				resultmsg	:	'结果描述',
	 * 				version		:	'最新版本号',
	 * 				versiondesc	:	'最新版本说明'
	 * 		}
	 * 	}
	 * @author wujiaxu
	 * @Time 2017-12-25 下午3:03:59
	 */
	private void appVersionCheck(HttpServletRequest arg0, HttpServletResponse arg1,
			Map paraMap) throws Exception {
 		PrintWriter out = arg1.getWriter();
		String appid = 	(String) paraMap.get("appid");		//APPID
		String version = (String) paraMap.get("version");	//版本号

		String sbid = 	(String) paraMap.get("sbid");		//设备ID
		String dwdm = 	(String) paraMap.get("dwdm");		//单位代码
		String xzqh = 	(String) paraMap.get("xzqh");		//行政区划
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		if (appid == null || "".equals(appid)) {
			error = "APPID[appid]为空";
		}else if(version == null || "".equals(version)){
			error = "版本[version]为空";
		}
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult(error, null));
			return;
		}
		Map reMap = new HashMap();
		reMap.put("result", "2");
		reMap.put("resultmsg", "无法识别");
		
		
		Map versionInfo = appManager.getUploadVersionInfo(appid,sbid,dwdm,xzqh);	
		if(versionInfo != null && versionInfo.size() > 0){
			String version_new = (String) versionInfo.get("version");
			String version_desc = (String) versionInfo.get("version_desc");
			//比对两个版本
			if(version_new != null && !version_new.equals(version)){
				reMap.put("result", "1");
				reMap.put("resultmsg", "版本不一致");
			}else{
				reMap.put("result", "0");
				reMap.put("resultmsg", "版本一致");
			}	
			reMap.put("version",version_new);
			reMap.put("versiondesc", version_desc);
			
		}
		
		/**
		 * 3、 组装返回
		 */

		out.print(buildJsonResult("", reMap));
		return;
		
	}



	
	/**
	 * APP 版本更新
	 * @param paraMap
	 * @return 
	 * 	结构
	 * 	{
	 * 		returntype	:	'0',
	 * 		returnmsg	:	
	 * 		{
	 * 				appid		:	'APPID',
	 * 				appname		:	'APP名称',
	 * 				version		:	'版本号',
	 * 				versiondesc	:	'版本说明',
	 * 				ssxm		:	'所属平台代码',
	 * 				ssxm_dicvalue:	'所属平台名称',
	 * 				appdesc		:	'APP简介',
	 * 				apk			:	'APK名称',
	 * 				apksrc		:	'APK地址',
	 * 				type		:	'APP类型 0：安卓 1:IOS'
	 * 		}	
	 * 		
	 * 	}
	 * @author wujiaxu
	 * @throws IOException 
	 * @Time 2017-12-25 下午3:03:59
	 */
	private void appVersionUpdate(HttpServletRequest arg0,
			HttpServletResponse arg1, Map paraMap) throws IOException {
		PrintWriter out = arg1.getWriter();
		String appid = 	(String) paraMap.get("appid");		//APPID

		String sbid = 	(String) paraMap.get("sbid");		//设备ID
		String dwdm = 	(String) paraMap.get("dwdm");		//单位代码
		String xzqh = 	(String) paraMap.get("xzqh");		//行政区划
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		if (appid == null || "".equals(appid)) {
			error = "APPID[appid]为空";
		}
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult(error, null));
			return;
		}
		
		Map versionInfo = appManager.getUploadVersionInfo(appid,sbid,dwdm,xzqh);	
		
		/**
		 * 2、保存更新记录
		 * 
		 */
		if(versionInfo != null && versionInfo.size() > 0){
			String version_new = (String) versionInfo.get("version");
			appManager.saveUpdateLog(appid,sbid,dwdm,xzqh,version_new);
		}
		
		
		/**
		 * 3、 组装返回
		 */

		if(versionInfo == null ){
			out.print(buildJsonResult("", ""));
		}else{
			out.print(buildJsonResult("", versionInfo));
		}
		return;
		
	}
	
	/**
	 * 检查KEY是否符合要求。KEY是每个接口调用的凭证，包含了单位信息、使用接口的有效期信息、接口的版本号信息等
	 * @param key 
	 * @return
	 */
	protected String checkKey(String key) {
		//System.out.println("key = " + key);
		return "";
	}

}
