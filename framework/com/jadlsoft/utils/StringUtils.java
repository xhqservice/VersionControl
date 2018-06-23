package com.jadlsoft.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: StringUtil.java
 * </p>
 * <p>
 * Description: 公用String函数
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 京安丹灵
 * </p>
 * 
 * @author 李帮贵
 * @version 1.0
 */

public class StringUtils {

	public static String filteNull(String str) {
		return (str == null || str.equalsIgnoreCase("null")) ? "" : str;
	}

	/**
	 * Description: 字符串转换成整数
	 * s
	 * @param strvalue
	 * @param defaultNum
	 * @return
	 */
	public static int convertIntDef(String strvalue, int defaultNum) {
		if (strvalue != null && !strvalue.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(strvalue);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static int getXzqhLevel(String xzqh) {
		// 空字符串
		if (xzqh == null || xzqh.length() == 0)
			return 0;
		// 非2的整数倍返回-1
		if (xzqh.length() % 2 > 0)
			return -1;

		String level = xzqh;
		while (level.endsWith("00")) {
			level = level.substring(0, level.length() - 2);
		}

		return level.length() / 2;
	}

	/**
	 * 补零
	 * 
	 * @param srcValue 原值
	 * @param length 补零后的长度
	 * @return
	 */
	public static String fillZero(int srcValue, int length) {
		String str = Integer.toString(srcValue);
		if (str.length() == length) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length - str.length(); i++) {
			sb.append("0");
		}
		return sb.toString() + str;
	}

	/**
	 * 李洪磊 2008-07-03 13:13 去掉换行符
	 * 
	 * @param _value 原值
	 * @return 去掉换行符后的值
	 */
	public static String base64BlankFilter(String _value) {
		if (_value == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < _value.length(); i++) {
			int _c = _value.charAt(i);
			if (_c != 10 && _c != 13) {
				sb.append(_value.substring(i, i + 1));
			}
		}
		return sb.toString();
	}

	/**
	 * 判断是否为指定长度
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static final boolean isLen(String str, int len) {
		if (isEmpty(str))
			return false;
		else if (str.length() == len)
			return true;
		else
			return false;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 李洪磊 add 2008-07-30 17:44 生成xml信息并传入response
	 * @param response
	 * @param text
	 * @param sign
	 */
	public static void createAjaxReturnXml(HttpServletResponse response, String[] text, String[] sign) {
		createAjaxReturnXml(response, text, sign, "");
	}

	/**
	 * 李洪磊 add 2008-07-30 17:44 生成xml信息并传入response
	 * @param response
	 * @param text
	 * @param sign
	 * @param oldCode
	 */
	public static void createAjaxReturnXml(HttpServletResponse response, String[] text, String[] sign, String oldCode) {
		StringBuffer retrunString = new StringBuffer("<jadl>");
		for (int i = 0; i < text.length; i++) {
			addXMLChild(retrunString, sign[i], text[i], true);
		}
		if (oldCode == null) {
			oldCode = "";
		}
		retrunString.append(oldCode);
		retrunString.append("</jadl>");
		PrintWriter out = null;
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(out != null){
			out.print(retrunString.toString().trim());
			retrunString = null;
			out.close();
		}
	}

	/**
	 * 李洪磊 add 2008-07-30 17:44 将字符串传入response
	 * 
	 * @param response
	 * @param text
	 */
	public void createAjaxReturnMsg(HttpServletResponse response, String text) {
		PrintWriter out = null;
		response.setContentType("text/xml;charset=utf-8");
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(out != null){
			out.print(text);
			out.close();
		 }
	}

	/**
	 * 处理字符串中的特殊字符，目前只处理“'”
	 * 
	 * @param str
	 * @return
	 */
	public static String processSpecialChar(String str) {
		return str.replaceAll("'", "''");
	}

	/**
	 * 去除数组对象重复记录
	 * 
	 * @param objArray
	 * @return
	 */
	public static Object[] wipeOffRepeat(Object[] obj) {
		List list = new ArrayList();
		for (int i = 0; i < obj.length; i++) {
			list.add(obj[i]);
		}
		Set set = new HashSet(list);
		return set.toArray();
	}
	
	/**
	 * Description: 操作成功页面
	 * @param request
	 * @param msg：给用户的提示信息
	 * @param listMs：给用户操作的显示信息
	 * @param listUrl：给用户操作的显示信息的连接
	 * @param listTarget：给用户操作的显示信息连接位置(_self:在本页打开, _blank:在新页中打开)
	 */
	public static void success(HttpServletRequest request, String msg, List listMs, List listUrl, List listTarget) {
		List list = new ArrayList();
		for (int i=0; i<listMs.size(); i++) {
			Map map = new HashMap();
			map.put("ms", listMs.get(i));
			map.put("url", listUrl.get(i));
			map.put("target", listTarget.get(i));
			list.add(map);
		}
		request.getSession().setAttribute("successPageMsUrl", list);
		request.getSession().setAttribute("successPageMsg", msg);
	}
	
//	public static void main(String[] args) {
//		Object[] aa = (Object[]) StringUtils.wipeOffRepeat(new String[] {"a", "b", "c","a"});
//		for(int i=0; i<aa.length; i++) {
//			System.out.println(aa[i]);
//		}
//	}
	
	/**
	 * 判断是否是直辖市 11 北京 12 天津 31 上海 50 重庆
	 * @param xzqh
	 * @return
	 */
	public static boolean isZXS(String xzqh){
		if(xzqh.startsWith("11") || xzqh.startsWith("12") || xzqh.startsWith("31") || xzqh.startsWith("50")){
			return true;
		}else{
			return false;
		}
	} 
	/**
	 * 字符串排序(若字符串相联则显示String1至String2,如:01,02,03,04,05,显示为01至05) 
	 * @param list
	 * @return
	 */
	public static String compositorStr (String list){	
		String[] lists_tmp = list.split(",");
		String[] lists = new String[lists_tmp.length + 2];
		lists[0] = "-2";
		lists[lists.length - 1] = "-2";
		for (int i = 0; i < lists_tmp.length; i++) {
			lists[i + 1] = lists_tmp[i];
		}

		StringBuffer ls = new StringBuffer();
		String splitChar = "，";

		for (int p = 1; p < lists.length - 1; p++) {
			int pre = Integer.parseInt(lists[p - 1]);
			int after = Integer.parseInt(lists[p + 1]);
			int current = Integer.parseInt(lists[p]);
			if (current == (after - 1) && current == (pre + 1)) {
				continue;
			} else if (current == (after - 1)) {
				ls.append(intTo2String(current) + "~");
			} else if (current == (pre + 1)) {
				ls.append(intTo2String(current)).append(splitChar);
			} else {
				ls.append(intTo2String(current)).append(splitChar);
			}
		}

		return ls.toString();
	}
	
	private static String intTo2String(int t){
		String s = t + "";
		return s.length() <= 1 ? "0" + s : s;
	}
	
	
	/**
	 * 张方俊 2007-01-17 10:35
	 * 向xml中添加节点。如果有特殊字符，将采用<![CDATA[]]>方式添加
	 * @param sb 组合的字符串
	 * @param childName 节点的名称
	 * @param childValue 节点的值
	 * @param hasSpecialChar 是否需要添加<![CDATA[]]>处理
	 */
	public static void addXMLChild(StringBuffer sb, String childName , String childValue , boolean hasSpecialChar){
		sb.append("<");
		sb.append(childName);
		sb.append(">");
		if(hasSpecialChar){
			sb.append("<![CDATA[");
		}
		sb.append(childValue);
		if(hasSpecialChar){
			sb.append("]]>");
		}
		sb.append("</");
		sb.append(childName);
		sb.append(">");
	}
	/**
	 * 填充空串
	 * 
	 * @param str
	 * @return
	 */
	public static final String fillNull(String str) {
		return str==null ? "" : str;
	}
	
	/**
	  * 产生指定位数的数字随机数
	  * @param numLenth	指定的位数
	  * @return: String
	  */
	 public static String createRandomNumber(int numLenth){
		if (numLenth<=0) {
			return "";
		}
		Random rd = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < numLenth; i++) {
			sb.append(rd.nextInt(10));
		}
		return sb.toString();
	 }
	 
	 /**
	  * 生成唯一的UUID，去除中间的横杠-
	  * @return
	  */
	 public static String getRandomUUID() {
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString().replaceAll("-", "");
	 }
	 
	 /**
	  * 生成指定长度的随机字符串
	  * @param @param length
	  * @return String
	  * @author chenkai
	  * @Time Aug 28, 2017 4:21:00 PM
	 */
	 public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//含有字符和数字的字符串
        Random random = new Random();//随机类初始化
        StringBuffer sb = new StringBuffer();//StringBuffer类生成，为了拼接字符串
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
	 
}
