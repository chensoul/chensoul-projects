package com.chensoul.lang.function;

import com.chensoul.util.StringUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * A factory class for creating Function objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class FunctionUtils {
    public static final Consumer<Throwable> RETHROW_ALL = FunctionUtils::sneakyThrow;

    public static final Consumer<Throwable> THROWABLE_TO_RUNTIME_EXCEPTION = t -> {
        if (t instanceof Error)
            throw (Error) t;

        if (t instanceof RuntimeException)
            throw (RuntimeException) t;

        if (t instanceof IOException)
            throw new UncheckedIOException((IOException) t);

        if (t instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }

        throw new UncheckedException(t);
    };

    public static <E extends Throwable> void sneakyThrow(Throwable throwable) throws E {
        throw (E) throwable;
    }

    /**
     * @param predicate
     * @param trueFunction
     * @param falseFunction
     * @return {@link Function}<{@link T}, {@link R}>
     */
    public static <T, R> Function<T, R> doIf(final Predicate<T> predicate,
                                             final CheckedFunction<T, R> trueFunction,
                                             final CheckedFunction<T, R> falseFunction) {
        return t -> {
            try {
                if (predicate.test(t)) {
                    return trueFunction.apply(t);
                }
                return falseFunction.apply(t);
            } catch (final Throwable e) {
                log.warn("doIf error", e);
                try {
                    return falseFunction.apply(t);
                } catch (final Throwable ex) {
                    throw new IllegalArgumentException(ex.getMessage());
                }
            }
        };
    }


    /**
     * Do if consumer.
     *
     * @param condition     the condition
     * @param trueConsumer
     * @param falseConsumer
     * @return the consumer
     */
    public static <T> Consumer<T> doIf(final boolean condition,
                                       final Consumer<T> trueConsumer,
                                       final Consumer<T> falseConsumer) {
        return account -> {
            if (condition) {
                trueConsumer.accept(account);
            } else {
                falseConsumer.accept(account);
            }
        };
    }

    /**
     * Do if condition holds.
     *
     * @param <T>          the type parameter
     * @param condition    the condition
     * @param trueFunction the true function
     * @return the consumer
     */
    public static <T> Consumer<T> doIf(final boolean condition, final Consumer<T> trueFunction) {
        return doIf(condition, trueFunction, t -> {
        });
    }

    /**
     * Do if function.
     *
     * @param <R>           the type parameter
     * @param condition     the condition
     * @param trueSupplier  the true function
     * @param falseSupplier the false function
     * @return the function
     */
    public static <R> Supplier<R> doIf(final boolean condition,
                                       final Supplier<R> trueSupplier,
                                       final Supplier<R> falseSupplier) {
        return () -> {
            try {
                if (condition) {
                    return trueSupplier.get();
                }
                return falseSupplier.get();
            } catch (final Throwable e) {
                log.warn("doIf error", e);
                return falseSupplier.get();
            }
        };
    }

    public static <R> Supplier<R> doIf(final boolean condition,
                                       final Supplier<R> trueSupplier) {
        return doIf(condition, trueSupplier, () -> null);
    }

    public static <R> void doAndHandle(final CheckedConsumer<R> function) {
        try {
            function.accept(null);
        } catch (final Throwable e) {
            log.warn("doAndHandle error", e);
        }
    }

    public static <T, R> Function<T, R> doAndHandle(final CheckedFunction<T, R> function,
                                                    final CheckedFunction<Throwable, R> errorHandler) {
        return doAndHandle(function, errorHandler, null);
    }

    public static <T, R> Function<T, R> doAndHandle(final CheckedFunction<T, R> function,
                                                    final CheckedFunction<Throwable, R> errorHandler,
                                                    final CheckedConsumer<T> finalConsumer) {
        return t -> {
            try {
                return function.apply(t);
            } catch (final Throwable e) {
                log.warn("doAndHandle error", e);
                return CheckedFunction.unchecked(errorHandler).apply(e);
            } finally {
                if (finalConsumer != null) {
                    CheckedConsumer.unchecked(finalConsumer).accept(t);
                }
            }
        };
    }

    /**
     * Do and handle checked consumer.
     *
     * @param <R>          the type parameter
     * @param function     the function
     * @param errorHandler the error handler
     * @return the checked consumer
     */
    public static <R> Consumer<R> doAndHandle(final CheckedConsumer<R> function,
                                              final CheckedFunction<Throwable, R> errorHandler) {
        return t -> {
            try {
                function.accept(t);
            } catch (final Throwable e) {
                log.warn("doAndHandle error", e);
                CheckedFunction.unchecked(errorHandler).apply(e);
            }
        };
    }

    /**
     * Do and handle supplier.
     *
     * @param <R>          the type parameter
     * @param function     the function
     * @param errorHandler the error handler
     * @return the supplier
     */
    public static <R> Supplier<R> doAndHandle(final CheckedSupplier<R> function,
                                              final CheckedFunction<Throwable, R> errorHandler) {
        return () -> {
            try {
                return function.get();
            } catch (final Throwable e) {
                log.warn("doAndHandle error", e);
                return CheckedFunction.unchecked(errorHandler).apply(e);
            }
        };
    }

    /**
     * Throw if value is blank.
     *
     * @param value the value
     * @return the value
     * @throws Throwable the throwable
     */
    public static String throwIfBlank(final String value) throws Throwable {
        throwIf(StringUtils.isBlank(value), () -> new IllegalArgumentException("Value cannot be empty or blank"));
        return value;
    }

    /**
     * Throw if null.
     *
     * @param <T>     the type parameter
     * @param value   the value
     * @param handler the handler
     * @return the t
     * @throws Throwable the throwable
     */
    public static <T> T throwIfNull(final T value, final CheckedSupplier<Throwable> handler) throws Throwable {
        throwIf(value == null, handler);
        return value;
    }

    /**
     * Throw if.
     *
     * @param condition the condition
     * @param throwable the throwable
     * @throws Throwable the throwable
     */
    public static void throwIf(final boolean condition,
                               final CheckedSupplier<? extends Throwable> throwable) throws Throwable {
        if (condition) {
            throw throwable.get();
        }
    }
}
