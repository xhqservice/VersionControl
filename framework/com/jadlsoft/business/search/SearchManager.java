/**
 * <p>Title:SearchManager.java</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 京安丹灵<p>
 * @date Nov 4, 2009
 * @author zhouxl
 * @version 3.0
 */
package com.jadlsoft.business.search;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.utils.JsonUtils;


public class SearchManager extends BaseManager{
	private Logger logger = Logger.getLogger(SearchManager.class);
	
	
	/**
	 * 返回符合条件的记录条数
	 * @param sql
	 * @return
	 */
	public int getSearchCount(String sql){
		return daoUtils.queryForInt("select count(*) from (" + sql + ")");
	}
	
	/**
	 * 返回指定跳过行数、返回条数的list
	 * @param sql
	 * @param skip
	 * @param count
	 * @return
	 */
	public List getSearchList(String sql,int skip,int count){
		return daoUtils.find(sql, new HashMap(), skip, count);
	}
	
	/**
	 * 根据sql返回list
	 * @param sql
	 * @return
	 */
	public List getSearchList(String sql){
		return daoUtils.find(sql, Collections.EMPTY_MAP);
	}
	
	/**
	 * @功能：获取记录数
	 * @参数：
	 * @param sql
	 * @param url
	 * @return
	 * @返回值：int
	 * create by zhaohuibin 2016-4-24 下午01:34:49
	 */
	public int getSearchCountByWebservice(String sql, String url) {
		Service  service = new Service();
		Call call = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
			call.setTimeout(new Integer(60 * 1000));// 超时设定1分钟抛出异常
			call.setOperationName("getSearchCountByWebservice");// 调用方法
			call.addParameter("sql", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
			call.setReturnType(XMLType.XSD_STRING);// 指定返回类型
			Object jsonString = call.invoke(new Object[]{sql});// 调用服务并返回存在的对应数据
			return Integer.parseInt(((String)jsonString));
		} catch (Exception e) {
			logger.error("调用webservice出错！url：" + url , e);
			//网络不通或webservice服务有问题返回-1
			return -1;
		}	
	}
	/**
	 * @功能：获取指定页列表
	 * @参数：
	 * @param sql
	 * @param skip
	 * @param pagesize
	 * @param url
	 * @return
	 * @返回值：List
	 * create by zhaohuibin 2016-4-24 下午01:35:07
	 */
	public List getSearchListByWebservice(String sql, int skip, int pagesize, String url){
		Service  service = new Service();
		Call call = null;
		List list = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
			call.setTimeout(new Integer(60 * 1000));// 超时设定1分钟抛出异常
			call.setOperationName("getSearchListByWebservice");// 调用方法
			call.addParameter("sql", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
			call.addParameter("skip", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
			call.addParameter("pagesize", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
			call.setReturnType(XMLType.XSD_STRING);// 指定返回类型
			Object jsonString = call.invoke(new Object[]{sql,skip+"",pagesize+""});// 调用服务并返回存在的对应数据
			list = JsonUtils.getList4Json((String)jsonString);
			return list;
		} catch (Exception e) {
			logger.error("调用webservice出错！url：" + url , e);
			//网络不通或webservice服务有问题返回-1
			return null;
		}
	}
	/**
	 * @功能：获取列表
	 * @参数：
	 * @param sql
	 * @param url
	 * @return
	 * @返回值：List
	 * create by zhaohuibin 2016-4-24 下午01:36:25
	 */
	public List getSearchListByWebservice(String sql, String url) {
		Service  service = new Service();
		Call call = null;
		List list = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);
			call.setTimeout(new Integer(60 * 1000));// 超时设定1分钟抛出异常
			call.setOperationName("getSearchListByWebservice");// 调用方法
			call.addParameter("sql", XMLType.XSD_STRING, ParameterMode.IN);// 增加参数
			call.setReturnType(XMLType.XSD_STRING);// 指定返回类型
			Object jsonString = call.invoke(new Object[]{sql});// 调用服务并返回存在的对应数据
			list = JsonUtils.getList4Json((String)jsonString);
			return list;
		} catch (Exception e) {
			logger.error("调用webservice出错！url：" + url , e);
			//网络不通或webservice服务有问题返回-1
			return null;
		}
	}	
}
