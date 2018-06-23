package com.jadlsoft.exceptions;

/**
 * @功能   解析listConfig.xml配置文件的异常类
 * @作者 zhangsanjie add 2017-3-24 下午12:41:02
 */
public class ListConfigParseException extends Exception{
	private static final long serialVersionUID = -4543940266083509100L;
	private long id;
      public long getId(){
    	  return id;
      }
      public ListConfigParseException(String message,long id){
    	  super(message);
    	  this.id = id;
      }
}
