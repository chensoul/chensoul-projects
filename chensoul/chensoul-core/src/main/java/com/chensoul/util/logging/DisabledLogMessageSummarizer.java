package com.chensoul.util.logging;

import org.slf4j.Logger;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DisabledLogMessageSummarizer implements LogMessageSummarizer {
    @Override
    public boolean shouldSummarize(final Logger logger) {
        return false;
    }
}
