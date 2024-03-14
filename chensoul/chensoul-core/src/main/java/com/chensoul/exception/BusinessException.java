package com.chensoul.exception;

/**
 * <p>BusinessException class.</p>
 *
 * @author chensoul
 * @version $Id: $Id
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 9053957949691426855L;

    /**
     * <p>Constructor for BusinessException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public BusinessException(final String message) {
        super(message);
    }

    /**
     * <p>Constructor for BusinessException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
