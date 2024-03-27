package com.chensoul.lang.function;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A {@link java.util.function.BiPredicate} that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@FunctionalInterface
public interface CheckedBiPredicate<T, U> {

    /**
     * <p>test.</p>
     *
     * @param t a T object
     * @param u a U object
     * @return a boolean
     * @throws java.lang.Throwable if any.
     */
    boolean test(T t, U u) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedBiPredicate} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiPredicate} object
     */
    static <T, U> BiPredicate<T, U> sneaky(CheckedBiPredicate<T, U> predicate) {
        return unchecked(predicate, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedBiPredicate} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiPredicate} object
     */
    static <T, U> BiPredicate<T, U> unchecked(CheckedBiPredicate<T, U> predicate) {
        return unchecked(predicate, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedBiPredicate} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @param <U> a U class
     * @return a {@link java.util.function.BiPredicate} object
     */
    static <T, U> BiPredicate<T, U> unchecked(CheckedBiPredicate<T, U> predicate, Consumer<Throwable> handler) {
        return (t, u) -> {
            try {
                return predicate.test(t, u);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
