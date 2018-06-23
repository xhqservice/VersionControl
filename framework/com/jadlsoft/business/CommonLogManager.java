package com.jadlsoft.business;

import java.util.Date;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.business.LogUtils;

public class CommonLogManager extends BaseManager{
	private static Logger log = Logger.getLogger(LogUtils.class);
	/**
	 * @功能 记录操作日志类
	 * @参数  记录IP地址、操作时间、操作人、操作内容、请求URL
	 * @作者 zhangsanjie add 2016-7-12 下午1:35:43
	 * @返回值类型 void
	 */
    public void saveOperateLog(String fwid, String userId,String ip,String Url
			,String czsm,Date czsj){
    	try{
    		this.logUtils.saveOperationLog(fwid,userId,ip,Url,czsm,czsj);
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error("记录操作日志失败！", e);
    	}
    	
    }
}
