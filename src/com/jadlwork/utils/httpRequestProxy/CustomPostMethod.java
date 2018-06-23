package com.jadlwork.utils.httpRequestProxy;

import org.apache.commons.httpclient.methods.PostMethod;

/**         
 * @ClassName：GBKPostMethod   
 * @Description：   
 * @author ：zhangqing   
 * @date ：2015-4-16 下午12:52:40      
 * @version      
 */
public class CustomPostMethod extends PostMethod{     
	   private String defualt_charset = "ISO-8859-1";
	   public CustomPostMethod(String url){     
           super(url);     
       }     
       public CustomPostMethod(String url,String charset){
    	   super(url); 
    	   if(charset!=null){
    		   defualt_charset = charset;
    	   }
       }
       @Override    
       public String getRequestCharSet() {     
           return defualt_charset;     
       }     
}
