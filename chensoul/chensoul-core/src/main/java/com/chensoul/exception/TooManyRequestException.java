package com.chensoul.exception;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException(final String message) {
        super(message);
    }

    /**
     * <p>Constructor for RequestNotPermittedException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public TooManyRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
