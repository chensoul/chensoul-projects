package com.chensoul.util.logging;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DefaultLogMessageSummarizer implements LogMessageSummarizer {

    private static final int LINES_TO_SUMMARIZE = 30;

    @Override
    public boolean shouldSummarize(final Logger logger) {
        return !logger.isDebugEnabled();
    }

    @Override
    public String summarizeStackTrace(final String message, final Throwable throwable) {
        Throwable rootCause = throwable.getCause();
        if (rootCause == null) {
            rootCause = throwable;
        }
        StringBuilder builder = new StringBuilder(StringUtils.defaultIfBlank(message, rootCause.toString())).append('\n');
        Arrays.stream(throwable.getStackTrace()).limit(LINES_TO_SUMMARIZE).forEach(trace -> {
            builder.append(trace);
            builder.append("\n");
        });
        return builder.toString();
    }
}
