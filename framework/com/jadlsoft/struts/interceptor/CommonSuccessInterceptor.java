/**
 * @Description 公用成功页面拦截器,执行完增、改操作后该拦截器将页面定向到公共成功页面
 * @Company 京安丹灵
 * @author zongshuai
 * @date 2012-03-06
 * @version 1.0
 */

package com.jadlsoft.struts.interceptor;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jadlsoft.dbutils.DaoUtils;
import com.jadlsoft.utils.DomainBeanUtils;
import com.jadlsoft.utils.SystemConstants;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

public class CommonSuccessInterceptor extends AbstractInterceptor{
	private DaoUtils daoUtils;
	
	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}
	
	private static Map successdMap = new HashMap();
	static {
		getListConfig("/successforward.xml");
	}
	public String intercept(ActionInvocation invocation) throws Exception {

		invocation.addPreResultListener(new PreResultListener() {
            public void beforeResult(ActionInvocation invocation, String resultCode) {
        		HttpServletRequest request = ServletActionContext.getRequest();
        		Object bean = request.getAttribute(SystemConstants.SAVELOG_BEAN);
//        		String method = invocation.getProxy().getMethod();

        		if (bean != null) {
        	    	/*
        	    	 * 获取请求路径
        	    	 */
        	    	String contextPath = request.getContextPath();
        	    	String servletPath = request.getRequestURI().substring(contextPath.length() + 1).replaceAll("!", "_");
        	    	int actionPos = servletPath.indexOf(".action");
        	    	if (actionPos != -1) {
        	    		servletPath = servletPath.substring(0, actionPos);
        	    	}
        	    		
        	    	/*
        	    	 * 根据请求路径取得对应的功能代码
        	    	 */
    				Map data = setSuccessInfo(servletPath, bean);
    				if (data == null) {
    					data = new HashMap();
    					data.put("description", "未定义提示信息！");
    				}else {
    					/**
    					 * 记录日志
    					 *//*
    					String sm = (String)data.get("description");
        				UserBean user = (UserBean)request.getSession(false).getAttribute("user");
        				
    					String sql = "insert into t_wh_xtrz values( :userid, :username, :czdate, :qgdw, :gncode, :sm)";
    					Map condition = new HashMap();
						condition.put("userid", user.getUserid());
						condition.put("username", user.getUsername());
						condition.put("czdate", DateUtils.createCurrentDate());
						condition.put("qgdw", user.getQgdw());
						condition.put("gncode", data.get("gncode"));
						condition.put("sm", sm);
    					daoUtils.execSql(sql, condition);*/
    				}
    				request.setAttribute("data", data);
        	    } 
            }});
		String result = invocation.invoke();
		return result;
	}
	
	/**
	 * 根据功能URI获取对应的成功页显示信息,并将显示信息中的变量替换掉
	 * @param servletPath 功能URI
	 * @param bean save或update后放到request中的业务数据对象
	 * @return 成功页显示信息
	 */
	private Map setSuccessInfo(String servletPath, Object bean) {
		Map data = new HashMap((Map)successdMap.get(servletPath));
		//加载该功能模块中的公共链接部分
		Map p_date = null;
		Map resultMap = new HashMap();
		
		if (servletPath.indexOf("_") != -1) {
			p_date =(Map)successdMap.get(servletPath.substring(0, servletPath.indexOf("_")));
			/*
			 * 如果没有当前功能URI和公共链接对应的成功页显示信息则返回null
			 */
			if(p_date != null){
				//取该功能模块公共部分的linkurl
				List linkurl =  new ArrayList((List)p_date.get("linkurl")); 
				//取该功能模块独有的linkurl
				List linkurlself = (List)data.get("linkurl");
				Iterator ir = linkurlself.iterator();
				//该循环将独有的linkurl拼接到公共模块上去
				while(ir.hasNext()){
					Map map = (Map)ir.next();
					linkurl.add(map);
				}
				//设置defaulturl
				if(data.get("defaulturl")==null){
					data.put("defaulturl", (String)p_date.get("defaulturl"));
				}
				//重新设置data里的linkurl
				data.put("linkurl", linkurl);
			}
		}

		if ((data == null || data.size() == 0) && p_date==null) {
			return null;
		}
		Set keys = data.keySet();
		Iterator item = keys.iterator();
		while(item.hasNext()) {
			Object key = item.next();
			Object value = data.get(key);
			resultMap.put(key, value);
			/*
			 * 替换<description>、<defaulturl>中的变量
			 */
			if (value instanceof String){
				resultMap.put(key, transform(value, bean));
			}
			/*
			 * 循环替换<linkurl>中的变量
			 */
			if (value instanceof List){
				List dataList = (ArrayList)resultMap.get(key);
				List list = new ArrayList();
				for (int i = 0; i < dataList.size(); i++) {
					//先在i这个位置上取出并存放到listMap中
					Map listMap = (Map)dataList.get(i);
					Map linkMap = new HashMap();
					linkMap.put("linkname", listMap.get("linkname"));
					linkMap.put("target", listMap.get("target"));
					linkMap.put("linkurl", transform(listMap.get("linkurl"), bean));
					list.add(linkMap);
				}
				resultMap.put(key, list);
			}
		}
		return resultMap;
	}

	/**
	 * 将链接中的字符串变量替换成Bean中的实际值
	 * @param objvalue 含有字符串变量的链接,如../dwgl/mqpzqy.do?method=get&qy_id=[$qy_id]
	 * @param bean 从中获取变量值的对象
	 * @return 替换掉变量的、可以点击的链接
	 */
	private String transform(Object objvalue, Object bean) {
		String value = objvalue.toString();
		if(bean != null) {
			int flag = value.indexOf("[$"); 
			while(flag >= 0){
				String property = value.substring(flag + 2, value.indexOf("]"));
				value = value.replaceAll("\\Q[$" + property + "]\\E", (DomainBeanUtils.getPropertyValue(bean, property))+"");
				flag = value.indexOf("[$");
			}
		}
		return value;
	}

	/**
	 * 加载成功页配置文件
	 * @param fileName 配置文件
	 */
	private static void getListConfig(String fileName) {
        try {
            URL url = CommonSuccessInterceptor.class.getResource(fileName);
            File file = new File(url.getFile().replaceAll("%20", " "));
    		if(file.exists() && file.isFile() && file.canRead()) {
    			SAXReader reader = new SAXReader();
    			try {
    				Document doc = reader.read(file);
    				Element root = doc.getRootElement();
    				Iterator item = root.elementIterator();
    				while(item.hasNext()) {
    					Element column = (Element) item.next();
    					String name = column.getName();
						if(name.equals("successforward")) {
							Map datamap = new HashMap();
							List linkList = new ArrayList();
							/*
							 * id:功能编号,并以他作为successdMap的key
							 */
							String id = column.attributeValue("id");
							datamap.put("id", id);
							Iterator prop = column.elementIterator();
							while(prop.hasNext()) {
								Element propitem = (Element) prop.next();
								name = propitem.getName();
								if (name.equals("linkurl")) {
									/*
									 * 获取多个<linkurl>并组织成List
									 * <linkurl>:操作成功后的可供选择的链接
									 */
									Map linkMap = new HashMap();
									linkMap.put("linkname", propitem.attributeValue("linkname"));
									linkMap.put("target", propitem.attributeValue("target"));
									linkMap.put("linkurl", propitem.getTextTrim());
									linkList.add(linkMap);
								} else {
									//获取<description> <defaulturl> <gncode>信息
									datamap.put(name, propitem.getTextTrim());
								}
								/*
								if(name.equals("description")) {
    								datamap.put("description", propitem.getTextTrim());
								}else if(name.equals("defaulturl")) {
    								datamap.put("defaulturl", propitem.getTextTrim());
								}else if(name.equals("linkurl")) {
									Map linkMap = new HashMap();
									linkMap.put("linkname", propitem.attributeValue("linkname"));
									linkMap.put("target", propitem.attributeValue("target"));
									linkMap.put("linkurl", propitem.getTextTrim());
									linkList.add(linkMap);
								}
								*/
							}
							datamap.put("linkurl", linkList);
							successdMap.put(id, datamap);
						}
    				}
    			} catch (DocumentException e) {
    			}
			}
        } catch (Throwable t) {
        } 
	}
}
