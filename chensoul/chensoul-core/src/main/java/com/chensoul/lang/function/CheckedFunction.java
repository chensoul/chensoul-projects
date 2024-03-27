package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link java.util.function.Function}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {
    /**
     * <p>apply.</p>
     *
     * @param t a T object
     * @return a R object
     * @throws java.lang.Throwable if any.
     */
    R apply(T t) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param function a {@link com.chensoul.lang.function.CheckedFunction} object
     * @param <T> a T class
     * @param <R> a R class
     * @return a {@link java.util.function.Function} object
     */
    static <T, R> Function<T, R> sneaky(CheckedFunction<T, R> function) {
        return unchecked(function, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param function a {@link com.chensoul.lang.function.CheckedFunction} object
     * @param <T> a T class
     * @param <R> a R class
     * @return a {@link java.util.function.Function} object
     */
    static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function) {
        return unchecked(function, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param function a {@link com.chensoul.lang.function.CheckedFunction} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @param <R> a R class
     * @return a {@link java.util.function.Function} object
     */
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

    /**
     * <p>compose.</p>
     *
     * @param before a {@link com.chensoul.lang.function.CheckedFunction} object
     * @param <V> a V class
     * @return a {@link com.chensoul.lang.function.CheckedFunction} object
     */
    default <V> CheckedFunction<V, R> compose(CheckedFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before is null");
        return t -> apply(before.apply(t));
    }

    /**
     * <p>andThen.</p>
     *
     * @param after a {@link com.chensoul.lang.function.CheckedFunction} object
     * @param <V> a V class
     * @return a {@link com.chensoul.lang.function.CheckedFunction} object
     */
    default <V> CheckedFunction<T, V> andThen(CheckedFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return t -> after.apply(apply(t));
    }
}
