package com.jadlwork.servlet;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.log4j.Logger;

import antlr.collections.impl.LList;

import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.SpringBeanFactory;
import com.jadlsoft.utils.StringUtils;
import com.jadlwork.business.scjbak.IScjBakManager;
import com.jadlwork.model.scjbak.ScjBakJbxxBean;
import com.jadlwork.utils.FileSHA1Utils;
import com.jadlwork.utils.FileUtils;
import com.jadlwork.utils.ZipUtils;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: HandsetCloudBackupServlet  
 * @描述: 手持机云备份接口
 * @作者: lcx
 * @时间: 2018-2-5 下午4:03:49
 */
public class ScjBakServlet extends BaseServlet {
	private static final Logger log = Logger.getLogger(ScjBakServlet.class);

	private IScjBakManager scjBakManager = (IScjBakManager) SpringBeanFactory
			.getBean("scjBakManager");
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			/**
			 * 1、验证
			 */
			String key = request.getParameter("key");
			String parameter = request.getParameter("para");
			String para = URLDecoder.decode(parameter , "UTF-8");
		

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
			log.info("手持机云备份接口，接收参数信息：【"+paraMap+"】");
			
			/**
			 * 2、调用处理方法
			 */
			if(IsValidInterface(paraMap, "dh_check")){
				//代号校验
				checkDhByDwdmAndFlashid(request,response,paraMap);
			}else if(IsValidInterface(paraMap, "dh_bind")){
				//代号绑定
				scjDhBind(request,response,paraMap);
			}else if(IsValidInterface(paraMap, "sb_iszb")){
				//手持机在保检测
				checkScjIsGuarantee(request,response,paraMap);
			}else if (IsValidInterface(paraMap, "sha_check")) {
				//数字签名校验
				this.sha_check(request, response, paraMap);
			}else if(IsValidInterface(paraMap, "tz_uploadfinish")){
				//备份完成通知
				this.tz_uploadfinish(request, response, paraMap);
			}else if(IsValidInterface(paraMap, "backup_list")){
				//查询还原列表
				this.backup_list(request, response, paraMap);
			}else if(IsValidInterface(paraMap, "backup_checkpwd")){
				//还原备份校验密码
				this.backup_checkpwd(request, response, paraMap);
			}else{
				out.print(buildJsonResult("非法请求!", null));
			}

		} catch (Exception e) {
			log.error("手持机云备份处理出错!", e);
			out.print(buildJsonResult(error_sys, null));
		}

	}
	
	/**
	 * 数据签名（SHA）验证
	 * 	入参：
	 * 		{
	 * 			dwdm：单位代码
	 * 			flashid：设备ID
	 * 			sha1：数字签名
	 * 		}
	 *  返回：
	 *  	一致：提示   已备份最新数据
	 *  	不一致：返回FTP信息
	 * @param request
	 * @param response
	 * @param paraMap
	 * @throws IOException
	 */
	private void sha_check(HttpServletRequest request, HttpServletResponse response,
			Map paraMap) throws IOException {
		PrintWriter out = response.getWriter();
		String dwdm = (String) paraMap.get("dwdm");
		String flashid = (String) paraMap.get("flashid");
		String sha1 = (String) paraMap.get("sha1");
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		if (StringUtils.isEmpty(dwdm)) {
			error = "单位代码[dwdm]为空";
		}else if (StringUtils.isEmpty(flashid)) {
			error = "设备ID[flashid]为空";
		}else if (StringUtils.isEmpty(sha1)) {
			error = "数字签名[sha1]为空";
		}
		
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult(error, null));
			return;
		}
		
		/**
		 * 2、获取最新一次备份的信息
		 */
		Map map = scjBakManager.getScjLastBakByFlashidAndDwdm(dwdm, flashid);
		
		/**
		 * 3、比对签名
		 */
		if (map == null || !sha1.equals(map.get("sha1"))) {
			//当前还没有备份信息，或者该次备份数据跟最新的数据不一致，需要返回FTP信息
			/**
			 * 3.1、获取FTP
			 */
			Map ftpMap = scjBakManager.getFtpMap();
			if (ftpMap == null || ftpMap.size()==0) {
				out.print(buildJsonResult("服务器没有配置FTP信息", ""));
				return;
			}
			//移除不需要向客户端展示字段
			ftpMap.remove("rootdir");
			
			/**
			 * 3.3、返回给客户端
			 */
			out.print(buildJsonResult("", ftpMap));
			return;
		}else {
			//当前最新备份数据跟该次提交备份的数据一致，返回提示信息，并更新当前备份版本的更新时间
//			int i = scjBakManager.updateScjBakXgsj(map.get("bakid").toString(), DateUtils.createCurrentDate());
//			if (i<=0) {
//				out.print(buildJsonResult("更新服务器版本信息失败", ""));
//				return;
//			}
			out.print(buildJsonResult("已备份最新数据", ""));
			return;
		}
	}
	
	/**
	 * 备份完成通知接口
	 * 		根据文件名去ftp上传目录取文件并转移到正式备份目录，正式备份目录即FTP根目录下scjbak/单位代码/flashid/，
	 * 	    转移完成之后保存新的备份记录，并根据最多保存次数调整记录数。一个flashid可以保存多少个数据根据单位代码去库中取
	 * 	入参：
	 * 		{
	 * 			filename：上传文件名
	 * 			dwdm：dwdm
	 * 			flashid：设备ID
	 * 		}
	 *  返回：
	 *  	成功或者失败
	 * @param request
	 * @param response
	 * @param paraMap
	 * @throws IOException
	 */
	private void tz_uploadfinish(HttpServletRequest request, HttpServletResponse response,
			Map paraMap) throws IOException {
		
		PrintWriter out = response.getWriter();
		String dwdm = paraMap.get("dwdm") == null ? "" : paraMap.get("dwdm").toString();
		String flashid = paraMap.get("flashid") == null ? "" : paraMap.get("flashid").toString();
		String filename = paraMap.get("filename") == null ? "" : paraMap.get("filename").toString();
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		ScjBakJbxxBean jbxxBean = null;
		if (StringUtils.isEmpty(dwdm)) {
			error = "单位代码[dwdm]为空";
		}else if (StringUtils.isEmpty(flashid)) {
			error = "设备ID[flashid]为空";
		}else if (StringUtils.isEmpty(filename)) {
			error = "上传文件名[filename]为空";
		}else {
			jbxxBean = scjBakManager.getScjJbxxBean(dwdm, flashid);
			if (jbxxBean == null || StringUtils.isEmpty(jbxxBean.getId()) 
					|| StringUtils.isEmpty(jbxxBean.getDh())) {
				error = "未找到该设备代号信息";
			}
		}
		
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult(error, null));
			return;
		}
		
		/**
		 * 2、根据文件名和库中存储的FTP根目录获取文件
		 */
		File uploadFile;
		try {
			uploadFile = scjBakManager.getFtpTmpFile(filename);
		} catch (Exception e) {
			log.info("获取上传文件失败");
			out.print(buildJsonResult("获取上传文件失败", null));
			return;
		}
		
		if (!uploadFile.isFile()) {
			log.info("上传文件不存在");
			out.print(buildJsonResult("上传文件不存在", null));
			return;
		}
		
		/**
		 * 3、移动文件到正式保存目录	--FTP根目录/scjbak/单位代码/flashid/
		 */
		
		File destDir = scjBakManager.getBakFileDir(dwdm, flashid);
		
		/*
		 * 处理文件名
		 */
		File destFile = new File(destDir, filename);
		
		/*
		 * 移动文件、返回结果
		 */
		try {
			/**
			 * 4.1、移动文件到正式保存路径
			 */
			boolean b = uploadFile.renameTo(destFile);
			if (!b) {
				destFile.delete();
				b = uploadFile.renameTo(destFile);
			}
			
			if (!b) {
				out.print(buildJsonResult("服务器移动文件失败", null));
				return;
			}
			
			/**
			 * 4.2、保存新的备份数据到库
			 */
			try {
				String filesize = FileUtils.getFileSize(destFile.getAbsolutePath(), FileUtils.SIZEFORMAT_M, 2);
				filename = File.separator + "scjbak" + File.separator + dwdm + File.separator + flashid + File.separator + filename;
				String sha1 = "";
				try {
					
					/**
					 * 调整，sha1从解压后的文件中取
					 */
					try {
						String tmpRootPath = ZipUtils.decompressZip(destFile.getAbsolutePath(), null, true);
						
						File tmpRoot = new File(tmpRootPath);
						File[] listFiles = tmpRoot.listFiles();
						if (listFiles == null || listFiles.length==0) {
							out.print(buildJsonResult("解压文件里面没有内容", null));
							return;
						}
						
						File tmpFile = FileUtils.findFile(tmpRootPath, ".db");
						
						if (tmpFile == null) {
							out.print(buildJsonResult("未在压缩包中找到文件名中包含.db的文件", null));
							return;
						}
						try {
							sha1 = FileSHA1Utils.getFileSha1(tmpFile.getAbsolutePath());
						} catch (Exception e) {
							out.print(buildJsonResult("获取备份文件SHA1值失败", ""));
							return;
						}
					} catch (Exception e) {
						out.print(buildJsonResult("解压文件失败", null));
						return;
					}
					
					/**
					 * 处理，将无用目录删除，在这里删除防止某个删不掉导致一直存留
					 */
					File parentFile = destFile.getParentFile();
					File[] files = parentFile.listFiles(new FileFilter() {
						
						@Override
						public boolean accept(File fileitem) {
							if (fileitem != null && fileitem.isDirectory()) {
								return true;
							}
							return false;
						}
					});
					for (File file : files) {
						if (file.isDirectory()) {
							FileUtils.delFolder(file.getAbsolutePath());
						}
					}
					
//					sha1 = FileSHA1Utils.getSha1(destFile);
				} catch (Exception e) {
					out.print(buildJsonResult("获取备份文件SHA1值失败", ""));
					return;
				}
				scjBakManager.saveScjBakAfterUpload(jbxxBean, filename, filesize, sha1);
			} catch (Exception e) {
				out.print(buildJsonResult("保存备份信息到库失败", ""));
				return;
			}
			
			out.print(buildJsonResult("", "文件处理完成"));
			
		} catch (Exception e) {
			/**
			 * 4、移动失败
			 */
			out.print(buildJsonResult("服务器移动文件异常", null));
		}
		
	}
	
	
	/**
	 * 查询还原列表
	 * 	入参：
	 * 		{
	 * 			filename：上传文件名
	 * 			dwdm：dwdm
	 * 			sblx：设备类型
	 * 			yylx：应用类型
	 * 		}
	 *  返回：
	 *  	{
	 *  		ftpinfo:{
	 *  			ftpuser:用户名
	 *  			ftppwd:密码
	 *  			ftpport:端口
	 *  			ftpip:ftpIP地址
	 *  		},
	 *  		hylist:[
	 *  			{
	 *					dh:手持机代号  
	 *					bfsj:备份时间 
	 *					filesize:文件大小（M）  
	 *					sha1:数字签名
	 *					filename:备份文件地址[ftp根目录下的文件地址]
	 *  			},
	 *  			...
	 *  		]
	 *  	}
	 * @param request
	 * @param response
	 * @param paraMap
	 * @throws IOException
	 */
	private void backup_list(HttpServletRequest request, HttpServletResponse response,
			Map paraMap) throws IOException {
		PrintWriter out = response.getWriter();
		String dwdm = paraMap.get("dwdm") == null ? "" : paraMap.get("dwdm").toString();
		String sblx = paraMap.get("sblx") == null ? "" : paraMap.get("sblx").toString();
		String yylx = paraMap.get("yylx") == null ? "" : paraMap.get("yylx").toString();
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		if (StringUtils.isEmpty(dwdm)) {
			error = "单位代码[dwdm]为空";
		}
		
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult(error, null));
			return;
		}
		
		/**
		 * 2、取出FTP信息
		 */
		Map ftpMap = scjBakManager.getFtpMap();
		if (ftpMap == null || ftpMap.size()==0) {
			log.info("系统未配置FTP信息");
			out.print(buildJsonResult("系统未配置FTP信息", null));
			return;
		}
		ftpMap.remove("rootdir");	//移除不需要向客户端返回字段
		
		/**
		 * 3、取出该单位下的所有可用备份信息
		 */
		List hyList = scjBakManager.getHyList(dwdm, sblx, yylx);
		if (hyList == null) {
			hyList = new ArrayList();
		}
		
		/**
		 * 3.1、整理数据	--暂时无需整理
		 */
		
		/**
		 * 4、返回
		 */
		Map resMap = new HashMap();
		resMap.put("ftpinfo", ftpMap);
		resMap.put("hylist", hyList);
		
		String res = buildJsonResult("", resMap);
		//处理返回路径信息，JSONObject加上了转义字符	--保证返回的路径跟库中的一致
