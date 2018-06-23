package com.jadlsoft.utils;

public interface IConstants {
	public static final String SUCCESS_KEY = "Success";

	public static final String SYSTEM_FAILURE_KEY = "SystemFailure";

	public static final String SESSION_TIME_OUT_KEY = "SessionTimeOut";

	public static final String FAILURE_KEY = "Failure";

	public static final String LOGIN_KEY = "Login";

	public static final String GET_KEY = "Get";

	public static final String CONTINUE_KEY = "Continue";

	public static final String RESULT_KEY = "Result";

	public static final String FILL_KEY = "Fill";

	public static final String EQUIP_KEY = "Equip";

	public static final String QUERY_KEY = "Query";

	public static final String SAVELIST_KEY = "Savelist"; // 列表保存

	public static final String SYSMESSAGE_KEY = "systemmessage"; // 返回消息

	public static final String GNCODE_KEY = "gncode"; // 功能代码

	public static final String EFFECTIVE = "0"; // 正常,有效的

	public static final String INVALID = "1"; // 注销的

	public static final String NEEDVIEW = "0"; // 需要提醒用户查看

	public static final String NOTNEEDVIEW = "1"; // 不需要提醒用户查看

	/*
	 * 枪支证件状态
	 */
	public static final String SQ = "11"; // 申请

	/*
	 * 行政区划级别
	 */
	public static final int MINISTRY = 0;// 公安部

	public static final int PROVINCE = 1;// 省级

	public static final int CITY = 2;// 地市

	public static final int COUNTRY = 3;// 县级

	public static final int ERRORLEVEL = -1;// 县级

	public static final String ERRORXZHQH = "999999";// 产生错误时候的代码

	public static final String SUPERIORXZHQH = "888888";// 公安部的上级代码

	public static final String MINISTRYXZHQH = "000000";// 公安部的代码

	// 页码设置
	public static final int PAGESIZE = 10;

	// 消息设置
	public static final String MESSAGE_KEY = "ApplicationMessage";

	// 系统升级时保存信息的xml文件名称
	public static final String XTSJ_XML_FILENAME = "updateconfig.xml";
	// 系统升级保存目录
	public static final String XTSJ_MULU = "xtsj";
	// 保存通知信息的xml文件名称
	public static final String TZXX_XML_FILENAME = "downtzconfig.xml";
	// 字典信息保存目录
	public static final String ZDXX_MULU = "zdgl";
	// 保存字典信息的xml文件名称
	public static final String ZDXX_XML_FILENAME = "downcodeconfig.xml";
	// 保存全部字典信息的xml文件名称
	public static final String ALL_ZDXX_FILENAME = "downallcodeconfig.xml";
	// 通知信息保存目录
	public static final String TZXX_MULU = "tzgl";
	// 旅馆必填项配置的xml文件名称
	public static final String LGBTX_XML_FILENAME = "downlgbtxconfig.xml";
	// 保存旅馆必填项的目录名称
	public static final String LGBTX_MULU = "lgbtx";
	// 通知，字典更新，升级维护，旅馆必填项最后的更新时间文件
	public static final String UPDATE_XML_FILENAME = "JALYDownConfig.xml";
	
	public static final String YHLX_QY = "1";
	public static final String YHLX_GA = "2";
	public static final String YHLX_GX = "3";
	
	public static final int YHBM_LEVEL_1 = 1;//一级（集团）

	public static final int YHBM_LEVEL_2 = 2;//二级

	public static final int YHBM_LEVEL_3 = 3;//三级

	public static final int YHBM_LEVEL_4 = 4;//四级
	
	public static final int YHBM_LEVEL_5 = 5;//五级
}
