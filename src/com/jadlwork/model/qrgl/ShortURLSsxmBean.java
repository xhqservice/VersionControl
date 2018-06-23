package com.jadlwork.model.qrgl;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: ShortURLSsxmBean
 * @描述: 短连接和所属项目映射bean
 * @作者: lcx
 * @时间: 2018-1-30 下午1:35:58
 */
public class ShortURLSsxmBean {

	public static final String db_tablename = "t_shorturl_ssxm";
	public static final String db_tablepkfields = "id";
	
	/**
	 * ID   VARCHAR2(17) not null,
	  SSXM VARCHAR2(2),
	  SRCURL VARCHAR2(255),
	  SHORTURL VARCHAR2(255),
	  ZT  VARCHAR2(1)
	 */
	
	private String id;
	private String ssxm;
	private String srcurl;
	private String shorturl;
	private String zt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSsxm() {
		return ssxm;
	}
	public void setSsxm(String ssxm) {
		this.ssxm = ssxm;
	}
	public String getSrcurl() {
		return srcurl;
	}
	public void setSrcurl(String srcurl) {
		this.srcurl = srcurl;
	}
	public String getShorturl() {
		return shorturl;
	}
	public void setShorturl(String shorturl) {
		this.shorturl = shorturl;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
}
