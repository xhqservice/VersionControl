package com.jadlsoft.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title:XzhqhUtils.java
 * </p>
 * <p>
 * Description: 行政区划处理类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 京安丹灵
 * </p>
 * 
 * @date 2006-10-12
 * @author Zong
 * @version 1.0
 */

public class XzhqhUtils {
	public static Map XZQHMAP = new HashMap();
	static{
		XZQHMAP.put("468200", "468200");
		XZQHMAP.put("468300", "468300");
		XZQHMAP.put("468400", "468400");
		XZQHMAP.put("468500", "468500");
		XZQHMAP.put("468600", "468600");
		XZQHMAP.put("468700", "468700");
		XZQHMAP.put("468800", "468800");
		XZQHMAP.put("468900", "468900");
		XZQHMAP.put("469100", "469100");
		XZQHMAP.put("469200", "469200");
		XZQHMAP.put("469300", "469300");
		XZQHMAP.put("469400", "469400");
		XZQHMAP.put("469500", "469500");
		XZQHMAP.put("469600", "469600");
		XZQHMAP.put("469700", "469700");
		XZQHMAP.put("469800", "469800");
		XZQHMAP.put("469900", "469900");
		XZQHMAP.put("441900", "441900");
		XZQHMAP.put("442000", "442000");
		XZQHMAP.put("410881", "410881");
		/**============区县级公安机关要求具备实际行政职能======================
		 * 2013-12-18 接公安部传真后加入
		 */
		//吉林
		XZQHMAP.put("220381", "220381");
		XZQHMAP.put("220581", "220581");
		//河北
		XZQHMAP.put("130181", "130181");
		XZQHMAP.put("130682", "130682");
		//湖北
		XZQHMAP.put("429004", "429004");
		XZQHMAP.put("429005", "429005");
		XZQHMAP.put("429006", "429006");
		XZQHMAP.put("429021", "429021");
		XZQHMAP.put("429099", "429099");
		//福建
		XZQHMAP.put("350128", "350128");
		//安徽
		XZQHMAP.put("340826", "340826");
		XZQHMAP.put("341822", "341822");
		//河南
		XZQHMAP.put("419900", "419900");
		
		/**============市级公安机关要求具备区县级行政职能======================
		 * 2013-12-18 接公安部传真后加入
		 */
		//吉林
		XZQHMAP.put("220300", "220300");
		XZQHMAP.put("220400", "220400");
		XZQHMAP.put("220600", "220600");
		//河北
		XZQHMAP.put("139800", "139800");
		XZQHMAP.put("139900", "139900");
		//甘肃
		XZQHMAP.put("620200", "620200");
		
		//========================================//
		XZQHMAP.put("320500", "320500");
		XZQHMAP.put("320600", "320600");
		XZQHMAP.put("320900", "320900");
		XZQHMAP.put("321000", "321000");
		XZQHMAP.put("321200", "321200");
		XZQHMAP.put("321300", "321300");
	}
	/**
	 * Method getLevel
	 * 
	 * @param xzqh:行政区划
	 * @description:获取行政区划级别
	 * @return
	 */
	public static int getXZHQHLevel(String xzqh) {
		if ((xzqh == null) || "".equals(xzqh) || (xzqh.trim().length() != 6)) {// 传入空参数或参数位数不匹配
			return IConstants.ERRORLEVEL;
		}
		if ("000000".equals(xzqh)) {
			return IConstants.MINISTRY; // 公安部级
		} else if (xzqh.substring(2, 6).equals("0000")) {
			return IConstants.PROVINCE; // 省级
		} else if (xzqh.substring(4, 6).equals("00")) {
			return IConstants.CITY; // 地市级
		} else {
			return IConstants.COUNTRY; // 县级
		}
	}

	public static int getXZHQHLevel_Old(String xzqh) {
		if ((xzqh != null) && (xzqh.trim().length() != 6)) {// 传入空参数或参数位数不匹配
			return IConstants.ERRORLEVEL;
		}
		if ("000000".equals(xzqh)) {
			return IConstants.MINISTRY; // 公安部级
		} else if (xzqh.substring(2, 6).equals("0000")) {
			return IConstants.PROVINCE; // 省级
		} else if (xzqh.substring(4, 6).equals("00")) {
			return IConstants.CITY; // 地市级
		} else if ("11,12,31,50".indexOf(xzqh.substring(0, 2)) != -1) {
			return IConstants.CITY; // 直辖市的县级按照地市级处理
		} else {
			return IConstants.COUNTRY; // 县级
		}
	}