//		res = res.replace(File.separator+File.separator, File.separator);
		out.print(res);
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
	
	
	/**
	 * 手持机代号校验
	 * @param paraMap 
	 * @return
	 * @throws Exception 
	 */
	private void checkDhByDwdmAndFlashid(HttpServletRequest arg0, HttpServletResponse arg1,Map paraMap) throws Exception{
		PrintWriter out = arg1.getWriter();
		String dwdm = (String)paraMap.get("dwdm");		//单位代码
		String flashid = (String)paraMap.get("flashid");//设备ID
		Map map = scjBakManager.getDhByDwdmAndFlashid(dwdm,flashid);
		if(map != null && map.size()>0){
			//查询到数据则把代号、最后的备份时间返回
			Map returnmsg = new HashMap();
			String backtime =  (String)map.get("xgsj");
			returnmsg.put("dh", (String)map.get("dh"));
			returnmsg.put("backtime", backtime);
			out.print(buildJsonResult("", returnmsg));
			return;
		}else{
			//未查询到数据，告知客户端未设置代号
			String error="未设置";
			out.print(buildJsonResult(error, null));
			return;
		}
	}
	
	
	/**
	 * 手持机代号绑定
	 * @param paraMap 
	 * @return
	 * @throws Exception 
	 */
	private void scjDhBind(HttpServletRequest arg0, HttpServletResponse arg1,Map paraMap) throws Exception{
		PrintWriter out = arg1.getWriter();
		String error="";
		String mm = (String)paraMap.get("mm");	//还原密码
		String dh = (String)paraMap.get("dh");	//代号
		String dwdm = (String)paraMap.get("dwdm");//单位代码
		String flashid = (String)paraMap.get("flashid");//设备ID
		String sblx = (String)paraMap.get("sblx");//设备类型
		String yylx = (String)paraMap.get("yylx");//应用类型
		
		/**
		 * 1、验证
		 */
		
		//1.1、还原密码非空约束
		if(StringUtils.isEmpty(mm)){
			error="还原密码为空！";
			out.print(buildJsonResult(error, null));
			return;
		}
		if(StringUtils.isEmpty(sblx)){
			error="设备类型为空！";
			out.print(buildJsonResult(error, null));
			return;
		}
		if(StringUtils.isEmpty(yylx)){
			error="应用类型为空！";
			out.print(buildJsonResult(error, null));
			return;
		}
		
		
		//1.2、同一单位代号唯一约束
		if(!scjBakManager.checkDhOnly(dwdm, dh)){
			error="代号重复";
			out.print(buildJsonResult(error, null));
			return;
			
		}
		
		/**
		 * 2、绑定
		 */

		ScjBakJbxxBean jbxxBean = new ScjBakJbxxBean();
		jbxxBean.setDh(dh);
		jbxxBean.setDwdm(dwdm);
		jbxxBean.setFlashid(flashid);
		jbxxBean.setMm(mm);
		jbxxBean.setSblx(sblx);
		jbxxBean.setYylx(yylx);
		int i = scjBakManager.saveScjBakJbxx(jbxxBean);
		
		if(i <= 0){
			error="代号绑定失败！";
			out.print(buildJsonResult(error, null));
			return;
		}
		
		out.print(buildJsonResult("", "绑定成功"));
		return;
	
	}
	
	/**
	 * 手持机在保检测（当前默认在线，后期与内部系统对接验证）
	 * @param paraMap 
	 * @return
	 * @throws Exception 
	 */
	private void checkScjIsGuarantee(HttpServletRequest arg0, HttpServletResponse arg1,Map paraMap) throws Exception{
		PrintWriter out = arg1.getWriter();
		String dwdm = (String)paraMap.get("dwdm");		//单位代码
		String flashid = (String)paraMap.get("flashid");//设备ID
		
		String flag = "0";//flag  手持机在保标志  暂时默认为0：有效
		if(flag.equals("0")){
			out.print(buildJsonResult("", "在保"));
			return;
		}else{
			String error="您的设备已过保，请联系400-690-8080及时缴费!";
			out.print(buildJsonResult(error, null));
			return;
		}
	}
	
	/**
	 * 还原备份前密码校验
	 * 	入参：
	 * 		{
	 * 			dwdm：上传文件名
	 * 			sblx：设备类型
	 * 			yylx：应用类型
	 * 			dh：代号
	 * 			mm：密码
	 * 		}
	 *  返回：
	 *  	{
	 *  		"returntype": "1：失败 0：成功",
				"returnmsg": "",
			 	"returnerror": "具体的失败原因"

	 *  	}
	 * @param request
	 * @param response
	 * @param paraMap
	 * @throws IOException
	 */
	private void backup_checkpwd(HttpServletRequest request,
			HttpServletResponse response, Map paraMap) throws IOException {
		
		PrintWriter out = response.getWriter();
		String dwdm = (String) paraMap.get("dwdm");
		String sblx = (String) paraMap.get("sblx");
		String yylx = (String) paraMap.get("yylx");
		String dh = (String) paraMap.get("dh");
		String mm = (String) paraMap.get("mm");
		
		/**
		 * 1、校验参数
		 */
		String error = "";
		if (StringUtils.isEmpty(dwdm)) {
			error = "单位代码[dwdm]为空";
		}else if (StringUtils.isEmpty(sblx)) {
			error = "设备类型[sblx]为空";
		}else if (StringUtils.isEmpty(yylx)) {
			error = "应用类型[yylx]为空";
		}else if (StringUtils.isEmpty(dh)) {
			error = "代号[dh]为空";
		}else if (StringUtils.isEmpty(mm)) {
			error = "密码[mm]为空";
		}
		
		
		if(!error.equals("")){
			log.info(error);
			out.print(buildJsonResult(error, null));
			return;
		}
		
		/**
		 * 2、校验代号是否正确
		 */
		Map scjjbxxMap = scjBakManager.checkBackupPwd(dh, dwdm, sblx, yylx);
		if (scjjbxxMap == null || scjjbxxMap.size()==0) {
			//根据代号等一系列信息没有找到手持机信息、说明代号不正确
			out.print(buildJsonResult("根据提供参数未找到对应的手持机设备", null));
			return;
		}
		
		/**
		 * 3、校验密码是否正确
		 */
		if (!mm.equals(scjjbxxMap.get("mm"))) {
			out.print(buildJsonResult("密码不正确", null));
			return;
		}
		
		out.print(buildJsonResult("", ""));
		return;
	}
	
	
	public static void main(String[] args) {
		File file = new File("D:\\a");
		String tmpRootPath = "D:\\a";
		FileUtils.delAllFile(tmpRootPath);
		file.delete();
		
	}
	
}
