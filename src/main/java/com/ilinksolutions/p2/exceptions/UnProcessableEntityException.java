package com.ilinksolutions.p2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnProcessableEntityException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnProcessableEntityException(String name) {
		super("Failed to save the data for: " + name);
	}

	public UnProcessableEntityException(Integer id) {
		super("Failed to update the data for id: " + id);
	}

}
