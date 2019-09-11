package com.ilinksolutions.p2.exceptions;

public class EmailException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public EmailException(String error) {
		super(error); 
	}

}
