package com.chensoul.lang.function;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Async functions
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class Async {
    private Async() {

    }

    /**
     * <p>supplyAsync.</p>
     *
     * @param supplier a {@link java.util.function.Supplier} object
     * @param <U> a U class
     * @return a {@link java.util.concurrent.CompletionStage} object
     */
    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier) {
        return SameExecutorCompletionStage.of(CompletableFuture.supplyAsync(supplier), null);
    }

    /**
     * <p>supplyAsync.</p>
     *
     * @param supplier a {@link java.util.function.Supplier} object
     * @param executor a {@link java.util.concurrent.Executor} object
     * @param <U> a U class
     * @return a {@link java.util.concurrent.CompletionStage} object
     */
    public static <U> CompletionStage<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return SameExecutorCompletionStage.of(CompletableFuture.supplyAsync(supplier, executor), executor);
    }

    /**
     * <p>runAsync.</p>
     *
     * @param runnable a {@link java.lang.Runnable} object
     * @param executor a {@link java.util.concurrent.Executor} object
     * @return a {@link java.util.concurrent.CompletionStage} object
     */
    public static CompletionStage<Void> runAsync(Runnable runnable, Executor executor) {
        return SameExecutorCompletionStage.of(CompletableFuture.runAsync(runnable, executor), executor);
    }

    /**
     * <p>runAsync.</p>
     *
     * @param runnable a {@link java.lang.Runnable} object
     * @return a {@link java.util.concurrent.CompletionStage} object
     */
    public static CompletionStage<Void> runAsync(Runnable runnable) {
        return SameExecutorCompletionStage.of(CompletableFuture.runAsync(runnable), null);
    }
}
