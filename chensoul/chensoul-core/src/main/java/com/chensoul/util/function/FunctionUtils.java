package com.chensoul.util.function;

import com.chensoul.util.logging.LoggingUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

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
     * Do unchecked.
     *
     * @param <T>      the type parameter
     * @param consumer the consumer
     * @return the t
     */
    public static <T> T doUnchecked(final CheckedSupplier<T> consumer) {
        return CheckedSupplier.unchecked(consumer).get();
    }

    /**
     * Do unchecked.
     *
     * @param consumer the consumer
     * @param params   the params
     */
    public static void doUnchecked(final CheckedConsumer<Object> consumer, final Object... params) {
        CheckedConsumer.unchecked(consumer::accept).accept(params);
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
                LoggingUtils.warn(log, e);
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
                LoggingUtils.warn(log, e);
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
            LoggingUtils.warn(log, e);
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
                LoggingUtils.warn(log, e);
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
                LoggingUtils.warn(log, e);
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
                LoggingUtils.warn(log, e);
                return CheckedFunction.unchecked(errorHandler).apply(e);
            }
        };
    }

    /**
     * Do and retry.
     *
     * @param <T>      the type parameter
     * @param callback the callback
     * @return the t
     * @throws Exception the exception
     */
    public static <T> T doAndRetry(final RetryCallback<T, Exception> callback) throws Exception {
        return doAndRetry(new ArrayList<>(), callback);
    }

    /**
     * Do and retry.
     *
     * @param <T>      the type parameter
     * @param clazzes  the classified clazzes
     * @param callback the callback
     * @return the t
     * @throws Exception the exception
     */
    public static <T> T doAndRetry(final List<Class<? extends Throwable>> clazzes,
                                   final RetryCallback<T, Exception> callback) throws Exception {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(new FixedBackOffPolicy());

        Map<Class<? extends Throwable>, Boolean> classified = new HashMap<>();
        classified.put(Error.class, Boolean.TRUE);
        classified.put(Throwable.class, Boolean.TRUE);
        clazzes.forEach(clz -> classified.put(clz, Boolean.TRUE));

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(SimpleRetryPolicy.DEFAULT_MAX_ATTEMPTS, classified, true);
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setThrowLastExceptionOnExhausted(true);
        retryTemplate.registerListener(new RetryListenerSupport() {
            @Override
            public boolean open(final RetryContext context, final RetryCallback retryCallback) {
                context.setAttribute("retry.maxAttempts", retryPolicy.getMaxAttempts());
                return true;
            }
        });
        return retryTemplate.execute(callback);
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
