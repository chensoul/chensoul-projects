package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link java.lang.Runnable}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@FunctionalInterface
public interface CheckedRunnable {
    /**
     * <p>run.</p>
     *
     * @throws java.lang.Throwable if any.
     */
    void run() throws Throwable;

    /**
     * <p>andThen.</p>
     *
     * @param after a {@link com.chensoul.lang.function.CheckedRunnable} object
     * @return a {@link com.chensoul.lang.function.CheckedRunnable} object
     */
    default CheckedRunnable andThen(CheckedRunnable after) {
        Objects.requireNonNull(after, "after is null");
        return () -> {
            run();
            after.run();
        };
    }

    /**
     * <p>sneaky.</p>
     *
     * @param runnable a {@link com.chensoul.lang.function.CheckedRunnable} object
     * @return a {@link java.lang.Runnable} object
     */
    static Runnable sneaky(CheckedRunnable runnable) {
        return unchecked(runnable, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param runnable a {@link com.chensoul.lang.function.CheckedRunnable} object
     * @return a {@link java.lang.Runnable} object
     */
    static Runnable unchecked(CheckedRunnable runnable) {
        return unchecked(runnable, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param runnable a {@link com.chensoul.lang.function.CheckedRunnable} object
     * @param handler a {@link java.util.function.Consumer} object
     * @return a {@link java.lang.Runnable} object
     */
    static Runnable unchecked(CheckedRunnable runnable, Consumer<Throwable> handler) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

}
