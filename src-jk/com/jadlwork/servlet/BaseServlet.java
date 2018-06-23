package com.jadlwork.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

class BaseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public static final String error_sys = "系统错误";
	public static final String error_param = "参数有误";
	public static final int  re_success = 0;//成功状态码
	public static final int  re_error = 1;//失败状态码
	public static final String column_returntype = "returntype";//字段——返回类型
	public static final String column_returnmsg = "returnmsg";//字段——返回内容
	public static final String column_returnerror = "returnerror";//字段——错误信息
	
	/**
	 * 检测接口是否合法
	 * @参数：@param paraMap
	 * @参数：@param string
	 * @参数：@return
	 * @返回值：boolean
	 */
	protected boolean IsValidInterface(Map paraMap, String qqlx) {
		if(paraMap.containsKey("qqlx") && paraMap.get("qqlx").equals(qqlx)){
			return true;
		}
		return false;
	}
	
	
	protected String buildJsonResult(Object error,Object msg) {
		Map map = new HashMap();
		if(error != null && !error.equals("")){
			map.put(column_returntype, String.valueOf(re_error));
			map.put(column_returnmsg, "");
			map.put(column_returnerror, error);
		}else{
			map.put(column_returntype, String.valueOf(re_success));
			map.put(column_returnmsg, msg);
			map.put(column_returnerror, "");
		}

		JSONObject jsObj = JSONObject.fromObject(map);
		return jsObj.toString();
	}

	/**
	 * 检查调用接口的参数是否正确
	 * @param para
	 * @return
	 */
	protected String checkPara(String para) {
		if(para == null || para.equals("")){
			return "参数为空！";
		}
		return "";
	}

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		this.doPost(arg0, arg1);
	}
}
