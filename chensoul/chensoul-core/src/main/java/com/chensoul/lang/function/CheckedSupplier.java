package com.chensoul.lang.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A {@link Supplier}-like interface which allows throwing Error.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Throwable;

    default <V> CheckedSupplier<V> andThen(CheckedFunction<? super T, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return () -> after.apply(get());
    }

    static <T> Supplier<T> sneaky(CheckedSupplier<T> supplier) {
        return unchecked(supplier, FunctionUtils.RETHROW_ALL);
    }

    static <T> Supplier<T> unchecked(CheckedSupplier<T> supplier) {
        return unchecked(supplier, FunctionUtils.THROWABLE_TO_RUNTIME_EXCEPTION);
    }

    static <T> Supplier<T> unchecked(CheckedSupplier<T> supplier, Consumer<Throwable> handler) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                handler.accept(e);
                throw new IllegalStateException("Exception handler must throw a RuntimeException", e);
            }
        };
    }

}
