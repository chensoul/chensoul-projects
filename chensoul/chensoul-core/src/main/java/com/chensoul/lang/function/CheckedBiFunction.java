package com.chensoul.lang.function;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * A {@link BiFunction} that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedBiFunction<T, U, R> {
    R apply(T t, U u) throws Throwable;

    static <T, U, R> BiFunction<T, U, R> sneaky(CheckedBiFunction<T, U, R> consumer) {
        return unchecked(consumer, FunctionUtils.RETHROW_ALL);
    }

    static <T, U, R> BiFunction<T, U, R> unchecked(CheckedBiFunction<T, U, R> consumer) {
        return unchecked(consumer, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    static <T, U, R> BiFunction<T, U, R> unchecked(CheckedBiFunction<T, U, R> consumer, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return consumer.apply(t, u);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
