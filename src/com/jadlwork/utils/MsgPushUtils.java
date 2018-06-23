package com.jadlwork.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlwork.utils.httputils.HttpRequestProxy;

/**
 * 推送相关操作工具
 * @类名: MsgPushUtils
 * @作者: lcx
 * @时间: 2017-8-9 上午9:57:33
 */
public class MsgPushUtils {

	/**
	 * 推送email消息
	 * 	所需参数：
	 * 	请求URL(配置文件中获取)
		@param toemails 目标email（list多个）
		@param msg 模板、内容（使用自定义模板的方式，在项目中写好模板信息）
		@param atts 附件信息（如果有，使用List<String>的方式）
	 * @return: String
	 */
	public static String pushEmailMsg(List<String> toemails, String msg, List<String> atts) {
		
		
		
		return null;
	}
	
	/**
	 * 推送多条自定义消息
	 * @param msgData 参数集合，list里面是map，每个map包含  touser、title、msg信息  
	 * 			map中还有ticketKey字段，用来标志当前的一条推送信息，服务那块儿会解析该字段，如果有就返回，没有就为空 
	 * @return: String
	 * @throws Exception 
	 */
	public static String pushEmailhMultiMsg(List msgData) throws Exception {
		//1、获取远程URL
		String remoteUrl = SysConfigUtils.getProperty("service.remoteurl.email.multisinglemsg");
		//2、组装参数
		Map data = new HashMap();
		data.put("msgData", JsonUtil.list2json(msgData));
		//3、发送
		HttpRequestProxy proxy = new HttpRequestProxy();
		String res = proxy.doRequest(remoteUrl, data, null, "UTF-8");
		return res;
	}
	
