package com.jadlsoft.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 按照web.xml中<filter-mapping />中<filter-name>属性定位<filter-class>com.jadlsoft.filter.SetCharacterEncodingFilter
 */
public class SetCharacterEncodingFilter implements Filter {

	String defaultEncode = "UTF-8";

	String[] specialPath = null;

	String specialEncode = "UTF8";

	/**
	 * 
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		String value = filterConfig.getInitParameter("DefaultEncode");
		if (value != null && value.length() > 0) {
			this.defaultEncode = value;
		}
		value = filterConfig.getInitParameter("SpecialPath");
		if (value != null && value.length() > 0) {
			this.specialPath = value.split(",");
		}
		value = filterConfig.getInitParameter("SpecialEncode");
		if (value != null && value.length() > 0) {
			this.specialEncode = value;
		}
	}
	
	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean isseted = false;
		if (this.specialPath != null) {
			String servletPath = ((HttpServletRequest) request).getServletPath();
			for (int i = 0; i < this.specialPath.length; i++) {
				if (servletPath.startsWith(this.specialPath[i])) {
					request.setCharacterEncoding(this.specialEncode);
					isseted = true;
					break;
				}
			}
		}
		
		if (!isseted) {
			/*
			 * 张方俊 2009-12-08 修改。
			 * 问题：系统编码为GBK，但jquery使用UTF-8，导致jquery传递汉字时，服务器端解析为乱码。
			 * 解决方法：使用jquery的ajax时，使用beforeSend方法在request的头中设置一个参数：RequestType为jadlAjax。
			 * 然后在过虑器中根据该参数判断是否为jquery的ajax，如果是，则设置编码为UTF-8，否则设置为默认编码。
			 * 
			request.setCharacterEncoding(this.defaultEncode);
			*/
			if(!"UTF-8".equalsIgnoreCase(this.defaultEncode)){
				HttpServletRequest req = (HttpServletRequest) request;
				if(req.getHeader("RequestType") != null && req.getHeader("RequestType").equalsIgnoreCase("jadlAjax")){
					request.setCharacterEncoding("UTF-8");
				}else{
					request.setCharacterEncoding(this.defaultEncode);
				}
			}else{
				request.setCharacterEncoding(this.defaultEncode);
			}
		}
			
		// 传递控制到下一个过滤器
		chain.doFilter(request, response);
	}
	
	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.defaultEncode = null;
		this.specialEncode = null;
		this.specialPath = null;
	}
}
