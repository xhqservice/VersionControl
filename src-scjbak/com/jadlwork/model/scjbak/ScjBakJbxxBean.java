package com.jadlwork.model.scjbak;

import java.util.Date;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: ScjBakJbxxBean
 * @描述: 手持机云备份基本信息Bean
 * @作者: lcx
 * @时间: 2018-2-6 上午9:22:19
 */
public class ScjBakJbxxBean {

	public static final String db_tablename = "t_scjbak_jbxx";
	public static final String db_tablepkfields = "id";
	
	private String id;
	private String flashid;
	private String bbh;
	private String dwdm;
	private String dh;	//代号
	private String mm;	//密码
	private Date cjsj;	//创建时间
	private Date xgsj;	//修改时间
	private Date zxsj;  //注销时间
	private String sblx;  //设备类型
	private String yylx;  //应用类型
	private String zt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlashid() {
		return flashid;
	}
	public void setFlashid(String flashid) {
		this.flashid = flashid;
	}
	public String getBbh() {
		return bbh;
	}
	public void setBbh(String bbh) {
		this.bbh = bbh;
	}
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}
	public String getDh() {
		return dh;
	}
	public void setDh(String dh) {
		this.dh = dh;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public Date getCjsj() {
		return cjsj;
	}
	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}
	public Date getXgsj() {
		return xgsj;
	}
	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}
	public Date getZxsj() {
		return zxsj;
	}
	public void setZxsj(Date zxsj) {
		this.zxsj = zxsj;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getSblx() {
		return sblx;
	}
	public void setSblx(String sblx) {
		this.sblx = sblx;
	}
	public String getYylx() {
		return yylx;
	}
	public void setYylx(String yylx) {
		this.yylx = yylx;
	}
	
}
