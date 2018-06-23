package com.jadlsoft.exceptions;

public class DataStreamParseException extends Throwable{
	
	private static final long serialVersionUID = 4918737149887055704L;

	public DataStreamParseException(String errMsg){
		super(errMsg);
	}
}
