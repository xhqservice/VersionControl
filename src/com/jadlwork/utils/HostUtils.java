package com.jadlwork.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

/**
 * 
 * 主机工具类
 * @author wujiaxu
 * @Time 2017-6-22 下午1:07:19
 *
 */
public class HostUtils {
	
	private static Logger logger = Logger.getLogger(HostUtils.class);

	public static String host_Ip = "localhost";
    public static int host_Port = 8080;
    static{
    	setHostPort();
    	setHostIp();
    }
	/**
	 * 获取本机tomcat端口
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-22 下午1:20:38
	 */
    private static void setHostPort() {
    	MBeanServer mBeanServer = null;  
        if (MBeanServerFactory.findMBeanServer(null).size() > 0)  {  
            mBeanServer = (MBeanServer)MBeanServerFactory.findMBeanServer(null).get(0);  
        }  
        
        try {
	        Set names  = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);  
	        if (names != null && names.size() > 0) {
	    		for (Object objectName : names) {
	    			String protocol = (String) mBeanServer.getAttribute((ObjectName)objectName, "protocol");
	    			
	    	    	if (protocol.equalsIgnoreCase("HTTP/1.1")) {
	    	    		host_Port = (Integer) mBeanServer.getAttribute((ObjectName)objectName, "port");
		    		}
	    		}
	    	}
        } catch (Exception e) {
        	logger.error("获取本机tomcat端口出错!", e);
		}
	}
    
    /**
     * 
     * 设置本机IP地址
     * @author wujiaxu
     * @Time 2017-6-19 下午2:22:24
     */
	private static void setHostIp() {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			host_Ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			logger.error("设置本机IP出错!", e);
		}
	}
	   
}
