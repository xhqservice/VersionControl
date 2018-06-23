package com.jadlwork.business.scjbak.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlwork.business.scjbak.IScjBakManager;
import com.jadlwork.model.scjbak.ScjBakBean;
import com.jadlwork.model.scjbak.ScjBakJbxxBean;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: ScjybfManager
 * @描述: 手持机云备份业务Manager实现类
 * @作者: lcx
 * @时间: 2018-2-6 上午9:30:12
 */
public class ScjBakManager extends BaseManager implements IScjBakManager {

	private Logger logger = Logger.getLogger(ScjBakManager.class);
	
	@Override
	public Map getDhByDwdmAndFlashid(String dwdm, String flashid) {
		Map condition = new HashMap();
		condition.put("dwdm", dwdm);
		condition.put("flashid", flashid);
		List list = daoUtils.find("#scjbak.getDhByDwdmAndFlashid",condition);
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (Map) list.get(0);
	}

	@Override
	public Map getScjLastBakByFlashidAndDwdm(String dwdm, String flashid) {
		Map condition = new HashMap();
		condition.put("dwdm", dwdm);
		condition.put("flashid", flashid);
		List list = daoUtils.find("#scjbak.getScjLastBakByFlashidAndDwdm", condition);
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (Map) list.get(0);
	}
	
