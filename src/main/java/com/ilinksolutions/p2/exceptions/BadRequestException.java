package com.ilinksolutions.p2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public BadRequestException(String email) {
		super(email + " is not a valid Email.");
	}
	
	public BadRequestException() {
		super("ID should be in number format!");
	}
	
	public BadRequestException(String missingField, boolean requiredfields) {
    	super("Missing required field(s): "+ missingField);
    }

}
