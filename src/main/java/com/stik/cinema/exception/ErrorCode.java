package com.stik.cinema.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(1),
	NOT_FOUND(2),
	ALREADY_OCCUPIED(3);
	private final int errorId;

	ErrorCode(int errorId) {
		this.errorId = errorId;
	}

}
