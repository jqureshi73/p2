package com.ilinksolutions.p2.exceptions;

public class UpdateDataException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
    public UpdateDataException(Integer id) {
    	super("Failed to update the data for id: " + id);
    }


}
