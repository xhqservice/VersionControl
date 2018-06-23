package com.jadlwork.model.app;
/**
 * 
 * app基本信息bean
 * @author wujiaxu
 * @Time 2017-12-20 上午9:11:38
 *
 */
public class AppJbxxBean{
	public static final String db_tablename = "t_app_jbxx";
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
    * 所属平台
    */
    private String ssxm;
   /*
    * 应用简介
    */
    private String appdesc;
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
     * 图标地址字段
     */
    private String iconsrc;
    /*
     * 排序
     */
    private String sort;
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
    public void setAppname(String appname){
	    this.appname = appname;
    }
    public String getAppname(){
	    return this.appname;
    }
    public void setSsxm(String ssxm){
	    this.ssxm = ssxm;
    }
    public String getSsxm(){
	    return this.ssxm;
    }
    public void setAppdesc(String appdesc){
	    this.appdesc = appdesc;
    }
    public String getAppdesc(){
	    return this.appdesc;
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
    public String getIconsrc() {
		return iconsrc;
	}
	public void setIconsrc(String iconsrc) {
		this.iconsrc = iconsrc;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
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
		if(ssxm != null && ssxm.getBytes().length > 2){
		    errMsg.append("所属平台超长！容许长度：2。"); 
		}	   	
		if(appdesc != null && appdesc.getBytes().length > 500){
		    errMsg.append("应用简介超长！容许长度：500。"); 
		}
		if(iconsrc != null && iconsrc.getBytes().length > 200){
		    errMsg.append("图标地址超长！容许长度：200。"); 
		}
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态 0：有效   1：无效超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}