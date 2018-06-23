package com.jadlwork.model.app;

public class AppAutotaskBean{
	public static final String db_tablename = "t_app_autotask";
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
    * 执行时间
    */
    private java.util.Date todotime;
    /*
     * 执行状态 
     */
     private String todozt;
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
  	
    public String getTodozt() {
		return todozt;
	}
	public void setTodozt(String todozt) {
		this.todozt = todozt;
	}
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
    public void setTodotime(java.util.Date todotime){
	    this.todotime = todotime;
    }
    public java.util.Date getTodotime(){
	    return this.todotime;
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
		if(appname != null && appname.getBytes().length > 100){
		    errMsg.append("应用名称超长！容许长度：100。"); 
		}	   	
		if(version != null && version.getBytes().length > 30){
		    errMsg.append("版本号超长！容许长度：30。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：有效   1：无效超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}