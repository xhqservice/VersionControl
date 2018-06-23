package com.jadlsoft.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ListFilter implements Filter {
	
	private String suffix = "list.action";	//后缀
	private String commonlist = "/commonlist.action";	//基本列表URL
	private String realpathparamter = "realservletpath"; //实际URL参数名称

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// webSphere 下取不到req.getServletPath()
    	HttpServletRequest req = (HttpServletRequest) request; 
    	// req.getRequestURI() = /shyyqzxt/jbxx/pqdwlist.action
    	String requestURI = req.getRequestURI();
    	// req.getContextPath() = /shyyqzxt
    	String contextPath = req.getContextPath(); 
    	// servletPath = /jbxx/pqdwlist.action 
    	String servletPath = requestURI.substring(contextPath.length());
    
		if(servletPath.endsWith(suffix) && !servletPath.equals(commonlist)) {
			request.setAttribute(realpathparamter, servletPath);
			RequestDispatcher dispatch = request.getRequestDispatcher(commonlist);
			dispatch.forward(request, response);
		} 
		else { 
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		String value = config.getInitParameter("suffix");
		if(value!=null && value.trim().length()>0) {
			suffix = value.trim();
		}
		
		value = config.getInitParameter("commonlist");
		if(value!=null && value.trim().length()>0) {
			commonlist = value.trim();
		}
		
		value = config.getInitParameter("realpathparamter");
		if(value!=null && value.trim().length()>0) {
			realpathparamter = value.trim();
		}
	}
}