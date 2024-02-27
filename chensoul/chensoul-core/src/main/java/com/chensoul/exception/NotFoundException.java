package com.chensoul.exception;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 9053957949691426855L;

    public NotFoundException() {
    }

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(final Throwable cause) {
        super(cause);
    }
}
