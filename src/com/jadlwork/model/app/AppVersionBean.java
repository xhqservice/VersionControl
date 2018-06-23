package com.jadlwork.model.app;
/**
 * 
 * app版本信息bean
 * @author wujiaxu
 * @Time 2017-12-20 上午9:12:36
 *
 */
public class AppVersionBean{
	public static final String db_tablename = "t_app_version";
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
    * APP安装包
    */
    private String apk;
   /*
    * APP安装包存放地址
    */
    private String apksrc;
   /*
    * 应用版本
    */
    private String version;
   /*
    * 版本说明
    */
    private String versiondesc;
   /*
    * 类型 0:安卓  1:IOS
    */
    private String type;
   /*
    * 是否默认升级包  0:是  1:不是
    */
    private String isdefault;
   /*
    * 创建时间
    */
    private java.util.Date cjsj;
   /*
    * 最后修改时间
    */
    private java.util.Date zhxgsj;
   /*
    * 注销时间
    */
    private java.util.Date zxsj;
    /*
     * apk大小
     */
    private String apksize;
    /*
     * 版本versionCode
     */
    private String versioncode;
   /*
    * 状态 0：有效   1：无效
    */
    private String zt;
  	
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
    public void setApk(String apk){
	    this.apk = apk;
    }
    public String getApk(){
	    return this.apk;
    }
    public void setApksrc(String apksrc){
	    this.apksrc = apksrc;
    }
    public String getApksrc(){
	    return this.apksrc;
    }
    public void setVersion(String version){
	    this.version = version;
    }
    public String getVersion(){
	    return this.version;
    }
    public void setVersiondesc(String versiondesc){
	    this.versiondesc = versiondesc;
    }
    public String getVersiondesc(){
	    return this.versiondesc;
    }
    public void setType(String type){
	    this.type = type;
    }
    public String getType(){
	    return this.type;
    }
    public void setIsdefault(String isdefault){
	    this.isdefault = isdefault;
    }
    public String getIsdefault(){
	    return this.isdefault;
    }
    public void setCjsj(java.util.Date cjsj){
	    this.cjsj = cjsj;
    }
    public java.util.Date getCjsj(){
	    return this.cjsj;
    }
    public void setZhxgsj(java.util.Date zhxgsj){
	    this.zhxgsj = zhxgsj;
    }
    public java.util.Date getZhxgsj(){
	    return this.zhxgsj;
    }
    public void setZxsj(java.util.Date zxsj){
	    this.zxsj = zxsj;
    }
    public java.util.Date getZxsj(){
	    return this.zxsj;
    }
    public String getApksize() {
		return apksize;
	}
	public void setApksize(String apksize) {
		this.apksize = apksize;
	}
	public String getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
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
		if(apk != null && apk.getBytes().length > 50){
		    errMsg.append("APP安装包超长！容许长度：50。"); 
		}	   	
		if(apksrc != null && apksrc.getBytes().length > 200){
		    errMsg.append("APP安装包存放地址超长！容许长度：200。"); 
		}	   	
		if(version != null && version.getBytes().length > 30){
		    errMsg.append("应用版本超长！容许长度：30。"); 
		}	   	
		if(versiondesc != null && versiondesc.getBytes().length > 300){
		    errMsg.append("版本说明超长！容许长度：300。"); 
		}	   	
		if(type != null && type.getBytes().length > 1){
		    errMsg.append("类型 0:安卓  1:IOS超长！容许长度：1。"); 
		}	   	
		if(isdefault != null && isdefault.getBytes().length > 1){
		    errMsg.append("是否默认升级包  0:是  1:不是超长！容许长度：1。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：有效   1：无效超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}