package com.chensoul.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
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
