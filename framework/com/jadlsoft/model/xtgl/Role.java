package com.jadlsoft.model.xtgl;

import java.util.List;

public class Role{
	public static final String db_tablename = "t_xt_role";
	public static final String db_tablepkfields = "roleid";
   /*
    * 角色ID
    */
    private String roleid;
   /*
    * 角色名称
    */
    private String rolename;
   /*
    * 状态（0：有效；1:无效）
    */
    private String zt;
    
    private List permisses;	  	//权限列表
  	
    public void setRoleid(String roleid){
	    this.roleid = roleid;
    }
    public String getRoleid(){
	    return this.roleid;
    }
    public void setRolename(String rolename){
	    this.rolename = rolename;
    }
    public String getRolename(){
	    return this.rolename;
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
		if(roleid == null || roleid.equals("")){	  		
		    errMsg.append("角色ID为空！");
		}
		/* 检查长度 */
		if(roleid != null && roleid.getBytes().length > 17){
		    errMsg.append("角色ID超长！容许长度：17。"); 
		}	   	
		if(rolename != null && rolename.getBytes().length > 30){
		    errMsg.append("角色名称超长！容许长度：30。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态（0：有效；1:无效）超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
	public List getPermisses() {
		return permisses;
	}
	public void setPermisses(List permisses) {
		this.permisses = permisses;
	}
}