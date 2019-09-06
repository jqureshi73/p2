package com.ilinksolutions.p2.exceptions;

public class EntityNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
    public EntityNotFoundException(Integer id) {
    	super("Entity not found for id: " + id);
    }
}
