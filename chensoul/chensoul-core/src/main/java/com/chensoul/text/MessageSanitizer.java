package com.chensoul.text;

/**
 * MessageSanitizer is responsible for sanitizing message.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@FunctionalInterface
public interface MessageSanitizer {
    /**
     * Sanitize string.
     *
     * @param msg the msg
     * @return the string
     */
    String sanitize(String msg);

    /**
     * None message sanitizer.
     *
     * @return the message sanitizer
     */
    static MessageSanitizer disabled() {
        return msg -> msg;
    }
}
