package com.chensoul.exception;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public class RequestNotPermittedException extends RuntimeException {
    /**
     * <p>Constructor for RequestNotPermittedException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
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
