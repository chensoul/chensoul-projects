package com.chensoul.util;

import java.time.Duration;

/**
 * ThreadUtils provides utility methods and decorators for {@code Thread} objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class ThreadUtils {
    public static void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Puts a thread to sleep forever.
     */
    public static void sleep() {
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleep(final Duration duration) {
        sleep(duration.toMillis());
    }
}
