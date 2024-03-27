package com.chensoul.spring.boot.websocket.exception;
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
