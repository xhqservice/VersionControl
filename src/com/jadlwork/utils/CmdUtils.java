package com.jadlwork.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 进行调用系统cmd命令完成功能的工具类
 * @ClassName: CmdUtils
 * @Description: 调用系统的cmd命令，如ping命令
 * @author: 李春晓
 * @date: 2016-12-22 下午04:38:47
 */
public class CmdUtils {

	// 日志
	private static Logger log = Logger.getLogger(CmdUtils.class);
	
	final static int maxCount = 3; //IP最大扫描次数
	final static int minCount = 1; //IP最小扫描次数
	final static int sleepTime = 10000; //初始化程序睡眠时间
	
	/**
	 * 对外提供的测试是否ping通的工具
	 * @Title: isPing 
	 * @param ip 要测试的ip
	 * @return: boolean 
	 */
	public static boolean isPing(String ip,int pingCount){
	
		Integer doPingCmd = doPingCmd(ip, pingCount);
		if (doPingCmd != null && doPingCmd >0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 对外提供的测试是否ping通的工具
	 * @Title: isPing 
	 * @param ip 要测试的ip
	 * @return: boolean 
	 */
	public static boolean isPing(String ip){
	
		Integer doPingCmd = doPingCmd(ip, maxCount);
		if (doPingCmd != null && doPingCmd >0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 替换Nginx配置文件
	 * @param nginxConf 本地的配置文件对象
	 * @param nginxConfPath Nginx主机中Nginx配置文件的路径
	 * @return: boolean
	 */
	public static boolean replaceNginxConf(File nginxConf, String nginxConfPath){
		
		return true;
	}
	
	/**
	 * 执行ping操作
	 * @参数：@param destIp
	 * @参数：@param maxCount
	 * @参数：@return
	 * @返回值：Integer
	 */
	private static Integer doPingCmd(String destIp, int maxCount) {
		LineNumberReader input = null;
		try {
			String osName = System.getProperty("os.name");
			String pingCmd = null;
			if (osName.startsWith("Windows")) {
				pingCmd = "cmd /c ping -n {0} {1}";
				pingCmd = MessageFormat.format(pingCmd, maxCount, destIp);
			} else if (osName.startsWith("Linux")) {
				pingCmd = "ping -c {0} {1}";
				pingCmd = MessageFormat.format(pingCmd, maxCount, destIp);
			} else {
				log.info("not support OS");
				return null;
			}
			Process process = Runtime.getRuntime().exec(pingCmd);
			InputStreamReader ir = new InputStreamReader(process
					.getInputStream());
			input = new LineNumberReader(ir);
			String line;
			List<String> reponse = new ArrayList<String>();

			while ((line = input.readLine()) != null) {
				if (!"".equals(line)) {
					reponse.add(line);
					// System.out.println("====:" + line);  
				}
			}
			if (osName.startsWith("Windows")) {
				return parseWindowsMsg(reponse, maxCount);
			} else if (osName.startsWith("Linux")) {
				return parseLinuxMsg(reponse, maxCount);
			}

		} catch (IOException e) {
			log.error("IOException   " + e.getMessage());
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException ex) {
					log.error("close error:" + ex.getMessage());
					
				}
			}
		}
		return null;
	}
	
	/**
	 * 处理windows系统返回信息
	 * @参数：@param reponse
	 * @参数：@param total
	 * @参数：@return
	 * @返回值：int
	 */
	private static int parseWindowsMsg(List<String> reponse, int total) {
		int countTrue = 0;
		int countFalse = 0;
		for (String str : reponse) {
			
			/*
			 * 为了高效的完成，设置常见的成功和失败的情况，确定成功就continue，确定失败就直接return
			 */
			
			// 常见可以确定为成功的，直接continue，进行下一次的判断
			if ((str.startsWith("来自") && str.contains("TTL")) || (str.startsWith("Reply from") && str.contains("TTL"))) {
				countTrue++;
				continue;
			}
			
			// 常见的ping失败，说明失败，直接返回，成功的条数为0
			if (str.startsWith("请求超时") || str.startsWith("Request timed out")) {
				countFalse++;
				return countTrue;
			}
			
			// 进行之后的判断
			if (str.contains("无法访问") || str.contains("Destination host Unreachable")) {
				countFalse++;
				return countTrue;
			}
			
			// TODO 继续添加其他的可能情况
			
		}
		return countTrue;
	}

	/**
	 * 处理linux系统返回信息
	 * @参数：@param reponse
	 * @参数：@param total
	 * @参数：@return
	 * @返回值：int
	 */
	private static int parseLinuxMsg(List<String> reponse, int total) {
		int countTrue = 0;
		for (String str : reponse) {
			if (str.contains("bytes from") && str.contains("icmp_seq=")) {
				countTrue++;
			}
		}
		return countTrue;
	}
}
