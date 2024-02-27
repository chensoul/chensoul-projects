package com.chensoul.spring.util.io;

import java.io.Closeable;

/**
 * This is {@link WatcherService}.
 */
public interface WatcherService extends Closeable {

    /**
     * No op watcher service.
     *
     * @return the watcher service
     */
    static WatcherService noOp() {
        return new WatcherService() {
        };
    }

    /**
     * Close.
     */
    @Override
    default void close() {
    }

    /**
     * Start the watch.
     *
     * @param name the name
     */
    default void start(final String name) {
    }
}
