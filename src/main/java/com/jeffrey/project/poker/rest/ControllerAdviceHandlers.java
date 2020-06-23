package com.jeffrey.project.poker.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jeffrey.project.poker.exceptions.PlayerAlreadyExistsException;

@ControllerAdvice
public class ControllerAdviceHandlers {

	
	@ResponseBody
	@ExceptionHandler(PlayerAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.IM_USED)
	String playerAlreadyExistsHandler(PlayerAlreadyExistsException ex) {
		return ex.getMessage();
	}
	
}
