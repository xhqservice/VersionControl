/**
 * <p>Title:AjaxAction.java </p>
 * <p>Description: 页面脚本与后台服务器交互Action</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 京安丹灵</p>
 * @date 2011-12-29
 * @author ZongShuai
 * @version 1.0
*/

package com.jadlsoft.struts.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jadlsoft.business.AjaxManager;

public class AjaxAction extends BaseAction {
	private Object result;//JSON返回值,必须要有get、set方法
	private AjaxManager ajaxManager;
	public void setAjaxManager(AjaxManager ajaxManager) {
		this.ajaxManager = ajaxManager;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * 从数据库中查询并返回指定列
	 * @return 查询结果List的JSON表达方式
	 */
	public String returnDBData() {
		String dataname = (String) request.getParameter("dataname");
		String cols = (String) request.getParameter("cols");
		String cond = ((String) request.getParameter("cond")).replaceAll("[$]xzqh", "");
		if(cond!=null)
		cond=cond.replace('|', '#');
		result = ajaxManager.getDataContent(dataname, cols, cond);
		return SUCCESS;
	}

	/**
	 * 从代码表缓存中查询并返回指定列的代码表内容
	 * @return 查询结果List的JSON表达方式
	 */
	public String returnDicData() throws IOException{
		String dicname = (String)request.getParameter("dicname");
		String diccode = (String)request.getParameter("diccode");
		String dictext = (String)request.getParameter("dictext");
		String cond = ((String) request.getParameter("cond")).replaceAll("[$]xzqh", "");
		if(cond!=null)
			cond=cond.replace('|', '#');
		List dic = (List)((Map)application.get("dic")).get(dicname);
		result = ajaxManager.getDicContent(dic, diccode, dictext, cond);
		return SUCCESS;
	}
	
	/**
	 * 从数据库中查询指定条件的数据是否存在
	 * @return 0:不存在;3:存在;其他:异常 JSON表达方式
	 */
	public String checkDataExist() throws IOException{
		String dataname = (String) request.getParameter("dataname");
		String expr =  java.net.URLDecoder.decode(request.getParameter("cond") , "UTF-8");
			
		String cond = expr.replaceAll("[$]xzqh","");
		if(cond!=null)
			cond=cond.replace('|', '#');
		result = ajaxManager.checkExistContent(dataname, cond);
		return SUCCESS;
	}
}
