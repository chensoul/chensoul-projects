package com.chensoul.exception;

/**
 * <p>NotFoundException class.</p>
 *
 * @author chensoul
 *
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }

    /**
     * <p>Constructor for NotFoundException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
