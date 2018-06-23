package com.jadlwork.model;

/**
 * 返回数据的统一对象
 * @ClassName: ResultBean 
 * @Description: 返回信息时候封装返回的内容
 * @author: 李春晓
 * @date: 2016-12-22 下午01:26:29
 */
public class ResultBean {

	private String statusCode;	//状态码
	private Object msg;			//返回的信息
	private Object arg1;		//参数1
	private Object arg2;		//参数2
	private Object arg3;		//参数3
	private Object arg4;		//参数4
	// ...之后有其他的字段可以添加
	
	
	public ResultBean() {
		super();
	}
	public ResultBean(String statusCode, Object msg) {
		super();
		this.statusCode = statusCode;
		this.msg =  msg;
	}
	public ResultBean(String statusCode, Object msg, String arg1) {
		super();
		this.statusCode = statusCode;
		this.msg = msg;
		this.arg1 = arg1;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	public Object getArg1() {
		return arg1;
	}
	public void setArg1(Object arg1) {
		this.arg1 = arg1;
	}
	public Object getArg2() {
		return arg2;
	}
	public void setArg2(Object arg2) {
		this.arg2 = arg2;
	}
	public Object getArg3() {
		return arg3;
	}
	public void setArg3(Object arg3) {
		this.arg3 = arg3;
	}
	public Object getArg4() {
		return arg4;
	}
	public void setArg4(Object arg4) {
		this.arg4 = arg4;
	}
	
}
