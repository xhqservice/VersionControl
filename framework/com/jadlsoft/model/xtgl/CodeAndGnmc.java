package com.jadlsoft.model.xtgl;

import java.util.Map;

public class CodeAndGnmc {

	private String code;//功能代码编号 以0000结尾为1级菜单  以00结尾为2级菜单
	
	private String gn;	//功能名称
	
	private Map<CodeAndGnmc,Map> map;

	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	public String getGn() {
		return gn;
	}

	public void setGn(String gn) {
		this.gn = gn;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map<CodeAndGnmc, Map> map) {
		this.map = map;
	}

	
}
