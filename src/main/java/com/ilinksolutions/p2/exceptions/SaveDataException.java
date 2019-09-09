package com.ilinksolutions.p2.exceptions;

public class SaveDataException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
    public SaveDataException(String name) {
    	super("Failed to save the data for: " + name);
    }

}