	@Override
	public boolean checkDhOnly(String dwdm, String dh) {
		Map condition = new HashMap();
		condition.put("dwdm", dwdm);
		condition.put("dh", dh);
		List list = daoUtils.find("#scjbak.checkOnly", condition);
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int saveScjBakJbxx(ScjBakJbxxBean jbxxBean) {
		String id = String.valueOf(this.daoUtils.getNextval("q_scjbak_jbxx"));
		jbxxBean.setId(id);
		jbxxBean.setCjsj(DateUtils.createCurrentDate());
		jbxxBean.setZt(SystemConstants.ZT_TRUE);
		return this.daoUtils.save(jbxxBean);
	}
	
	@Override
	public ScjBakBean getScjBakBean(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		Object object = daoUtils.findObjectCompatibleNull(ScjBakBean.class, condition);
		return object == null ? null : (ScjBakBean)object;
	}
	
	@Override
	public int saveScjBak(ScjBakBean scjBakBean) {
		if (StringUtils.isEmpty(scjBakBean.getId())) {
			String id;
			synchronized (scjBakBean) {
				id = String.valueOf(daoUtils.getNextval("Q_SCJBAK_BAK"));
			}
			scjBakBean.setId(id);
		}
		scjBakBean.setZt(SystemConstants.ZT_TRUE);
		return daoUtils.save(scjBakBean);
	}
	
	@Override
	public int updateScjBak(ScjBakBean scjBakBean, String fields) {
		if (scjBakBean == null) {
			return 0;
		}
		return daoUtils.update(scjBakBean, fields);
	}
	
	@Override
	public int updateScjBakXgsj(String id, Date date) {
		Map condition = new HashMap();
		condition.put("id", id);
		condition.put("xgsj", date);
		return daoUtils.execSql("#scjbak.updateScjBakGxsj", condition);
	}
	
	/**
	 * 获取当前配置的FTP信息
	 * @return 包含{"ftpip": "ftpIP地址",
    				"ftpuser": "用户名",
    				"ftppwd": "密码",
    				"ftpport": "端口"
    			}
	 */
	@Override
	public Map getFtpMap() {
		List list = daoUtils.find("#scjbak.getFtpMap", null);
		if (list == null || list.size()==0) {
			return null;
		}
		return (Map) list.get(0);
	}
	
	
	@Override
	public int saveScjBakAfterUpload(ScjBakJbxxBean jbxxBean, String filename, String filesize, String sha1) {
		/**
		 * 1、保存基本信息
		 */
		ScjBakBean scjBakBean = new ScjBakBean();
		scjBakBean.setBfsj(DateUtils.createCurrentDate());
		scjBakBean.setFilename(filename);
		scjBakBean.setFilesize(filesize);
		scjBakBean.setXgsj(scjBakBean.getBfsj());
		scjBakBean.setScjid(jbxxBean.getId());
		scjBakBean.setSha1(sha1);
		
		int i = saveScjBak(scjBakBean);
		if (i>0) {
			/**
			 * 2、判断是否要移除最旧的备份信息
			 */
			
			/**
			 * 2.1、获取该单位的可备份次数
			 */
			int kbfcs = this.getBfcsByDwdm(jbxxBean.getDwdm());
			
			/**
			 * 2.2、获取手持机当前备份信息	--已经包含了当次的备份
			 */
			List scjBakList= this.getScjWithBakxx(jbxxBean.getId());
			
			if (scjBakList == null || scjBakList.size()==0) {
				logger.error("获取手持机备份信息异常");
				throw new RuntimeException("获取手持机备份信息异常！");
			}
			
			/**
			 * 2.3、校验
			 */
			if (scjBakList.size()>kbfcs) {
				for(int k=0;k<(scjBakList.size()-kbfcs);k++) {
					Map map = (Map) scjBakList.get(k);
					/**
					 * 3.1、删除多余文件信息
					 */
					String tmpfilename = (String) map.get("filename");
					try {
						File file = this.getBakFileBySavedFilename(tmpfilename);
						if (file.isFile()) {
							boolean b = file.delete();
							if (!b) {
								logger.error("删除多余备份文件失败，请手动删除");
							}
						}
					} catch (Exception e) {
						logger.error("删除多余备份文件失败，请手动删除");
					}
					
					
					/**
					 * 3.2、删除库中多余信息	--采用改变状态的方式
					 */
					Map condition = new HashMap();
					condition.put("id", map.get("bakid"));
					condition.put("zt", SystemConstants.ZT_FALSE);
					condition.put("cxsj", DateUtils.createCurrentDate());
					daoUtils.execSql("#scjbak.delscjbakByid", condition);
				}
			}
		}
		
		return i;
	}
	
	/**
	 * @param id
	 */
	private List getScjWithBakxx(String id) {
		Map condition = new HashMap();
		condition.put("id", id);
		return daoUtils.find("#scjbak.getScjWithBakxx", condition);
	}

	/**
	 * 根据单位代码和flashID获取手持机基本信息
	 * @param dwdm
	 * @param flashid
	 */
	@Override
	public ScjBakJbxxBean getScjJbxxBean(String dwdm, String flashid) {
		Map condition = new HashMap();
		condition.put("dwdm", dwdm);
		condition.put("flashid", flashid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		Object obj = daoUtils.findObjectCompatibleNull(ScjBakJbxxBean.class, condition);
		return obj == null ? null : (ScjBakJbxxBean) obj;
	}
	
	@Override
	public List getHyList(String dwdm, String sblx, String yylx) {
 		Map condition = new HashMap();
 		condition.put("dwdm", dwdm);
 		condition.put("sblx", sblx);
 		condition.put("yylx", yylx);
 		condition.put("zt", SystemConstants.ZT_TRUE);
 		return daoUtils.find("#scjbak.getHyList", condition);
	}

	/**
	 * 获取手持机的基本信息包含备份次数
	 * @param dwdm	单位代码
	 * @param flashid	flashID
	 * @return
	 */
	private Map getScjJbxxWithBakCountByScjid(String scjid) {
		Map condition = new HashMap();
		condition.put("scjid", scjid);
		List list = daoUtils.find("#scjbak.getScjJbxxWithBakCountByScjid", condition);
		return list == null || list.size()==0 ? null : (Map)list.get(0); 
	}
	
	/**
	 * 获取指定单位的可备份次数
	 * @param dwdm 单位代码
	 * @return
	 */
	private int getBfcsByDwdm(String dwdm) {
		int defaultCs = 3;
		if (StringUtils.isEmpty(dwdm)) {
			return 0;
		}
		Map condition = new HashMap();
		condition.put("dwdm", dwdm);
		List list = daoUtils.find("#scjbak.getBfcsByDwdm", condition);
		if (list == null || list.size()==0) {
			//没有就返回默认3次
			return defaultCs;
		}
		
		Map map = (Map) list.get(0);
		Object cs = map.get("bfcs");
		if (cs == null) {
			return defaultCs;
		}
		try {
			return Integer.parseInt(cs.toString());
		} catch (Exception e) {
			return defaultCs;
		}
	}
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FTP文件存储工具 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	
	/**
	 * 获取FTP根目录
	 * @return
	 */
	private File getFtpRootDir() {
		Map ftpMap = this.getFtpMap();
		if (ftpMap == null || ftpMap.size()==0) {
			throw new RuntimeException("系统未设置FTP信息!");
		}
		
		File ftpRootDir = new File((String) ftpMap.get("rootdir"));
		if (!ftpRootDir.isDirectory()) {
			ftpRootDir.mkdirs();
		}
		
		return ftpRootDir;
	}
	
	@Override
	public File getFtpTmpFile(String filename) {
		File ftpRootDir = getFtpRootDir();
		File destFile = new File(ftpRootDir, filename);
		return destFile;
	}
	
	@Override
	public File getBakFileDir(String dwdm, String flashid) {
		File ftpRootDir = getFtpRootDir();
		String destDirStr = ftpRootDir.getAbsolutePath() + File.separator + "scjbak" + File.separator + dwdm + File.separator + flashid;
		File destDir = new File(destDirStr);
		if (!destDir.isDirectory()) {
			destDir.mkdirs();
		}
		return destDir;
	}
	
	@Override
	public Map checkBackupPwd(String dh, String dwdm, String sblx, String yylx) {
		Map condition = new HashMap();
		condition.put("dh", dh);
		condition.put("dwdm", dwdm);
		condition.put("sblx", sblx);
		condition.put("yylx", yylx);
		condition.put("zt", SystemConstants.ZT_TRUE);
		
		List list = daoUtils.find(ScjBakJbxxBean.class, condition);
		if (list == null || list.size()==0) {
			return null;
		}
		return (Map) list.get(0);
	}
	
	
	/**
	 * 根据库中保存的文件名（ftp根目录后面的路径）获取文件
	 * @param filename
	 * @return
	 */
	private File getBakFileBySavedFilename(String filename) {
		File ftpRootDir = getFtpRootDir();
		String destStr = ftpRootDir.getAbsolutePath() + File.separator + filename;
		File destFile = new File(destStr);
		return destFile;
	}
	
}
