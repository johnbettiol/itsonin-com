package com.itsonin.exception;

import java.io.PrintWriter;
import java.io.StringWriter;


public class AppException extends RuntimeException {
		
	private static final long serialVersionUID = 1L;

	public AppException(){
		super();
	}
	
	public AppException(Throwable e){
		super(e);
	}
	
	public AppException(String msg, Throwable e){
		super(msg, e);
	}

	public AppException(String msg){
		super(msg);
	}

	public String getInternalErrorMessage()

	{
		Throwable cause = this.getCause();
		if(cause != null)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			cause.printStackTrace(pw);
			return sw.toString();
		}
		return null;
	}

}