package com.chensoul.exception;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class RequestNotPermittedException extends RuntimeException {
    public RequestNotPermittedException(final String message) {
        super(message);
    }

    public RequestNotPermittedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