	/**
	 * 根据标签进行微信的模板推送
	 * @param templateid	模板id
	 * @param varData	模板对应的所需的参数信息
	 * @return: String
	 */
	public static String pushWeChatMsgByTag(String templateid, Map varData) {
		//1、获取远程URL
		String remoteUrl = SysConfigUtils.getProperty("service.remoteurl.wechat.templateByTag");
		//2、标签id
		String tagid = SysConfigUtils.getProperty("service.wechat.tag.gzts");
		//3、微信公众号基本信息
		String appid = SysConfigUtils.getProperty("service.wechat.appid");
		String appno = SysConfigUtils.getProperty("service.wechat.appno");
		String appsecret = SysConfigUtils.getProperty("service.wechat.appsecret");
		//4、组装参数信息
		/*
		 * 接口要求参数
		 * 	appid、appno、appsecret
		 * 			String template_id 模板id（通过微信开发平台获取）
		 * 			Map varData 数据，简单处理，一个key对应一个变量，value为对应值
		 * 			tagid 标签id
		 */
		Map postData = new HashMap();
		postData.put("appid", appid);
		postData.put("appno", appno);
		postData.put("appsecret", appsecret);
		postData.put("template_id", templateid);
		postData.put("varData", JsonUtil.map2json(varData));
		postData.put("tagid", tagid);
		//5、调用工具完成推送
		String res = "";
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			res = proxy.doRequest(remoteUrl, postData, null, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static String pushWeChatMsgByUserList(String templateid, Map userList) throws Exception {
		//1、获取远程URL
		String remoteUrl = SysConfigUtils.getProperty("service.remoteurl.wechat.templateByUserList");
		//2、标签id
		//3、微信公众号基本信息
		String appid = SysConfigUtils.getProperty("service.wechat.appid");
		String appno = SysConfigUtils.getProperty("service.wechat.appno");
		String appsecret = SysConfigUtils.getProperty("service.wechat.appsecret");
		//4、组装参数信息
		/*
		 * 接口要求参数
		 * 			appid、appno、appsecret
		 * 			String template_id 模板id（通过微信开发平台获取）
		 * 			Map varData 数据，简单处理，一个key对应一个变量，value为对应值
		 * 			List<String> touseres 要推送的粉丝 的openid
		 * 			List<Map> userList 用户信息
		 */
		Map postData = new HashMap();
		postData.put("appid", appid);
		postData.put("appno", appno);
		postData.put("appsecret", appsecret);
		postData.put("template_id", templateid);
//		postData.put("varData", JsonUtil.map2json(varData));
		postData.put("touseres", JsonUtil.map2json(userList));
		//5、调用工具完成推送
		HttpRequestProxy proxy = new HttpRequestProxy();
		String res = proxy.doRequest(remoteUrl, postData, null, "UTF-8");
		return res;
	}
	
	/**
	 * 根据用户列表完成微信模板消息的发送
	 * @return: String
	 */
	public static String pushWeChatMsgByUserList(String templateid, Map varData, List userList) {
		//1、获取远程URL
		String remoteUrl = SysConfigUtils.getProperty("service.remoteurl.wechat.templateByUserList");
		//2、标签id
		//3、微信公众号基本信息
		String appid = SysConfigUtils.getProperty("service.wechat.appid");
		String appno = SysConfigUtils.getProperty("service.wechat.appno");
		String appsecret = SysConfigUtils.getProperty("service.wechat.appsecret");
		//4、组装参数信息
		/*
		 * 接口要求参数
		 * 			appid、appno、appsecret
		 * 			String template_id 模板id（通过微信开发平台获取）
		 * 			Map varData 数据，简单处理，一个key对应一个变量，value为对应值
		 * 			List<String> touseres 要推送的粉丝 的openid
		 */
		Map postData = new HashMap();
		postData.put("appid", appid);
		postData.put("appno", appno);
		postData.put("appsecret", appsecret);
		postData.put("template_id", templateid);
		postData.put("varData", JsonUtil.map2json(varData));
		postData.put("touseres", JsonUtil.list2json(userList));
		//5、调用工具完成推送
		String res = "";
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			res = proxy.doRequest(remoteUrl, postData, null, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 获取带参二维码地址
	 * @param eventKey	参数信息
	 * @return: String
	 */
	public static String getEwmWithPara(String eventKey) {
		String url = SysConfigUtils.getProperty("service.remoteurl.wechat.ewm.hqdc");
		String appid = SysConfigUtils.getProperty("service.wechat.appid");
		String appno = SysConfigUtils.getProperty("service.wechat.appno");
		String appsecret = SysConfigUtils.getProperty("service.wechat.appsecret");
		
		//组装参数信息
		/*
		 * 所需参数：
	  		基本信息：appid、appno、appsecret
	 		eventKey : 参数
	  		expireSeconds : 二维码有效期（单位/s，最长30天）
		 */
		Map postData = new HashMap();
		postData.put("appid", appid);
		postData.put("appno", appno);
		postData.put("appsecret", appsecret);
		
		postData.put("eventKey", eventKey);
		postData.put("expireSeconds", 10 * 60);
		
		String res = "";
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			res = proxy.doRequest(url, postData, null, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 向标签中批量加入用户
	 * @param tagid	标签id
	 * @param openidList	用户id集合
	 * @return: String
	 */
	public static String addOpenidToTag(String tagid, List<String> openidList) {
		
		if (openidList == null || openidList.size()==0) {
			return null;
		}
		
		String url = SysConfigUtils.getProperty("service.remoteurl.wechat.tag.adduser");
		String appid = SysConfigUtils.getProperty("service.wechat.appid");
		String appno = SysConfigUtils.getProperty("service.wechat.appno");
		String appsecret = SysConfigUtils.getProperty("service.wechat.appsecret");
		
		/*
		 * 	所需参数：
	  		基本信息：appid,appno,appsecret
	  		tagid :标签id
	  		openid_list :　用户openid集合
		 */
		Map postData = new HashMap();
		postData.put("appid", appid);
		postData.put("appno", appno);
		postData.put("appsecret", appsecret);
		
		postData.put("tagid", tagid);
		postData.put("openid_list", JsonUtil.list2json(openidList));
		
		String res = "";
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			res = proxy.doRequest(url, postData, null, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 从标签中批量移除用户
	 * @param tagid	标签id
	 * @param openidList	用户id集合
	 * @return: String
	 */
	public static String removeOpenidToTag(String tagid, List<String> openidList) {
		
		if (openidList == null || openidList.size()==0) {
			return null;
		}
		
		String url = SysConfigUtils.getProperty("service.remoteurl.wechat.tag.removeuser");
		String appid = SysConfigUtils.getProperty("service.wechat.appid");
		String appno = SysConfigUtils.getProperty("service.wechat.appno");
		String appsecret = SysConfigUtils.getProperty("service.wechat.appsecret");
		
		/*
		 * 	所需参数：
	  		基本信息：appid,appno,appsecret
	  		tagid :标签id
	  		openid_list :　用户openid集合
		 */
		Map postData = new HashMap();
		postData.put("appid", appid);
		postData.put("appno", appno);
		postData.put("appsecret", appsecret);
		
		postData.put("tagid", tagid);
		postData.put("openid_list", JsonUtil.list2json(openidList));
		
		String res = "";
		HttpRequestProxy proxy = new HttpRequestProxy();
		try {
			res = proxy.doRequest(url, postData, null, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
