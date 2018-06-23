package com.jadlwork.business.scjbak;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jadlwork.model.scjbak.ScjBakBean;
import com.jadlwork.model.scjbak.ScjBakJbxxBean;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: IScjybfManager
 * @描述: 手持机云备份业务接口
 * @作者: lcx
 * @时间: 2018-2-6 上午9:29:56
 */
public interface IScjBakManager {
	
	/**
	 * 根据dwdm,flashid获取代号
	 * @param dwdm,flashid
	 * @return
	 * @author JWT
	 * @Time 2018-2-6 上午11:40:23
	 */
	Map getDhByDwdmAndFlashid(String dwdm,String flashid);
	
	/**
	 * 同一单位代号唯一验证
	 * @param dwdm,dh
	 * @return
	 * @author JWT
	 * @Time 2018-2-6 上午13:46:11
	 */
	boolean checkDhOnly(String dwdm,String dh);

	/**
	 * 保存手持机代号绑定信息
	 * @param dwdm,password
	 * @return
	 * @author JWT
	 * @Time 2018-2-6 上午13:54:35
	 */
	int saveScjBakJbxx(ScjBakJbxxBean jbxxBean);
	
	/**
	 * 根据flashID和单位代码获取最新一次的备份信息
	 * @param dwdm 单位代码
	 * @param flashid
	 * @return
	 */
	Map getScjLastBakByFlashidAndDwdm(String dwdm, String flashid);
	
	/**
	 * 根据id获取备份bean
	 * @param id
	 * @return
	 */
	ScjBakBean getScjBakBean(String id);
	
	/**
	 * 保存手持机备份bean
	 * @param scjBakBean
	 * @return
	 */
	int saveScjBak(ScjBakBean scjBakBean);
	
	/**
	 * 根据单位代码和flashID获取手持机备份基本信息
	 * @param dwdm
	 * @param flashid
	 * @return
	 */
	ScjBakJbxxBean getScjJbxxBean(String dwdm, String flashid);
	
	/**
	 * 更新手持机备份信息
	 * @param scjBakBean 
	 * @param fields 待更新字段
	 * @return
	 */
	int updateScjBak(ScjBakBean scjBakBean, String fields);
	
	/**
	 * 更新备份信息的修改时间
	 * @param id 备份的id
	 * @param date
	 * @return
	 */
	int updateScjBakXgsj(String id, Date date);
	
	/**
	 * 获取FTP信息
	 * @return
	 */
	Map getFtpMap();

	/**
	 * 客户端上传完并且服务器移动完成后创建新的备份数据
	 * @param jbxxBean	手持机备份基本信息
	 * @param filename 上传文件名
	 * @param filesize 上传文件大小
	 * @param sha1 
	 */
	int saveScjBakAfterUpload(ScjBakJbxxBean jbxxBean, String filename, String filesize, String sha1);

	/**
	 * 获取所有可还原备份信息
	 * @param dwdm	单位代码
	 * @param sblx	设备类型
	 * @param yylx	应用类型
	 * @return
	 */
	List getHyList(String dwdm, String sblx, String yylx);
	
	/**
	 * 获取FTP根目录中的临时文件
	 * @param filename
	 * @return
	 */
	File getFtpTmpFile(String filename);
	
	/**
	 * 获取正式保存的根目录
	 * @param dwdm
	 * @param flashid
	 * @return
	 */
	File getBakFileDir(String dwdm, String flashid);

	/**
	 * 获取  还原时候校验密码所需基本信息
	 * @param dh	代号
	 * @param dwdm	单位代码
	 * @param sblx	设备类型
	 * @param yylx	应用类型
	 * @return
	 */
	Map checkBackupPwd(String dh, String dwdm, String sblx, String yylx);
	
}
