package com.stik.cinema.exception;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestControllerAdvice
public class RestResponseStatusExceptionResolver extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CinemaException.class)
	protected ResponseEntity<Object> handleCBException(CinemaException exception) {
		exception.printStackTrace();
		return ResponseEntity.status(406)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new CinemaMessageDto(exception.getMessage()));
	}
}
