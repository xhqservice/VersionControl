

package com.jadlsoft.ajaxaction;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ContextLoaderPlugIn;

import com.jadlsoft.dbutils.BlobUtils;

public class Dispatch extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(Dispatch.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = (String) request.getParameter("action");
		if (action == null || action.length() == 0) {
			returnSimpleErrorResult("", response);
		} else {
			if (action.equalsIgnoreCase("getdic")) {
				getDicData(request, response);
			} else if (action.equalsIgnoreCase("getdata")) {
				getDataContent(request, response);
			} else if (action.equalsIgnoreCase("checkdataexist")) {
				checkDataExist(request, response);
			} else if(action.equalsIgnoreCase("checkdataequalfromsession")){
				checkDataEqualFromSession(request, response);
			}
		}
	}

	private void getDicData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String dicname = (String) request.getParameter("dicname");
			String dm = (String) request.getParameter("dm");
			String value = (String) request.getParameter("value");
			String cond = ((String) request.getParameter("cond"));
			List dic = (List) ((Map) this.getServletContext().getAttribute("dic")).get(dicname);
			String result = DicManager.getDicContent(dic, dm, value, cond);
			returnResult(response, result);
		} catch (Exception e) {
			returnErrorResult(response, e);
		}
	}

	
	private void returnResult(HttpServletResponse response, String result) throws IOException {
		response.setContentType("text/xml");
		OutputStream out = response.getOutputStream();
		out.write(result.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	
	private void returnSimpleErrorResult(String errinfo, HttpServletResponse response) throws IOException {
		log.error("AJAX错误" + errinfo);
		String result = null;
		if (errinfo == null || errinfo.length() == 0) {
			result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>1</status><content></content></result>";
		} else {
			result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>1</status><content>" + errinfo + "</content></result>";
		}
		returnResult(response, result);
	}

	private void returnErrorResult(HttpServletResponse response, Exception e) throws IOException {
		log.error("AJAX错误！", e);
		String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>1</status><content></content></result>";
		returnResult(response, result);
	}


	private void checkDataExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>0</status><content></content></result>";
			String dataname = (String) request.getParameter("dataname");
			String cond = ((String) request.getParameter("cond"));
			//cond=java.net.URLDecoder.decode(cond, "GBK");
			cond=java.net.URLDecoder.decode(cond, "UTF-8");
			//cond = new String(cond.getBytes("ISO-8859-1"),"UTF-8");
			result = DataloadManager.checkExistContent(getSpringDatasource(), dataname, cond);
			returnResult(response, result);
		} catch (Exception e) {
			returnErrorResult(response, e);
		}
	}


	private void getDataContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><result><status>0</status><content></content></result>";
			String dataname = (String) request.getParameter("dataname");
			String cols = (String) request.getParameter("cols");
			String cond = ((String) request.getParameter("cond"));
			String blobflag = (String) request.getParameter("blobflag");
			result = DataloadManager.getDataContent(getSpringDatasource(), (BlobUtils) getSpringBean("blobUtils"), dataname, cols, cond, (blobflag != null && blobflag.equals("1") ? true : false));
			returnResult(response, result);
		} catch (Exception e) {
			returnErrorResult(response, e);
		}
	}


	private DataSource getSpringDatasource() {
		return (DataSource) getSpringBean("dataSource");
	}

	private Object getSpringBean(String beanid) {
		WebApplicationContext wac = (WebApplicationContext) this.getServletContext().getAttribute(ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX);
		if (wac == null) {
			log.info("AJAX Servlet!");
			return null;
		}
		return wac.getBean(beanid);
	}
	

	private void checkDataEqualFromSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			String parameterName = (String)request.getParameter("parameterName");
			String parameterValue = (String)request.getParameter("parameterValue");
			String parameterValueFromSession = request.getSession().getAttribute(parameterName).toString();
			String result = "";
			if(parameterValue.equals(parameterValueFromSession)){
				result = "TRUE";
			}else{
				result = "FALSE"; 
			}
			returnResult(response, result);
		}catch(Exception e)
		{
			returnErrorResult(response, e);
		}
	}
}