package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A {@link Predicate}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedPredicate<T> {
    boolean test(T t) throws Throwable;

    default CheckedPredicate<T> negate() {
        return t -> !test(t);
    }

    default CheckedPredicate<T> andThen(CheckedPredicate<T> other) {
        Objects.requireNonNull(other, "other is null");
        return (T t) -> test(t) && other.test(t);
    }

    default CheckedPredicate<T> or(CheckedPredicate<? super T> other) {
        Objects.requireNonNull(other, "other is null");
        return (t) -> test(t) || other.test(t);
    }

    static <T> Predicate<T> sneaky(CheckedPredicate predicate) {
        return unchecked(predicate, FunctionUtils.RETHROW_ALL);
    }

    static <T> Predicate<T> unchecked(CheckedPredicate predicate) {
        return unchecked(predicate, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

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
