package com.chensoul.spring.boot.websocket.exception;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class PrincipalNotFoundException extends RuntimeException {

    /**
     *
     */
    public PrincipalNotFoundException() {
        super();
    }

    /**
     * @param message
     */
    public PrincipalNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public PrincipalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public PrincipalNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public PrincipalNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
