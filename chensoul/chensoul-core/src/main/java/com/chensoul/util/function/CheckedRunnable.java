package com.chensoul.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link Runnable}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Throwable;

    default CheckedRunnable andThen(CheckedRunnable after) {
        Objects.requireNonNull(after, "after is null");
        return () -> {
            run();
            after.run();
        };
    }

    static Runnable sneaky(CheckedRunnable runnable) {
        return unchecked(runnable, FunctionUtils.RETHROW_ALL);
    }

    static Runnable unchecked(CheckedRunnable runnable) {
        return unchecked(runnable, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

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
