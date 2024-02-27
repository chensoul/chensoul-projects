package com.chensoul.util.logging;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * This is {@link LogMessageSummarizerTests}.
 */
@Tag("Utility")
@Slf4j
class LogMessageSummarizerTests {
    @Test
    void verifyOperation() throws Throwable {
        val summarizer = new DisabledLogMessageSummarizer();
        assertFalse(summarizer.shouldSummarize(log));
        assertTrue(summarizer.summarizeStackTrace("Message", new IllegalArgumentException("Error")).isEmpty());
    }

    @Test
    void verifyExceptionMessageIsNull() throws Throwable {
        val defaultLogMessageSummarizer = new DefaultLogMessageSummarizer();
        assertNotNull(defaultLogMessageSummarizer.summarizeStackTrace(null, new IllegalArgumentException()));
    }
}


