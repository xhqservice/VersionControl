package com.jadlsoft.model.xtgl;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class UserSessionBean extends BaseUserSession implements Serializable {

	/**
	 * 过滤字段
	 */
	private String xzqh;
	/**
	 * 行政区划名称
	 */
	private String xzqh_cn;
	/**
	 * 短的行政区划（长度：2位 或者 4位 或者 6位），用于控制用户的访问权限
	 */
	private String Shortxzqh;

	/**
	 * 用户的功能代码
	 */
	@SuppressWarnings("unchecked")
	private List gndmList;

	/**
	 * 登陆IP
	 */
	private String loginip;
	
	private String kbh;
	

	public String getKbh() {
		return kbh;
	}

	public void setKbh(String kbh) {
		this.kbh = kbh;
	}

	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getXzqh_cn() {
		return xzqh_cn;
	}

	public void setXzqh_cn(String xzqh_cn) {
		this.xzqh_cn = xzqh_cn;
	}

	public String getShortxzqh() {
		return Shortxzqh;
	}

	public void setShortxzqh(String shortxzqh) {
		Shortxzqh = shortxzqh;
	}

	public List getGndmList() {
		return gndmList;
	}

	public void setGndmList(List gndmList) {
		this.gndmList = gndmList;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}
	
	
	
	
}
