package com.chensoul.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
class DefaultMessageSanitizerTest {
    @Test
    public void testSanitizerText() throws Exception {
        DefaultMessageSanitizer sanitizer = new DefaultMessageSanitizer();
        String msg = "clientSecret=1234567890";
        String sanitizedMsg = sanitizer.sanitize(msg);
        assertEquals("clientSecret=******", sanitizedMsg);
    }

    @Test
    public void testSanitizerJson() throws Exception {
        DefaultMessageSanitizer sanitizer = new DefaultMessageSanitizer();
        String msg = "\"clientSecret\":\"1234567890\"";
        String sanitizedMsg = sanitizer.sanitize(msg);
        assertEquals("\"clientSecret\":\"******\"", sanitizedMsg);
    }

}
