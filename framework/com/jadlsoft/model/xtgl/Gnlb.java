package com.jadlsoft.model.xtgl;

public class Gnlb{
	public static final String db_tablename = "t_dm_gnlb";
	public static final String db_tablepkfields = "gncode";
   /*
    * 功能代码
    */
    private String gncode;
   /*
    * 功能名称
    */
    private String gnname;
   /*
    * 链接
    */
    private String link;
   /*
    * 顺序号
    */
    private String sort;
   /*
    * 状态（0：有效；1:无效）
    */
    private String zt;
  	
    public void setGncode(String gncode){
	    this.gncode = gncode;
    }
    public String getGncode(){
	    return this.gncode;
    }
    public void setGnname(String gnname){
	    this.gnname = gnname;
    }
    public String getGnname(){
	    return this.gnname;
    }
    public void setLink(String link){
	    this.link = link;
    }
    public String getLink(){
	    return this.link;
    }
    public void setSort(String sort){
	    this.sort = sort;
    }
    public String getSort(){
	    return this.sort;
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
		if(gncode == null || gncode.equals("")){	  		
		    errMsg.append("功能代码为空！");
		}
		if(gnname == null || gnname.equals("")){	  		
		    errMsg.append("功能名称为空！");
		}
		/* 检查长度 */
		if(gncode != null && gncode.getBytes().length > 6){
		    errMsg.append("功能代码超长！容许长度：6。"); 
		}	   	
		if(gnname != null && gnname.getBytes().length > 30){
		    errMsg.append("功能名称超长！容许长度：30。"); 
		}	   	
		if(link != null && link.getBytes().length > 100){
		    errMsg.append("链接超长！容许长度：100。"); 
		}	   	
		if(sort != null && sort.getBytes().length > 6){
		    errMsg.append("顺序号超长！容许长度：6。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态（0：有效；1:无效）超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}