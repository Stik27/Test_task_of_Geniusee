package com.stik.cinema.exception;

import lombok.Getter;

@Getter
public class CinemaException extends RuntimeException {
	private final ErrorCode errorCode;

	public CinemaException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}
