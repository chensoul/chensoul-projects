package com.chensoul.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link Consumer}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws Throwable;

    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after, "after is null");
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }

    static <T> Consumer<T> sneaky(CheckedConsumer<T> consumer) {
        return unchecked(consumer, FunctionUtils.RETHROW_ALL);
    }

    static <T> Consumer<T> unchecked(CheckedConsumer<T> consumer) {
        return unchecked(consumer, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    static <T> Consumer<T> unchecked(CheckedConsumer<T> consumer, Consumer<Throwable> handler) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
