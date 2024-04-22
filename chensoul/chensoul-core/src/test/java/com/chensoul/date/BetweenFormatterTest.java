package com.chensoul.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class BetweenFormatterTest {

    @Test
    public void testFormat() {
        BetweenFormatter formatter = new BetweenFormatter(1461 * 24 * 60 * 60 * 1000L, BetweenFormatter.Level.DAY);
        String result = formatter.format();
        assertNotNull(result);
        assertEquals("1461天", result);
    }
}
