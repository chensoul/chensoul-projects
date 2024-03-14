package com.chensoul.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DelegatingScheduledExecutorService implements ScheduledExecutorService {

    /**
     *
     */
    private volatile ScheduledExecutorService delegate;

    /**
     * @param delegate
     */
    public DelegatingScheduledExecutorService(ScheduledExecutorService delegate) {
        this.delegate = delegate;
    }

    /**
     * @return {@link ScheduledExecutorService}
     */
    public ScheduledExecutorService getDelegate() {
        return delegate;
    }

    /**
     * @param delegate
     */
    public void setDelegate(ScheduledExecutorService delegate) {
        this.delegate = delegate;
    }

    /**
     * @param command
     * @param delay
     * @param unit
     * @return {@link ScheduledFuture}<{@link ?}>
     */
    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return getDelegate().schedule(command, delay, unit);
    }

    /**
     * @param callable
     * @param delay
     * @param unit
     */
    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return getDelegate().schedule(callable, delay, unit);
    }

    /**
     * @param command
     * @param initialDelay
     * @param period
     * @param unit
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return getDelegate().scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    /**
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return getDelegate().scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    /**
     *
     */
    @Override
    public void shutdown() {
        getDelegate().shutdown();
    }

    /**
     */
    @Override
    public List<Runnable> shutdownNow() {
        return getDelegate().shutdownNow();
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isShutdown() {
        return getDelegate().isShutdown();
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isTerminated() {
        return getDelegate().isTerminated();
    }

    /**
     * @param timeout
     * @param unit
     * @return boolean
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return getDelegate().awaitTermination(timeout, unit);
    }

    /**
     * @param task
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return getDelegate().submit(task);
    }

    /**
     * @param task
     * @param result
     */
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return getDelegate().submit(task, result);
    }

    /**
     * @param task
     */
    @Override
    public Future<?> submit(Runnable task) {
        return getDelegate().submit(task);
    }

    /**
     * @param tasks
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return getDelegate().invokeAll(tasks);
    }

    /**
     * @param tasks
     * @param timeout
     * @param unit
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return getDelegate().invokeAll(tasks, timeout, unit);
    }

    /**
     * @param tasks
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return getDelegate().invokeAny(tasks);
    }

    /**
     * @param tasks
     * @param timeout
     * @param unit
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return getDelegate().invokeAny(tasks, timeout, unit);
    }

    /**
     * @param command
     */
    @Override
    public void execute(Runnable command) {
        getDelegate().execute(command);
    }
}
