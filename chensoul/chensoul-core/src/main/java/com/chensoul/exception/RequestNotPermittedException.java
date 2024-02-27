package com.chensoul.exception;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class RequestNotPermittedException extends RuntimeException {
    public RequestNotPermittedException() {
    }

    public RequestNotPermittedException(String message) {
        super(message);
    }

    public RequestNotPermittedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestNotPermittedException(Throwable cause) {
        super(cause);
    }
}