	/**
	 * Method getXZQH
	 * 
	 * @param xzqh:行政区划代码
	 * @description:根据传入行政区划的代码,返回模糊查询所用的行政区划.该结果用于 like %% 查询中,查询结果中包括自身及其下属单位
	 * @return
	 */
	public static String getXZHQH(String xzqh) {
		if ((xzqh == null) || (xzqh.trim().length() != 6)) {
			return "999999";// 出错,返回一个没有的行政区划代码
		} else {
			xzqh = xzqh.trim();
		}
		if (xzqh.equals("000000")) {// 公安部的行政区划
			return "";
		} else if (xzqh.substring(2, 6).equals("0000")) {// 省级的行政区划
			return xzqh.substring(0, 2);
		} else if (xzqh.substring(4, 6).equals("00")) {// 市级的行政区划
			return xzqh.substring(0, 4);
		} else {// 县级的行政区划
			return xzqh;
		}
	}
	/**
	 * @功能 返回行政区划的前两位
	 * @参数 @param xzqh
	 * @参数 @return
	 * @作者 zhangsanjie add 2017-3-2 下午1:47:47
	 * @返回值类型 String
	 */
   public static String getXzqhFrontTwo(String xzqh){
	   if ((xzqh == null) || (xzqh.trim().length() != 6)) {
			return "99";// 出错,返回一个没有的行政区划代码
		} else {
			xzqh = xzqh.trim();
		}
		if (xzqh.equals("000000")) {// 公安部的行政区划
			return "";
		} else {
			return xzqh.substring(0, 2);
		}
   }
	/**
	 * Method getFatherXZHQH
	 * 
	 * @param xzqh:行政区划代码
	 * @description:根据传入行政区划的代码,获取其上级行政区划代码
	 * @return
	 */
	public static String getFatherXZHQH(String xzqh) {
		if ((xzqh == null) || (xzqh.trim().length() != 6)) {
			return IConstants.ERRORXZHQH;// 出错,返回一个没有的行政区划代码
		} else {
			xzqh = xzqh.trim();
		}
		final String especialXZQH = "11,12,31,50,71,81,82";
		String xzqhLeft2 = xzqh.substring(0, 2);
		String xzqhRight4 = xzqh.substring(2, 6);
		xzqh = xzqh.trim();
		if ((especialXZQH.indexOf(xzqhLeft2) >= 0)
				&& (!xzqhRight4.equals("0000"))) {
			return (xzqhLeft2 + "0000");
		}
		if (xzqh.equals("000000")) {// 公安部的行政区划
			return IConstants.SUPERIORXZHQH;
		} else if (xzqh.substring(2, 6).equals("0000")) {// 省级的行政区划
			return "000000";
		} else if (xzqh.substring(4, 6).equals("00")) {// 市级的行政区划
			return xzqh.substring(0, 2) + "0000";
		} else {// 县级的行政区划
			return xzqh.substring(0, 4) + "00";
		}
	}

	/**
	 * Method getFatherXZHQH
	 * 
	 * @param xzqh:行政区划代码
	 * @param level:要求生成上级代码的级别
	 * @description:根据传入行政区划的代码,获取其上级行政区划代码
	 * @return
	 */
	public static String getFatherXZHQH(String xzqh, int level) {
		if (level == IConstants.MINISTRY) {
			return "000000";
		} else if (level == IConstants.PROVINCE) {
			return xzqh.substring(0, 2) + "0000";
		} else if (level == IConstants.CITY) {
			return xzqh.substring(0, 4) + "00";
		} else if (level == IConstants.COUNTRY) {
			return xzqh;
		} else {
			return xzqh;
		}
	}
	
	
	/**
	 * @功能：根据userid解析用户级别
	 * @参数：
	 * @param userid 用户编码
	 * @return
	 * @返回值：String
	 * create by zhaohuibin 2015-12-7 上午11:40:32
	 */
	public static String getShortYHBM(String userid) {
		if(userid!=null && !"".equals(userid)){
			if(userid.length()>=17){
				if (userid.substring(7, 17).equals("0000000000")) {
					return userid.substring(0, 7);
				} else if (userid.substring(9, 17).equals("00000000")) {
					return userid.substring(0, 9);
				} else if (userid.substring(11, 17).equals("000000")) {
					return userid.substring(0, 11);
				} else if (userid.substring(13, 17).equals("0000")) {
					return userid.substring(0, 13);
				} else if (userid.substring(15, 17).equals("00")) {
					return userid.substring(0, 15);
				} else {
					return userid.substring(0, 17);
				}
			}
		}
	   return null;
	}
	
