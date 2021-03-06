package com.msf.ifsc.common.exceptions;

public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRequestException(String message) {
		super(message);
	}

}
