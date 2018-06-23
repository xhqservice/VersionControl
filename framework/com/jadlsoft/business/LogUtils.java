package com.jadlsoft.business;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcOperations;

import com.jadlsoft.dbutils.DaoUtils;

/**
 * @author sky 功能：
 * 
 */
public class LogUtils {
	public static Logger log = Logger.getLogger(LogUtils.class);

	private DaoUtils daoUtils;

	private JdbcOperations getJdbcTemplate() {
		return this.daoUtils.getJdbcTemplate();
	}

	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}

	/**
	 * addOperationLog() 功能：添加操作日志
	 * @param gaglkbh 当前登录的公安管理卡卡编号
	 * @param gncode 所操作的功能模块的代码
	 * @param czsm 操作说明，明确表达用户做的是什么操作 void
	 * 
	 * 张方俊 与 2009年11月05日修改：为避免在manager层中捕获异常，将此处抛出异常的代码屏蔽。
	 */
	public void addOperationLog(String gaglkbh, String gnCode, String czsm){
		try {			
			if (gaglkbh != null && !"".equals(gaglkbh)) {
				String sql = "INSERT INTO t_wh_xtrz(xh,sj,kbh,gnbh,zj) "
					+ "VALUES(q_wh_xtrz.nextval,?,?,?,?)";
				Object[] values = new Object[4];
				values[0] = new Timestamp(System.currentTimeMillis());
				values[1] = gaglkbh; 
				values[2] = gnCode; 
				values[3] = czsm;				
				getJdbcTemplate().update(sql , values);
			}
		} catch (Exception e) {
			log.error("添加操作日志失败！", e);
		}
	}
	/**
	 * @功能 记录系统日志
	 * @参数 @param fwid
	 * @参数 @param userId
	 * @参数 @param ip
	 * @参数 @param Url
	 * @参数 @param czsm
	 * @参数 @param czsj
	 * @作者 zhangsanjie add 2016-7-12 下午2:13:04
	 * @返回值类型 void
	 */
	public void saveOperationLog(String fwid, String userId,String ip,String Url
			,String czsm,Date czsj){
		try {			
			if (userId != null) {
				String sql = "INSERT INTO t_wh_xtrz(xh,fwid,userid,ip,url,czsm,czsj) "
					+ "VALUES(q_wh_xtrz.nextval,?,?,?,?,?,?)";
				Object[] values = new Object[6];
				values[0] = fwid;
				values[1] = userId; 
				values[2] = ip; 
				values[3] = Url;	
				values[4] = czsm;
				values[5] = czsj;
				getJdbcTemplate().update(sql , values);}
		} catch (Exception e) {
			log.error("添加操作日志失败！", e);
		}
	}
}
