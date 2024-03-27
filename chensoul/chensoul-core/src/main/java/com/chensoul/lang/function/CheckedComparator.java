package com.chensoul.lang.function;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * A {@link java.util.Comparator}-like that allows for checked exceptions.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 *
 */
@FunctionalInterface
public interface CheckedComparator<T> {

    /**
     * Compares its two arguments for order.
     *
     * @param o1 a T object
     * @param o2 a T object
     * @return a int
     * @throws java.lang.Throwable if any.
     */
    int compare(T o1, T o2) throws Throwable;

    /**
     * <p>sneaky.</p>
     *
     * @param callable a {@link com.chensoul.lang.function.CheckedComparator} object
     * @param <T> a T class
     * @return a {@link java.util.Comparator} object
     */
    static <T> Comparator<T> sneaky(CheckedComparator<T> callable) {
        return unchecked(callable, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param callable a {@link com.chensoul.lang.function.CheckedComparator} object
     * @param <T> a T class
     * @return a {@link java.util.Comparator} object
     */
    static <T> Comparator<T> unchecked(CheckedComparator<T> callable) {
        return unchecked(callable, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param callable a {@link com.chensoul.lang.function.CheckedComparator} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @return a {@link java.util.Comparator} object
     */
    static <T> Comparator<T> unchecked(CheckedComparator<T> callable, Consumer<Throwable> handler) {
        return (t1, t2) -> {
            try {
                return callable.compare(t1, t2);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }
}
