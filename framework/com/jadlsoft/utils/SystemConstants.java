package com.jadlsoft.utils;

import java.util.HashMap;
import java.util.Map;

public class SystemConstants {
	
	/**
	 * 系统换行符
	 */
	public static final String LINE_SEPARATER = System.getProperty("line.separator", "\n");
	
	/**
	 * 默认主键序列表
	 */
	public static final String DB_PK_SEQUENCE = "q_table_pkid";
	
	public static final String DB_TABLEPKFIELDS = "db_tablepkfields";
	/**
	 * 表名
	 */
	public static final String DB_TABLENAME = "db_tablename";
	/**
	 * 排序字段，可以加上desc等
	 */
	public static final String DB_ORDERBY = "db_resultorderby";
	/**
	 * 条件字段
	 */
	public static final String DB_CONDITIONFIELDS = "condition_fields";
	/**
	 * Blob字段
	 */
	public static final String DB_BLOBFIELD = "db_blobfields";
	/**
	 * TIMESTAMP字段
	 */
	public static final String DB_TIMESTAMPFIELD = "qztimestamp";
	
	public static final String BLOBFIELDS = "ry_zp,ry_qm,zp";
	
	
	public static final String SUCCESS_KEY = "Success";

	public static final String SYSTEM_FAILURE_KEY = "SystemFailure";

	public static final String SESSION_TIME_OUT_KEY = "SessionTimeOut";

	public static final String FAILURE_KEY = "Failure";

	public static final String LOGIN_KEY = "Login";

	public static final String GET_KEY = "Get";

	public static final String CONTINUE_KEY = "Continue";

	public static final String RESULT_KEY = "Result";
	
	public static final String VIEW_KEY = "View";

	public static final String FILL_KEY = "Fill";

	public static final String EQUIP_KEY = "Equip";

	public static final String QUERY_KEY = "Query";

	public static final String SYSMESSAGE_KEY = "systemmessage"; //返回消息

	public static final String GNCODE_KEY = "gncode"; 	//功能代码

	public static final String XXZT_ZC = "0"; 	//正常,有效的

	public static final String XXZT_ZX = "1"; 	//注销的

	public static final String SAVELOG_BEAN = "log_bean";

	public static final int PAGESIZE = 10;	//页码设置
	
	public static final String MESSAGE_KEY = "ApplicationMessage";	//消息设置
	
	public static final String SAVELIST_KEY = "Savelist";	//列表保存
	public static final String COMMOMLIST_KEY = "Commonlist";	//列表公共页
	
	/**
	 * 主机检测返回状态码
	 */
	public static String STATUSCODE_OK = "0000";	//成功
	public static String STATUSCODE_FALSE = "1111";	//失败
	public static String STATUSCODE_FALSE_IPEXIST = "1001";		//ip已存在
	public static String STATUSCODE_FALSE_PINGERROR = "1011";	//ping不通
	public static String STATUSCODE_FALSE_SOCKETERROR = "1012";	//socket通信失败
	public static String STATUSCODE_FALSE_UNEXPECT = "1000";	//未知失败类型

	/**
	 * 数据状态 0:无效  1:有效
	 */
	public static final String ZT_FALSE = "1"; 
	public static final String ZT_TRUE = "0"; 

	/**
	 * 图标文件名模板
	 */
	public static final String APPICONNAME = "{@appid}Icon.{@suffix}";
	
	/**
	 * 应用默认原始版本号
	 */
	public static final String YYVERSION_DEFAULT = "1.0";
	

	
	
	/**
	 * APP类型  0:android  1:IOS
	 */
	public static final String APPTYPE_ANDROID = "0"; 
	public static final String APPTYPE_IOS = "1"; 
	
	/**
	 * 是否默认APP版本  0:是  1:不是
	 */
	public static final String APPVERSION_DEFAULT = "0"; 
	public static final String APPVERSION_NODEFAULT = "1"; 
	
	/**
	 * 定时升级任务执行状态  01：已执行  02:未执行
	 */
	public static final String APPTASKTODOZT_YES = "01"; 
	public static final String APPTASKTODOZT_NO = "02"; 
	
	/**
	 * 更新类型  01：设备  02：单位  03：地区
	 */
	public static final String TARGETLX_SB = "01"; 
	public static final String TARGETLX_DW = "02"; 
	public static final String TARGETLX_DQ = "03"; 

	/**
	 * 二维码生成类型 01:版本控制中心二维码、02：任意文本
	 */
	public static final String QRCODE_SCLX_APPSTORE = "01";
	public static final String QRCODE_SCLX_TXTINPUT = "02";
	
	/**
	 * 短连接url模板
	 */
	public static String shortUrl_perfix = "/VersionControlCenter/sti/";
	public static String srcUrl_perfix = "/VersionControlCenter/appUpload.do?para={qqlx=%27appCenter%27,ssxm=%27{@ssxm}%27}";

}
