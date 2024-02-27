package com.chensoul.exception;

public class InvalidInputException extends RuntimeException {
	private static final long serialVersionUID = -5709595068644751724L;

	public InvalidInputException() {
	}

	public InvalidInputException(final String message) {
		super(message);
	}

	public InvalidInputException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidInputException(final Throwable cause) {
		super(cause);
	}
}
