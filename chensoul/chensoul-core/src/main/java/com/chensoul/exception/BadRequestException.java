package com.chensoul.exception;

/**
 * <p>BadRequestException class.</p>
 *
 * @author chensoul
 *
 */
public class BadRequestException extends RuntimeException {
    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for BadRequestException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
