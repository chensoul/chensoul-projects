package com.chensoul.text;
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
