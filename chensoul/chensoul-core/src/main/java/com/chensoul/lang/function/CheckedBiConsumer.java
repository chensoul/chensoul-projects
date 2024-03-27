package com.chensoul.lang.function;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A {@link java.util.function.BiConsumer} that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@FunctionalInterface
public interface CheckedBiConsumer<T, U> {

    /**
     * <p>accept.</p>
     *
     * @param t a T object
     * @param u a U object
     * @throws java.lang.Throwable if any.
     */
    void accept(T t, U u) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedBiConsumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiConsumer} object
     */
    static <T, U> BiConsumer<T, U> sneaky(final CheckedBiConsumer<T, U> consumer) {
        return unchecked(consumer, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedBiConsumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiConsumer} object
     */
    static <T, U> BiConsumer<T, U> unchecked(final CheckedBiConsumer<T, U> consumer) {
        return unchecked(consumer, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param consumer a {@link com.chensoul.lang.function.CheckedBiConsumer} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiConsumer} object
     */
    static <T, U> BiConsumer<T, U> unchecked(final CheckedBiConsumer<T, U> consumer, final Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            } catch (final Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
