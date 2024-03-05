package com.chensoul.spring.boot.websocket.exception;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class IllegalChannelException extends RuntimeException {

    /**
     *
     */
    public IllegalChannelException() {
        super();
    }

    /**
     * @param message
     */
    public IllegalChannelException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public IllegalChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public IllegalChannelException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public IllegalChannelException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
