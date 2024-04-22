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
}