	/**
	 * @功能：根据userid解析用户级别
	 * @参数：
	 * @param userid 用户编码
	 * @return
	 * @返回值：int
	 * create by zhaohuibin 2015-12-7 上午11:38:25
	 */
	public static int getYHBMLevel(String userid) {
		if ((userid != null) && (userid.trim().length() != 20)) {// 传入空参数或参数位数不匹配
			return IConstants.ERRORLEVEL;
		}
		if (userid.substring(7, 17).equals("0000000000")) {
			return IConstants.YHBM_LEVEL_1;
		} else if (userid.substring(9, 17).equals("00000000")) {
			return IConstants.YHBM_LEVEL_2;
		} else if (userid.substring(11, 17).equals("000000")) {
			return IConstants.YHBM_LEVEL_3;
		} else if (userid.substring(13, 17).equals("0000")) {
			return IConstants.YHBM_LEVEL_4;
		} else if (userid.substring(15, 17).equals("00")) {
			return IConstants.YHBM_LEVEL_5;
		}
		return IConstants.ERRORLEVEL;
	}
	/**
	 * @功能：判断当前行政区划是否为省级行政区划
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2012-6-17 下午03:01:49
	 */
	public static boolean isProvince(String xzqh) {
		return "0000".equals(xzqh.substring(2,6));
	}
	/**
	 * @功能：判断当前行政区划是否为地市级行政区划
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2012-6-17 下午03:02:09
	 */
	public static boolean isCity(String xzqh) {
		if(isZxs(xzqh)){
			return ("0000".equals(xzqh.substring(2,6))) || (!"00".equals(xzqh.substring(4,6))) || isShengZxx(xzqh);
		}else{
			return ("00".equals(xzqh.substring(4,6)) && !"00".equals(xzqh.substring(2,4)));
		}
	}
	/**
	 * @功能：判断当前行政区划是否为区县级行政区划
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2012-6-17 下午03:02:18
	 */
	public static boolean isCountry(String xzqh) {
		return !"00".equals(xzqh.substring(4,6)) || isShengZxx(xzqh);
	}
	
	/**
	 * @功能：判断是否为直辖市
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2013-1-8 下午02:10:24
	 */
	public static boolean isZxs(String xzqh){
		return ("11").equals(xzqh.substring(0,2)) || ("12".equals(xzqh.substring(0,2))) || ("31".equals(xzqh.substring(0,2))) || ("50".equals(xzqh.substring(0,2))) || isShengZxx(xzqh);
	}
	
	/**
	 * @功能：判断是否为重庆或上海
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2013-4-10 上午09:14:50
	 */
	public static boolean isChongqingOrShanghai(String xzqh){
		return "31".equals(xzqh.substring(0,2)) || "50".equals(xzqh.substring(0,2));
	}
	
	/**
	 * @功能：判断是否为省直辖县
	 * @参数：
	 * @param xzqh
	 * @return
	 * @返回值：boolean
	 * create by zhaohuibin 2013-7-18 下午05:07:31
	 */
	public static boolean isShengZxx(String xzqh){
		return XZQHMAP.containsKey(xzqh) || ("4290".equals(xzqh.substring(0, 4)) && !"00".equals(xzqh.substring(4))) || "420103".equals(xzqh);
	}
	public static void main(String[] arg){
		System.out.println(XzhqhUtils.getShortYHBM("MB000010000000000"));
		System.out.println(XzhqhUtils.getShortYHBM("MB000010100000000"));
		System.out.println(XzhqhUtils.getYHBMLevel("MB000010100000000001"));
	}
	
}
