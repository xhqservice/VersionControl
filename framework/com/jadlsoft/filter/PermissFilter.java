package com.jadlsoft.filter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jadlsoft.model.xtgl.BaseUserSession;
import com.jadlsoft.struts.action.UserUtils;

/**
 * Example filter that sets the character encoding to be used in parsing the
 * incoming request
 */
public class PermissFilter implements Filter {
	//--------------properties-----------------------
	private String[] anonymousPath = null;
	private String loginurl ;
	private String permissurl ;
	private String permissconfig = "/WEB-INF/permissconfig.xml";
	
	//--------------local variants-------------------
	private Document permissConf;
	private Map hmUrls = new HashMap(); 
	private static Logger log = Logger.getLogger(PermissFilter.class);
	
	/**
     * Take this filter out of service.
     */
    public void destroy() {
    	this.anonymousPath = null;
    	this.loginurl = null;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response,
    		FilterChain chain)throws IOException, ServletException {
    	 
    	String servletPath = ((HttpServletRequest)request).getServletPath();
    	String contextpath = ((HttpServletRequest)request).getContextPath();
    	if(servletPath==null || servletPath.length()<=1) {
    		chain.doFilter(request, response);
            return;
    	} else {
	    	if(this.anonymousPath!=null) {
		    	for(int i=0;i<this.anonymousPath.length;i++)
			    	if (servletPath.startsWith(this.anonymousPath[i]))
			        {
			    		chain.doFilter(request, response);
			            return;
			        }
	    	}
    	}
    	HttpSession session = ((HttpServletRequest)request).getSession(false);
		if(session==null || session.getAttribute(UserUtils.USER_SESSION)==null) {
			((HttpServletResponse)response).sendRedirect(contextpath + loginurl);
			return;
		}
    	
    	//以下进行权限控制
		if(!isAccessable((HttpServletRequest)request, (BaseUserSession)session.getAttribute(UserUtils.USER_SESSION))) {
			((HttpServletResponse)response).sendRedirect(contextpath + permissurl);
			return;
		} else {
			request.setAttribute("isforward", "true");
		}
	
	    // 传递控制到下一个过滤器
	    chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        String value = filterConfig.getInitParameter("anonymousPath");
        if(value!=null && value.length()>0) {
        	this.anonymousPath = value.split(",");
        }
        value = filterConfig.getInitParameter("loginurl");
        if(value!=null && value.length()>0) {
        	this.loginurl = value;
        }
        value = filterConfig.getInitParameter("permissurl");
        if(value!=null && value.length()>0) {
        	this.permissurl = value;
        }
        value = filterConfig.getInitParameter("permissconfig");
        
        if(value!=null && value.length()>0) {
        	this.permissconfig = value;
        }
        this.permissconfig = filterConfig.getServletContext().getRealPath(this.permissconfig);
        //权限配置初始化
        initPermissConfig(this.permissconfig);
      
    }
    
    private void initPermissConfig(String configfile) {
    	SAXReader reader = new SAXReader();//读xml文件包
		FileInputStream in = null;//文件输入流
		try {
            //获得文件输入流
			in = new FileInputStream(configfile);
            //将流读入doc中
			permissConf = reader.read(in);
			//获得根元素
			Element eRoot = permissConf.getRootElement();
			//得到子元素列表
			List ls = eRoot.elements();
			//临时元素存放
			Element eTemp = null;
			//循环获取元素
			for (int n = 0; n < ls.size(); n++) {
				//存放第n个元素
				eTemp = (Element) ls.get(n);
				//将元素存放进hashmap中
				hmUrls.put(eTemp.attributeValue("url"), eTemp);
			}
			//日志信息提示
			log.info("系统权限参数初始化完毕！");

		} catch (Exception ex1) {
			//日志信息提示
			log.error("系统权限参数初始化错误！",ex1);
		} finally {
			if (in != null) {//判断流是否已经关闭
				try {//异常处理
					in.close();//关闭
				} catch (IOException e) {
                    //错误信息提示				
					log.info(e.toString());
				}
			}
		}
    }
    
    /**
     * Description: 是否可访问判断
     * @param request
     * @param user 登录用户
     * @return 可访问返回true
     * 2006-10-19
     */
    private boolean isAccessable(HttpServletRequest request, BaseUserSession user) {
    	//获取URL
    	//String servletPath = request.getServletPath();
    	
    	//判断是否forward
    	String isForward = (String) request.getAttribute("isforward"); 
    	if(isForward!=null && isForward.length()>0) {
    		return true;
    	}
    	
    	String contextpath = ((HttpServletRequest)request).getContextPath();
    	
    	String requestUri = ((HttpServletRequest)request).getRequestURI();
    	String servletPath = requestUri.substring(contextpath.length());
    	
    	//没有配置不允许访问
    	if(!hmUrls.containsKey(servletPath)) {
    		String method = request.getParameter("method");
    		String type = request.getParameter("type");
        	if(method!=null && method.length()>0) {
        		if(!hmUrls.containsKey(servletPath + "?method=" + method)) {
        			if(type!=null && type.length()>0 && !hmUrls.containsKey(servletPath + "?type=" + type)) {
        				log.info("URL没有配置：" + servletPath);
            			return false;
        			} else {
        				servletPath += "?type=" + type;
        			}
        		} else {
        			servletPath += "?method=" + method;
        		}
        	} else {
        		if(type!=null && type.length()>0) {
        			servletPath += "?type=" + type;
            		if(!hmUrls.containsKey(servletPath)) {
            			log.info("URL没有配置：" + servletPath);
            			return false;
            		}
        		} else {
        			log.info("URL没有配置：" + servletPath);
        			return false;
        		}
        	}
    	}
    	
    	Element etemp = (Element) hmUrls.get(servletPath);
    	//判断是否有功能权限
    	String gncode = etemp.attributeValue("gncode");
    	if(gncode!=null && gncode.length()>0) {
    		if(!user.getRole().getPermisses().contains(gncode)) {
    			log.info("URL没有权限访问，功能编号：" + gncode + ", URL: " + servletPath);
    			return false;
    		}
    		request.setAttribute("gncode", gncode);	//设置功能编号，用于日志记录
    	}
    	return true;
    }
    
   
}
