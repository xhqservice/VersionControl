package com.jadlsoft.model.xtgl;

public class UserBean{
	public static final String db_tablename = "t_xt_user";
	public static final String db_tablepkfields = "id";
   /*
    * 自增主键
    */
    private String id;
   /*
    * 用户ID
    */
    private String userid;
   /*
    * 用户名
    */
    private String username;
   /*
    * 密码
    */
    private String password;
   /*
    * 所属单位（预留字段）
    */
    private String qydm;
   /*
    * 行政区划（预留字段）
    */
    private String xzqh;
   /*
    * 录入时间
    */
    private java.util.Date lrsj;
   /*
    * 最后修改时间
    */
    private java.util.Date zhxgsj;
   /*
    * 注销时间
    */
    private java.util.Date zxsj;
   /*
    * 联系人
    */
    private String lxr;
   /*
    * 联系人联系电话
    */
    private String lxrlxdh;
   /*
    * 状态（0：有效；1:无效）
    */
    private String zt;
    
    /*
     * 用户角色
     */
    private Role role;
  	
    public void setId(String id){
	    this.id = id;
    }
    public String getId(){
	    return this.id;
    }
    public void setUserid(String userid){
	    this.userid = userid;
    }
    public String getUserid(){
	    return this.userid;
    }
    public void setUsername(String username){
	    this.username = username;
    }
    public String getUsername(){
	    return this.username;
    }
    public void setPassword(String password){
	    this.password = password;
    }
    public String getPassword(){
	    return this.password;
    }
   
    public void setXzqh(String xzqh){
	    this.xzqh = xzqh;
    }
    public String getXzqh(){
	    return this.xzqh;
    }
    public void setLrsj(java.util.Date lrsj){
	    this.lrsj = lrsj;
    }
    public java.util.Date getLrsj(){
	    return this.lrsj;
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
    public void setLxr(String lxr){
	    this.lxr = lxr;
    }
    public String getLxr(){
	    return this.lxr;
    }
    public void setLxrlxdh(String lxrlxdh){
	    this.lxrlxdh = lxrlxdh;
    }
    public String getLxrlxdh(){
	    return this.lxrlxdh;
    }
    public void setZt(String zt){
	    this.zt = zt;
    }
    public String getZt(){
	    return this.zt;
    }
    public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String validate(){
    	StringBuffer errMsg = new StringBuffer();
		/* 检查非空项 */
		if(id == null || id.equals("")){	  		
		    errMsg.append("自增主键为空！");
		}
		if(userid == null || userid.equals("")){	  		
		    errMsg.append("用户ID为空！");
		}
		if(username == null || username.equals("")){	  		
		    errMsg.append("用户名为空！");
		}
		/* 检查长度 */
		if(id != null && id.getBytes().length > 17){
		    errMsg.append("自增主键超长！容许长度：17。"); 
		}	   	
		if(userid != null && userid.getBytes().length > 30){
		    errMsg.append("用户ID超长！容许长度：30。"); 
		}	   	
		if(username != null && username.getBytes().length > 100){
		    errMsg.append("用户名超长！容许长度：100。"); 
		}	   	
		if(password != null && password.getBytes().length > 30){
		    errMsg.append("密码超长！容许长度：30。"); 
		}	   	
		if(qydm != null && qydm.getBytes().length > 13){
		    errMsg.append("所属单位（预留字段）超长！容许长度：13。"); 
		}	   	
		if(xzqh != null && xzqh.getBytes().length > 6){
		    errMsg.append("行政区划（预留字段）超长！容许长度：6。"); 
		}	   	
		if(lxr != null && lxr.getBytes().length > 32){
		    errMsg.append("联系人超长！容许长度：32。"); 
		}	   	
		if(lxrlxdh != null && lxrlxdh.getBytes().length > 20){
		    errMsg.append("联系人联系电话超长！容许长度：20。"); 
		}	   	
		if(zt != null && zt.getBytes().length > 1){
		    errMsg.append("状态（0：有效；1:无效）超长！容许长度：1。"); 
		}	   	
	  	return errMsg.toString();
    }
	public String getQydm() {
		return qydm;
	}
	public void setQydm(String qydm) {
		this.qydm = qydm;
	}
}