package com.ilinksolutions.p2.exceptions;

public class NumbersFormatException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public NumbersFormatException() {
		super("ID should be in number format!");
	}

}
