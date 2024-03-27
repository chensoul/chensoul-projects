package com.chensoul.exception;

public class RequestNotPermittedException extends RuntimeException {
    public RequestNotPermittedException(final String message) {
        super(message);
    }

    /**
     * <p>Constructor for RequestNotPermittedException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public RequestNotPermittedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
