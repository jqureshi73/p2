package com.ilinksolutions.p2.exceptions;

public class RequiredFieldMissingException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
    public RequiredFieldMissingException(String missingField) {
    	super("Missing required field(s): "+ missingField);
    }

}
