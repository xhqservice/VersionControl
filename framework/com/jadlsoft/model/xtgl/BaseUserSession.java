package com.jadlsoft.model.xtgl;

import java.util.Date;



public class BaseUserSession {
	private String userId;
	//用户名
	private String userName;
	//单位代码
	private String dwdm;
	//企业代码
	private String qydm;
	//单位名称
	private String dwmc;
	//角色
	private Role role;
	//用户类型
	private String yhlx;
	//行政区划
	private String xzqh;
	
	private Date signTime;
	
	private String admin;
	//公安机关联系电话
	private String gajglxdh;
	
	//爆破作业单位类别
    private String bpzydwlb;
    private String yxksrq; //有效开始日期
	private String yxjzrq;//有效截止日期
	private String roleid;//角色id
	
	
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getYxksrq() {
		return yxksrq;
	}
	public void setYxksrq(String yxksrq) {
		this.yxksrq = yxksrq;
	}
	public String getYxjzrq() {
		return yxjzrq;
	}
	public void setYxjzrq(String yxjzrq) {
		this.yxjzrq = yxjzrq;
	}
	public String getBpzydwlb() {
		return bpzydwlb;
	}
	public void setBpzydwlb(String bpzydwlb) {
		this.bpzydwlb = bpzydwlb;
	}
	
	public String getGajglxdh() {
		return gajglxdh;
	}
	public void setGajglxdh(String gajglxdh) {
		this.gajglxdh = gajglxdh;
	}
	
	public Date getSignTime() {
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public String getQydm() {
		return qydm;
	}
	public void setQydm(String qydm) {
		this.qydm = qydm;
	}
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getDwmc(){
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getYhlx() {
		return yhlx;
	}
	public void setYhlx(String yhlx) {
		this.yhlx = yhlx;
	}
	public String getXzqh() {
		return xzqh;
	}
	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}
}
