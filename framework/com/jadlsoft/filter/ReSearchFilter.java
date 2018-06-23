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

public class ReSearchFilter implements Filter {
	
	private String suffix = "research.action";
	
	public void init(FilterConfig config) throws ServletException {
		String value = config.getInitParameter("suffix");
		if (value != null && value.trim().length() > 0) {
			suffix = value.trim();
		}
	}
	
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String servletPath = ((HttpServletRequest) request).getServletPath();
		if (servletPath.endsWith(suffix)) {
			request.setAttribute("queryparamtername", request.getParameter("queryparamtername"));
			RequestDispatcher dispatch = request.getRequestDispatcher(servletPath.substring(0, servletPath.indexOf("research.action")) + ".action");
			dispatch.forward(request, response);
		} else {
			// 传递控制到下一个过滤器
			chain.doFilter(request, response);
		}
	}
}
