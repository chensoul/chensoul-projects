package com.chensoul.util;

import java.time.Duration;

/**
 * ThreadUtils provides utility methods and decorators for {@code Thread} objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class ThreadUtils {
    /**
     * <p>sleep.</p>
     *
     * @param ms a long
     */
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

    /**
     * <p>sleep.</p>
     *
     * @param duration a {@link java.time.Duration} object
     */
    public static void sleep(final Duration duration) {
        sleep(duration.toMillis());
    }
}
