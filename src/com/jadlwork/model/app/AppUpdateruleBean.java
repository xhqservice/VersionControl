package com.jadlwork.model.app;

public class AppUpdateruleBean{
	public static final String db_tablename = "t_app_updaterule";
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
    * 版本号
    */
    private String version;
   /*
    * 作用目标
    */
    private String target;
   /*
    * 目标类型
    */
    private String targetlx;
   /*
    * 开始时间
    */
    private java.util.Date kssj;
   /*
    * 结束时间
    */
    private java.util.Date jssj;
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
    
    public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setTarget(String target){
	    this.target = target;
    }
    public String getTarget(){
	    return this.target;
    }
    public void setTargetlx(String targetlx){
	    this.targetlx = targetlx;
    }
    public String getTargetlx(){
	    return this.targetlx;
    }
    public void setKssj(java.util.Date kssj){
	    this.kssj = kssj;
    }
    public java.util.Date getKssj(){
	    return this.kssj;
    }
    public void setJssj(java.util.Date jssj){
	    this.jssj = jssj;
    }
    public java.util.Date getJssj(){
	    return this.jssj;
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
		 	
		if(target != null && target.getBytes().length > 50){
		    errMsg.append("作用目标超长！容许长度：50。"); 
		}	   	
		if(targetlx != null && targetlx.getBytes().length > 2){
		    errMsg.append("目标类型超长！容许长度：2。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：有效   1：无效超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}