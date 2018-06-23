package com.jadlsoft.taglib.page;
import javax.servlet.*;

public class ParamUtil {
  /**
   * 获得request中指定名称的参数值,若有中文乱码问题请增加转码部分
   * @param request ServletRequest对象
   * @param paramName 参数名称
   * @return 如果该变量值存在则返回该值，否则返回""
   */
  public static String getParameter( ServletRequest request, String paramName ) {
      String temp = request.getParameter(paramName);
      if (temp != null && !temp.equals("")) {
          //若有中文问题，请将下面语句注释
    	  /*
          try {
              temp = new String(temp.getBytes("8859_1"), "GB2312");
          }
          catch (Exception e) {
              return "";
          }
          */
          return temp;
      }
      else {
          return "";
      }
  }

  /**
   * 获得request中的int型参数值
   * @param request ServletRequest对象
   * @param paramName 参数名称
   * @param defaultNum 默认值，如果没有返回该值
   * @return 如果该参数值存在则返回其转换为int型的值，否则返回defaultNum
   */
  public static int getIntParameter( ServletRequest request, String paramName, int defaultNum ) {
      String temp = request.getParameter(paramName);
      if (temp != null && !temp.equals("")) {
          int num = defaultNum;
          try {
              num = Integer.parseInt(temp);
          }
          catch (Exception ignored) {
          }
          return num;
      }
      else {
          return defaultNum;
      }
  }
}///////////

