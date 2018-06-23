package com.jadlwork.model.scjbak;

import java.util.Date;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: ScjBakBean
 * @描述: 手持机云备份备份信息Bean
 * @作者: lcx
 * @时间: 2018-2-6 上午9:22:19
 */
public class ScjBakBean {

	public static final String db_tablename = "t_scjbak_bak";
	public static final String db_tablepkfields = "id";
	
	private String id;
	private String scjid;	//手持机云备份基本信息id
	private String filesize;	//备份文件大小
	private String filename;	//备份文件路径
	private String sha1;	//签名
	private Date bfsj;	//备份时间
	private Date xgsj;	//修改时间
	private Date cxsj;	//撤销时间
	private String zt;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScjid() {
		return scjid;
	}
	public void setScjid(String scjid) {
		this.scjid = scjid;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public Date getBfsj() {
		return bfsj;
	}
	public void setBfsj(Date bfsj) {
		this.bfsj = bfsj;
	}
	public Date getXgsj() {
		return xgsj;
	}
	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}
	public Date getCxsj() {
		return cxsj;
	}
	public void setCxsj(Date cxsj) {
		this.cxsj = cxsj;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
}
