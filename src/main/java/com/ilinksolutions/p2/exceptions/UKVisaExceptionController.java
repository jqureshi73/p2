package com.ilinksolutions.p2.exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UKVisaExceptionController extends ResponseEntityExceptionHandler{
	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(value = EntityNotFoundException.class)
	public void notFoundException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.NOT_FOUND.value());
	}
	
	@ExceptionHandler(value = RequiredFieldMissingException.class)
	public void requiredFieldsException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
	
	@ExceptionHandler(value = SaveDataException.class)
	public void saveDataException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}
	
	@ExceptionHandler(value = UpdateDataException.class)
	public void updateDataException(HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}
	
}
