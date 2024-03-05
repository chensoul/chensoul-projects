package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link Function}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Throwable;

    default R execute(T t) throws RuntimeException {
        return execute(t, this::handleException);
    }

    default R execute(T t, BiFunction<T, Throwable, R> exceptionHandler) throws RuntimeException {
        R result = null;
        try {
            result = apply(t);
        } catch (Throwable e) {
            result = exceptionHandler.apply(t, e);
        }
        return result;
    }

    default R handleException(T t, Throwable failure) {
        throw new RuntimeException(failure);
    }

    default <V> CheckedFunction<V, R> compose(CheckedFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before is null");
        return (V v) -> apply(before.apply(v));
    }

    default <V> CheckedFunction<T, V> andThen(CheckedFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (T t) -> after.apply(apply(t));
    }

    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function) {
        return unchecked(function, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function, Consumer<Throwable> handler) {
        return t1 -> {
            try {
                return function.apply(t1);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
