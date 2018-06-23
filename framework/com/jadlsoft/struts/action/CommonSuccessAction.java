package com.jadlsoft.struts.action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jadlsoft.business.AopManager;
import com.jadlsoft.dbutils.DicMapUtils;
import com.jadlsoft.struts.interceptor.CommonSuccessInterceptor;
import com.jadlsoft.utils.DomainBeanUtils;
import com.jadlsoft.utils.SystemConstants;

public class CommonSuccessAction  extends BaseAction {
	private static Map successdMap = new HashMap();
	
	private AopManager aopManager;
	
	public void setAopManager(AopManager aopManager) {
		this.aopManager = aopManager;
	}
	
	static {
		getListConfig("/successforward.xml");
	}
	public String execute() throws Exception {
		Object bean = request.getAttribute(SystemConstants.SAVELOG_BEAN);
		if (bean != null) {
			List permissList = (List)DicMapUtils.getDicMapCache().get("permiss");
			/**
			 * linkMap中存的是从t_dm_xtgn中查出的link和code
			 */
			Map linkMap = new HashMap();
	    	for(int i = 0; i < permissList.size(); i++){
	    		Map map = (Map)permissList.get(i);
	    		linkMap.put(map.get("link"), map.get("code"));
	    	}
	    	
	    	/**
	    	 * 获取请求路径
	    	 */
	    	HttpServletRequest req = (HttpServletRequest) request; 
	    	String requestURI = req.getRequestURI();
	    	String contextPath = req.getContextPath();
	    	String servletPath = requestURI.substring(contextPath.length()+1);
	    	
	    	System.out.println(servletPath+"_save.action");
	    	String link = servletPath+"_save.action";
	    	
	    	/**
	    	 * 根据请求路径取得对应的功能代码
	    	 */
	    	Object gncode = linkMap.get(link);
			
			if ((gncode != null) && (!"".equals(gncode.toString()))) {
				/*
				 * 获取成功页显示信息并储存到request对象中
				 * 当setSuccessInfo方法返回null时表示该功能在successforward.xml中没有对应配置
				 */
				Map data = setSuccessInfo(gncode.toString(), bean);
				if (data == null) {
					data = new HashMap();
					data.put("description", "未定义提示信息！");
				}else {
					/**
					 * 记录日志
					 */
					String sm = (String)data.get("description");
					aopManager.saveXtrz(getUserSession(), (String)gncode, sm);
				}
				request.setAttribute("data", data);
			}
	    } 
		return SUCCESS;
	}
	
	/**
	 * 根据request中的功能编号获取对应的成功页显示信息,并将显示信息中的变量替换掉
	 * @param gncode request中的功能编号
	 * @param bean save或update后放到request中的业务数据对象
	 * @return 成功页显示信息
	 */
	private Map setSuccessInfo(String gncode, Object bean) {
		Map data = new HashMap((Map)successdMap.get(gncode));
		// 加载该功能模块中的公共链接部分,他有该功能模块代码的前4位+"00"组成
		Map gongong =(Map)successdMap.get(gncode.substring(0, 4) + "00");
		Map resultMap = new HashMap();
		/*
		 * 如果没有当前功能编号和公共链接对应的成功页显示信息则返回null
		 */
		if ((data == null || data.size() == 0) && gongong==null) {
			return null;
		}
		if(gongong != null){
			//取该功能模块公共部分的linkurl
			List linkurl =  new ArrayList((List)gongong.get("linkurl")); 
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
				data.put("defaulturl", (String)gongong.get("defaulturl"));
			}
			//重新设置data里的linkurl
			data.put("linkurl", linkurl);
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
				value = value.replaceAll("\\Q[$" + property + "]\\E", DomainBeanUtils.getPropertyValue(bean, property).toString());
				flag = value.indexOf("[$");
			}
		}
		return value;
	}

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
								if(name.equals("description")) {
									/*
									 * 获取<description>信息
									 * <description>:页面操作成功后的提示信息
									 */
    								datamap.put("description", propitem.getTextTrim());
								}else if(name.equals("defaulturl")) {
									/*
									 * 获取<defaulturl>信息
									 * <defaulturl>:如果没有点击成功页上的任何链接,默认的跳转页面
									 */
    								datamap.put("defaulturl", propitem.getTextTrim());
								}else if(name.equals("linkurl")) {
									/*
									 * 获取多个<linkurl>并组织成List
									 * <linkurl>:操作成功后的可供选择的链接
									 */
									Map linkMap = new HashMap();
									linkMap.put("linkname", propitem.attributeValue("linkname"));
									linkMap.put("target", propitem.attributeValue("target"));
									linkMap.put("linkurl", propitem.getTextTrim());
									linkList.add(linkMap);
								}
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
