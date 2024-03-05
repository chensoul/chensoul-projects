package com.chensoul.spring.boot.web.webmvc.cookie;

/**
 * This exception is thrown when there are problems found with cookie.
 *
 * @author Hal Deadman
 * @since 6.2.0
 */
public class InvalidCookieException extends RuntimeException {
    private static final long serialVersionUID = -994393142011101111L;

    public InvalidCookieException(final String message) {
        super(message);
    }
}
