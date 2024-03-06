package com.chensoul.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 9053957949691426855L;

    public BusinessException() {
    }

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BusinessException(final Throwable cause) {
        super(cause);
    }
}
