package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A {@link java.util.function.Consumer}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@FunctionalInterface
public interface CheckedConsumer<T> {
    /**
     * <p>accept.</p>
     *
     * @param t a T object
     * @throws java.lang.Throwable if any.
     */
    void accept(T t) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedConsumer} object
     * @param <T> a T class
     * @return a {@link java.util.function.Consumer} object
     */
    static <T> Consumer<T> sneaky(CheckedConsumer<T> consumer) {
        return unchecked(consumer, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedConsumer} object
     * @param <T> a T class
     * @return a {@link java.util.function.Consumer} object
     */
    static <T> Consumer<T> unchecked(CheckedConsumer<T> consumer) {
        return unchecked(consumer, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>andThen.</p>
     *
     * @param after a {@link com.chensoul.lang.function.CheckedConsumer} object
     * @return a {@link com.chensoul.lang.function.CheckedConsumer} object
     */
    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after, "after is null");
        return t -> {
            accept(t);
            after.accept(t);
        };
    }

    /**
     * <p>unchecked.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedConsumer} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @return a {@link java.util.function.Consumer} object
     */
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
