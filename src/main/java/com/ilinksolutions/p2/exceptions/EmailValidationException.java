package com.ilinksolutions.p2.exceptions;

public class EmailValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public EmailValidationException(String email) {
		super(email + " is not a valid Email.");
	}

}
