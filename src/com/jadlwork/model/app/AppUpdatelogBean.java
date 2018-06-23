package com.jadlwork.model.app;
/**
 * 
 * APP用户更新日志bean
 * @author wujiaxu
 * @Time 2017-12-20 上午9:11:48
 *
 */
public class AppUpdatelogBean{
	public static final String db_tablename = "t_app_updatelog";
	public static final String db_tablepkfields = "id";
   /*
    * 自增ID
    */
    private String id;
   /*
    * 应用ID
    */
    private String appid;
   /*
    * 应用名称
    */
    private String appname;
   /*
    * 版本号
    */
    private String version;
   /*
    * 所属平台
    */
    private String ssxm;
   /*
    * 设备ID
    */
    private String sbid;
   /*
    * 用户ID
    */
    private String userid;
   /*
    * 单位代码
    */
    private String dwdm;
   /*
    * 行政区划
    */
    private String xzqh;
   /*
    * 下载时间
    */
    private java.util.Date xzsj;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setAppid(String appid){
	    this.appid = appid;
    }
    public String getAppid(){
	    return this.appid;
    }
    public void setAppname(String appname){
	    this.appname = appname;
    }
    public String getAppname(){
	    return this.appname;
    }
    public void setVersion(String version){
	    this.version = version;
    }
    public String getVersion(){
	    return this.version;
    }
    public void setSsxm(String ssxm){
	    this.ssxm = ssxm;
    }
    public String getSsxm(){
	    return this.ssxm;
    }
    public void setSbid(String sbid){
	    this.sbid = sbid;
    }
    public String getSbid(){
	    return this.sbid;
    }
    public void setUserid(String userid){
	    this.userid = userid;
    }
    public String getUserid(){
	    return this.userid;
    }
    public void setDwdm(String dwdm){
	    this.dwdm = dwdm;
    }
    public String getDwdm(){
	    return this.dwdm;
    }
    public void setXzqh(String xzqh){
	    this.xzqh = xzqh;
    }
    public String getXzqh(){
	    return this.xzqh;
    }
    public void setXzsj(java.util.Date xzsj){
	    this.xzsj = xzsj;
    }
    public java.util.Date getXzsj(){
	    return this.xzsj;
    }
  	
    public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("自增ID为空！");
		}
		/* 检查长度 */
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("自增ID超长！容许长度：17。"); 
		}	   	
		if(appid != null && appid.getBytes().length > 30){
		    errMsg.append("应用ID超长！容许长度：30。"); 
		}	   	
		if(appname != null && appname.getBytes().length > 30){
		    errMsg.append("应用名称超长！容许长度：30。"); 
		}	   	
		if(version != null && version.getBytes().length > 30){
		    errMsg.append("版本号超长！容许长度：30。"); 
		}	   	
		if(ssxm != null && ssxm.getBytes().length > 2){
		    errMsg.append("所属平台超长！容许长度：2。"); 
		}	   	
		if(sbid != null && sbid.getBytes().length > 50){
		    errMsg.append("设备ID超长！容许长度：50。"); 
		}	   	
		if(userid != null && userid.getBytes().length > 32){
		    errMsg.append("用户ID超长！容许长度：32。"); 
		}	   	
		if(dwdm != null && dwdm.getBytes().length > 13){
		    errMsg.append("单位代码超长！容许长度：13。"); 
		}	   	
		if(xzqh != null && xzqh.getBytes().length > 6){
		    errMsg.append("行政区划超长！容许长度：6。"); 
		}	   	
	  	return errMsg.toString();
    }
}