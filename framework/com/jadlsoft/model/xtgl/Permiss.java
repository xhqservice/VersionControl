package com.jadlsoft.model.xtgl;

import java.util.List;

public class Permiss{
	public static final String db_tablename = "t_xt_permiss";
	public static final String db_tablepkfields = "permissid";
   /*
    * 权限ID
    */
    private String permissid;
   /*
    * 权限名称
    */
    private String permissname;
   /*
    * 状态（0：有效；1:无效）
    */
    private String zt;
    private List gnlbs;
  	
    public void setPermissid(String permissid){
	    this.permissid = permissid;
    }
    public String getPermissid(){
	    return this.permissid;
    }
    public void setPermissname(String permissname){
	    this.permissname = permissname;
    }
    public String getPermissname(){
	    return this.permissname;
    }
    public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
    }
    public List getGnlbs() {
		return gnlbs;
	}
	public void setGnlbs(List gnlbs) {
		this.gnlbs = gnlbs;
	}
	
	public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(permissid == null || permissid.equals("")){	  		
		    errMsg.append("权限ID为空！");
		}
		/* 检查长度 */
		if(permissid != null && permissid.getBytes().length > 17){
		    errMsg.append("权限ID超长！容许长度：17。"); 
		}	   	
		if(permissname != null && permissname.getBytes().length > 30){
		    errMsg.append("权限名称超长！容许长度：30。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态（0：有效；1:无效）超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
}