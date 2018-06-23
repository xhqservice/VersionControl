package com.jadlwork.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.SpringBeanFactory;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlwork.business.qrgl.IQRCodeManager;
import com.jadlwork.business.qrgl.impl.QRCodeManager;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: ShortUrlServlet
 * @描述: 短URL临时实现
 * @作者: lcx
 * @时间: 2018-1-24 上午9:19:56
 */
public class ShortUrlServlet extends HttpServlet {

	private static final long serialVersionUID = 7927973754986032748L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * 短的URL，当前只是处理应用商店二维码的问题
		 */
		String servletPath = request.getServletPath();
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		if (uri.contains(ctx+servletPath)) {
			int i = uri.indexOf(ctx+servletPath);
			i += (ctx+servletPath).length();
			
			if (i == uri.length()) {
				try {
					ResponseUtils.render(response, "错误的地址！");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				String key = uri.substring(i);
				key = key.indexOf("/")>-1 ? key.substring(1) : key;
				
				/**
				 * 查询对应的url地址
				 */
				String srcurl = getSrcUrlByShortUrlKey(request, key);
				if (StringUtils.isEmpty(srcurl)) {
					try {
						ResponseUtils.render(response, "错误的地址！");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				/**
				 * 跳转
				 */
				response.sendRedirect(srcurl);
			}
		}else {
			try {
				ResponseUtils.render(response, "系统出错！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getSrcUrlByShortUrlKey(HttpServletRequest request, String key) {
		IQRCodeManager qrCodeManager = (IQRCodeManager) SpringBeanFactory.getBean("qrCodeManager");
		Map map = qrCodeManager.getQRCodeMapperByShortUrl(key);
		if (map == null || map.get("srcurl") == null || map.get("srcurl").equals("")) {
			return null;
		}
		
		String s = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf(request.getRequestURI()));
		String srcurl = s + map.get("srcurl");
		return srcurl;
	}
	
}
