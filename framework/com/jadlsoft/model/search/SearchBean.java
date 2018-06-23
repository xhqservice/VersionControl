package com.jadlsoft.model.search;

public class SearchBean {
	/**
	 * 查询条件
	 */
	private String conditions;
	/**
	 * 跳转路径
	 */
	private String forward;
	/**
	 * 登录用户行政区划
	 */
	private String curxzhqh;
	/**
	 * 排序字符串
	 */
	private String orderby;
	/**
	 * 扩展消息
	 */
	private String ext;
	/**
	 * 页号
	 */
	private String pageNo;
	/**
	 * 每页消息数量
	 */
	private String pageSize;
	/**
	 * 前、后、当前页
	 */
	private String choice;

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getCurxzhqh() {
		return curxzhqh;
	}

	public void setCurxzhqh(String curxzhqh) {
		this.curxzhqh = curxzhqh;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
}
