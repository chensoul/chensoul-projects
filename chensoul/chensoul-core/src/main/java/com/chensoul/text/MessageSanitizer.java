package com.chensoul.text;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface MessageSanitizer {
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
