package com.jadlsoft.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;


public class DateUtils {
	public static Logger log = Logger.getLogger(DateUtils.class);

	public static int YEAR = 1;

	public static int MONTH = 2;

	public static int DATE = 3;
	
	public static final String defaultDateFormat = "yyyy-MM-dd";
	public static final String defaultTimeFormat = "yyyy-MM-dd HH:mm:ss";
	public static String getCurrentDateStr(String format) {
		if (format == null || format.equals("")) {
			 return getCurrentDateStr();
		}
		return (new SimpleDateFormat(format).format(new Date()));
	}
	
	public static String getCurrentDateStr(){
		return (new SimpleDateFormat(defaultDateFormat).format(new Date()));
	}
	
	/**
	 * Description: 获取当前日期 return: 2006-09-28
	 */
	public static String getCurrentData() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());	// 初始化calendar
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}
	
	public static String getCurrentDate() {
		return (new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}
	
	/**
	 * 李洪磊 2009-03-16 
	 * 获得某个年月日
	 * @param kind  获得变更的类型
	 * @param value  变更的量
	 * @return
	 */
	public static String getDate(int type, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (type == YEAR) {
			calendar.add(Calendar.YEAR, value);
		} else if (type == MONTH) {
			calendar.add(Calendar.MONTH, value);
		} else if (type == DATE) {
			calendar.add(Calendar.DATE, value);
		} else {
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}
	/**
	 * 获取上个月今天日期：today：2008-08-18
	 * @return 2008-07-18
	 */
	public static String getLastMonthToday() {	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY)-1);   
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}
	
	/**
	 * 获取下个月今天日期：today：2008-08-18
	 * @return 2008-09-18
	 */
	public static String getNextMonthToday() {	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY)+1);   
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}
	
	/**
	 * 获得明年的今天日期
	 * @return
	 */
	public static Date getNextYearToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);   
		return calendar.getTime();
	}
	/**
	 * @param days
	 * @return 当前日期days天以前的日期
	 */
	public static String getLastFewDaysAgo(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day - days);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * Description: 获取当前日期 return: 2006-09-28 14:05:10
	 */
	public static String getCurrentData(String format) {
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd";
		}
		return (new SimpleDateFormat(format).format(new Date()));
	}

	//培训系统日期转换
	public static Date getCurrentData(Date date ,String format)  {
		if (format == null || format.equals("")) {
			return date;
		}
	
		SimpleDateFormat s =  new SimpleDateFormat(format);
		try {
			String a = new SimpleDateFormat(format).format(date);
			date= s.parse(a);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
		
	}
	
	
	/**
	 * Description: 获取当前日期 return: 2006-09-28 14:05:10
	 */
	public static String getCurrentDataTime() {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
	public static String getDateTime(Date date) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
	}
	/**
	 * Description: 获取日期
	 * 
	 * @param fulldate:【2006-09-28 14:05:10】格式的字符串,数据库中Date类型字段所记录时间值为此格式 return: 2006-09-28
	 */
	public static String getData(String fulldate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = dateFormat.parse(fulldate);
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(date);
		} catch (Exception e) {
			log.error("转换成String类型的日期时发生错误!", e);
			return null;
		}
	}

	/**
	 * Description: 获取当前年 return: 2006-09-28 14:05:10
	 */
	public static String getCurrentYear() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}

	/**
	 * Description: 通过给当前的日期增加偏移量而获取指定的日期
	 * 
	 * @param type:类常量YEAR、MONTH、DATE中的一个,分别指定对年、月、日增加偏移量
	 * @param value:偏移量,可正可负
	 * @return: 2006-10-10
	 */
	public static String getOffsetData(int type, int value) {
		Calendar cal = Calendar.getInstance();
		return createOffsetData(cal, type, value);
	}

	/**
	 * Description: 通过给指定的日期增加偏移量而获取指定的日期
	 * 
	 * @param fulldate:给定的日期
	 * @param type:类常量YEAR、MONTH、DATE中的一个,分别指定对年、月、日增加偏移量
	 * @param value:偏移量,可正可负
	 * @return: 2006-10-10
	 */
	public static String getOffsetData(String fulldate, int type, int value) {
		if (fulldate.length() < 10) {
			return getOffsetData(type, value);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(fulldate.substring(0, 4)), Integer.parseInt(fulldate.substring(5, 7)) - 1, Integer.parseInt(fulldate.substring(8, 10)));
		return createOffsetData(cal, type, value);
	}
	
	/**
	 * Description: 获取当前日期 return: Date
	 */
	public static Date createCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		//2009-10-30 张俊吉 解决天津oracl8i问题，不得不作出修改，开购买证时候，插入的购买证或者别的信息出现了乱码
		if(SysConfigUtils.getProperty("oracle8i" , "false").equals("true")){
			return createDate(DateUtils.getCurrentDate());
		}else{
			calendar.setTimeInMillis(System.currentTimeMillis());
			return calendar.getTime();
		}	
	}

	/**
	 * Description: 获取指定日期
	 * 
	 * @param appointdate:指定的时间,格式为【2006-09-28】
	 *            return: Date
	 */
	public static Date createDate(String appointdate) {
		// 李洪磊 修改 2008-07-16
		if(appointdate == null || appointdate.trim().equals("")) {
			return null;
		}
		try {
			return (new SimpleDateFormat("yyyy-MM-dd").parse(appointdate));
		} catch (Exception e) {
			log.error("转换成Date类型的日期时发生错误!", e);
			return null;
		}
	}

	/**
	 * Description: 判断日期1--date1是否在日期2--date2之前
	 * 
	 * @param date1:日期1,格式为【2006-09-28】
	 * @param date2:日期2,格式为【2006-09-28】
	 * return: 如date1在date2之前则返回true,否则返回false.异常时返回false
	 */
	public static boolean compareDate(String date1, String date2) {
		try {
			return createDate(date1).before(createDate(date2));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Description: 通过对日期增加偏移量而获取指定的日期
	 * 
	 * @param cal:Calendar类实例
	 * @param type:类常量YEAR、MONTH、DATE中的一个,分别指定对年、月、日增加偏移量
	 * @param value:偏移量,可正可负
	 * @return: 2006-10-10
	 */
	private static String createOffsetData(Calendar cal, int type, int value) {
		if (type == YEAR) {
			cal.add(Calendar.YEAR, value);
		} else if (type == MONTH) {
			cal.add(Calendar.MONTH, value);
		} else if (type == DATE) {
			cal.add(Calendar.DATE, value);
		} else {
		}
		return (cal.get(Calendar.YEAR) + "-" + int2string(cal.get(Calendar.MONTH) + 1) + "-" + int2string(cal.get(Calendar.DATE)));
	}

	/**
	 * Description: 将日期中的用到的月、日的int值转换为String值,如将5月转换为05月返回
	 * 
	 * @param value:待转换的值
	 * @return: 05
	 */
	private static String int2string(int value) {
		String returnValue = String.valueOf(value);
		if (returnValue.length() == 1) {
			returnValue = "0" + returnValue;
		}
		return returnValue;
	}

	/**
	 * Description: 将日期改为String return: 2006-09-28
	 */
	public static String getDatatoString(Date date) {
		if(date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	

	/**
	 * Description: 将日期改为String
	 * @param date:指定的日期
	 * @param foramt:指定的日期样式
	 * return: 2006-09-28
	 */
	public static String getDatatoString(Date date, String foramt) {
		if(date == null) {
			return null;
		}
		return new SimpleDateFormat(foramt).format(date);
	}
	
	
	/**
	 * Description: 转换日期格式
	 * @param totransdate:要转换的时间,格式为【2006-09-28】
	 * @param 格式:原格式
	 * @param 格式:要转换为的格式
	 * @return: String
	 */
	public static String transDateFormat(String totransdate, String srcformat, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			SimpleDateFormat tmpformat = new SimpleDateFormat(srcformat);
			Date date = tmpformat.parse(totransdate);
			return dateFormat.format(date);
		} catch (Exception e) {
			log.error("转换Date格式时发生错误!", e);
			return null;
		}
	}

	/**
	 * util的Date转为sql的Date
	 */
	public static java.sql.Date getUtilDate2SqlDate(java.util.Date date, String format) {
		return java.sql.Date.valueOf(new SimpleDateFormat(format).format(date));
	}

	/**
	 * 
	 * @param field
	 * @param amount
	 * @return
	 */
	public static String getDiffDate(int field , int amount) {
		GregorianCalendar d = new GregorianCalendar();
		d.add(field , amount);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = d.getTime();
		return format.format(d1);
	}
	
	/**
	 * 计算两个日期相差天数
	 * @param fromDate
	 * @param toDate
	 * @return 天数
	 */
	public static int countDays(String fromDate, String toDate) {
		int days = 0;
		try {
			long l = (createDate(toDate).getTime() - createDate(fromDate).getTime());
			days = (int) (l / 60 / 60 / 1000 / 24);
		} catch (Exception e) {
			log.error("计算两个日期相差天数出错", e);
		}
		return days;
	}
	
	public static Timestamp tranString2Date(String timeString){	
		if(timeString.indexOf("-") != -1){
			return tranString2Timestamp(timeString , "yyyy-MM-dd");
		}
		return tranString2Timestamp(timeString , "yyyyMMdd");
	}
	
	public static Timestamp tranString2Timestamp(String timeString){		
		/*modify by 张俊吉 at 2008-05-19 领用方法的时间精确到分钟，这时转化时间有问题*/
		if(timeString.length() == 12)
		{
			return tranString2Timestamp(timeString , "yyyyMMddHHmm");
		}
		/*modify by 张俊吉 at 2008-05-19 领用方法的时间精确到分钟，这时转化时间有问题--------end*/
		return tranString2Timestamp(timeString , "yyyyMMddHHmmss");
	}
	
	public static Timestamp tranString2Timestamp(String timeString , String dateFormat){
		SimpleDateFormat format1 = new SimpleDateFormat(dateFormat);		
		Date d1 = new Date();
		try {
			d1 = format1.parse(timeString);
		} catch (ParseException e) {		
			log.error("字符串转日期错误。字符串为：" + timeString);
			e.printStackTrace();
		}
		return new Timestamp(d1.getTime());
	}
	
	/**
	 * 比较前、后时间差
	 * @param now 现在
	 * @param last 过去
	 * @return 相差的天数
	 */
	public static long compare2Date(String now , String last){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date passDate1 = null;
		java.util.Date passDate2 = null;
		try {
			passDate1 = simpleDateFormat.parse(now);
			passDate2 = simpleDateFormat.parse(last);
		} catch (ParseException e) {
			return 0;
		}
		GregorianCalendar startDate = new GregorianCalendar(2000, 0, 1);
		if (passDate2.getTime() < startDate.getTimeInMillis()) {
			return 0;
		}
		return (passDate1.getTime() - passDate2.getTime()) / (24 * 60 * 60 * 1000);
	}
	/**
	 * 获取当前的小时，24小时制
	 * @return
	 */
	public static int getCurrentHour(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取当前的分钟
	 * @return
	 */
	public static int getCurrentMinute(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar.get(Calendar.MINUTE);
	}
/*=================================================================================================
 * 
 * 					以下为数据流解析用到的方法 add by 张俊吉 at 2008-11-12 
 * 
 * ================================================================================================*/	
	/**
	 * 返回当前时间及日期,用于数据解析
	 * @return 返回格式：yyyy-MM-dd hh:mm:ss
	 * @date:2008-7-18 上午11:39:18
	 */
	public static String getCurrentTime(){
		java.util.Date d = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(d);
	}
	/**
	 * @date:2008-11-12 上午10:44:52
	 * @param date
	 * @return
	 */
	public static String tranDate2String(Date date){
		return (new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
	
	public static String tranTimestamp2StringWithFormat(Date date){
		return tranTimestamp2String(date , "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String tranTimestamp2StringNoFormat(Date date){
		return tranTimestamp2String(date , "yyyyMMddHHmmss");
	}
	
	private static String tranTimestamp2String(Date date , String format){
		return (new SimpleDateFormat(format).format(date));
	}
	
	/**
	 * 获取上两个月今天日期：
	 * 
	 */
	public static String getLastTwoMonthToday() {	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY)-2);   
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}
	
	/**
	 * add by zhaohuibin 2010-05-27 DataStreamWeb中用到了此方法，
	 * 而dataStream.jar打包时删除了此方法，故在此添加此方法
	 * @函数名：isGreaterThanToay
	 * @功能：传入的日期字符串是否大于今天的日期
	 * @param date
	 * @return true 大于 false 小于
	 * boolean
	 */
	public static boolean isGreaterThanToay(String date){
		return getCurrentDate().compareTo(date) > 0 ?true:false;
	}
	
	/** 
	 * @功能：得到本月的第一天
	 * @参数：
	 * @return
	 * @返回值：String
	 * create by zhaohuibin 2010-9-21 上午10:21:54
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar
				.getActualMinimum(Calendar.DAY_OF_MONTH));

		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * @功能：得到本月的最后一天
	 * @参数：
	 * @return
	 * @返回值：String
	 * create by zhaohuibin 2010-9-21 上午10:22:01
	 */
	public static String getMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}
	//日期+30天返回
	public static Date getAddData(Date date )  {
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(date); 
		theCa.add(theCa.DATE, +30); 
		return theCa.getTime();	
	}
	//日期加3年返回
	public static Date getAddYear(Date date )  {
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(date); 
		theCa.add(theCa.YEAR, +3); 
		return theCa.getTime();	
	}
	/**
	 * @功能 两个日期比较
	 * @参数 @param DATE1
	 * @参数 @param DATE2
	 * @参数 @return
	 * @作者 zhangsanjie add 2017-3-2 下午2:43:14
	 * @返回值类型 int
	 */
	public static int compareDateStr(String DATE1, String DATE2) {
	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	public static String getDateAddMin(String nowtime, int addMix) {

		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(nowtime);
		} catch (ParseException e) {
			log.error("日期格式化时发生错误!", e);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, addMix);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        
	}
	
	public static String DateToString(Date date,String format){
		if(date == null){
			return null;
		}
		if(format ==null || "".equals(format)){
			return DateToString(date);
		}
		return new SimpleDateFormat(format).format(date);
	}
	public static String DateToString(Date date) {
		return (new SimpleDateFormat(defaultDateFormat).format(date));
	}
	 
	public static Date StringToDate(String dateStr,String format){
		if(dateStr == null || "".equals(dateStr.trim())){
			return null;
		}
		if(format ==null || "".equals(format)){
			return StringToDate(dateStr);
		}
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			log.error("String转换成日期类型的时发生错误!", e);
		}
		return date;
	}
	public static Date StringToDate(String dateStr){
		if(dateStr == null || "".equals(dateStr.trim())){
			return null;
		}
		Date date = null; 
		try {
			date = new SimpleDateFormat(defaultDateFormat).parse(dateStr);
		} catch (ParseException e) {
			log.error("String转换成日期类型的时发生错误!", e);
		}
		return date;
	}
	
}
