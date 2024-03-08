package com.chensoul.lang.function;

import java.util.Objects;
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

    static <T, R> Function<T, R> sneaky(CheckedFunction<T, R> function) {
        return unchecked(function, FunctionUtils.SNEAKY_THROW);
    }

    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function) {
        return unchecked(function, FunctionUtils.CHECKED_THROW);
    }

    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function, Consumer<Throwable> handler) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

    default <V> CheckedFunction<V, R> compose(CheckedFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before is null");
        return t -> apply(before.apply(t));
    }

    default <V> CheckedFunction<T, V> andThen(CheckedFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return t -> after.apply(apply(t));
    }
}
