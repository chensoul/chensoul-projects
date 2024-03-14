package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A {@link java.util.function.Predicate}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
@FunctionalInterface
public interface CheckedPredicate<T> {
    /**
     * <p>test.</p>
     *
     * @param t a T object
     * @return a boolean
     * @throws java.lang.Throwable if any.
     */
    boolean test(T t) throws Throwable;

    /**
     * <p>negate.</p>
     *
     * @return a {@link com.chensoul.lang.function.CheckedPredicate} object
     */
    default CheckedPredicate<T> negate() {
        return t -> !test(t);
    }

    /**
     * <p>andThen.</p>
     *
     * @param other a {@link com.chensoul.lang.function.CheckedPredicate} object
     * @return a {@link com.chensoul.lang.function.CheckedPredicate} object
     */
    default CheckedPredicate<T> andThen(CheckedPredicate<T> other) {
        Objects.requireNonNull(other, "other is null");
        return (T t) -> test(t) && other.test(t);
    }

    /**
     * <p>or.</p>
     *
     * @param other a {@link com.chensoul.lang.function.CheckedPredicate} object
     * @return a {@link com.chensoul.lang.function.CheckedPredicate} object
     */
    default CheckedPredicate<T> or(CheckedPredicate<? super T> other) {
        Objects.requireNonNull(other, "other is null");
        return (t) -> test(t) || other.test(t);
    }

    /**
     * <p>sneaky.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedPredicate} object
     * @param <T> a T class
     * @return a {@link java.util.function.Predicate} object
     */
    static <T> Predicate<T> sneaky(CheckedPredicate predicate) {
        return unchecked(predicate, FunctionUtils.SNEAKY_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedPredicate} object
     * @param <T> a T class
     * @return a {@link java.util.function.Predicate} object
     */
    static <T> Predicate<T> unchecked(CheckedPredicate predicate) {
        return unchecked(predicate, FunctionUtils.CHECKED_THROW);
    }

    /**
     * <p>unchecked.</p>
     *
     * @param predicate a {@link com.chensoul.lang.function.CheckedPredicate} object
     * @param handler a {@link java.util.function.Consumer} object
     * @param <T> a T class
     * @return a {@link java.util.function.Predicate} object
     */
    static <T> Predicate<T> unchecked(CheckedPredicate predicate, Consumer<Throwable> handler) {
        return t -> {
            try {
                return predicate.test(t);
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

}
