package com.chensoul.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Customized {@link ThreadFactory}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class CustomizedThreadFactory implements ThreadFactory {

    /**
     *
     */
    private final ThreadGroup group;

    /**
     *
     */
    private final AtomicInteger threadNumber;

    /**
     *
     */
    private final String namePrefix;

    /**
     *
     */
    private final boolean daemon;

    /**
     *
     */
    private final int priority;

    /**
     *
     */
    private final long stackSize;

    /**
     * @param namePrefix
     */
    protected CustomizedThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    /**
     * @param namePrefix
     * @param daemon
     */
    protected CustomizedThreadFactory(String namePrefix, boolean daemon) {
        this(namePrefix, daemon, Thread.NORM_PRIORITY);
    }

    /**
     * @param namePrefix
     * @param daemon
     * @param priority
     */
    protected CustomizedThreadFactory(String namePrefix, boolean daemon, int priority) {
        this(namePrefix, daemon, priority, 0);
    }

    /**
     * @param namePrefix
     * @param daemon
     * @param priority
     * @param stackSize
     */
    protected CustomizedThreadFactory(String namePrefix, boolean daemon, int priority, long stackSize) {
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() :
            Thread.currentThread().getThreadGroup();
        this.threadNumber = new AtomicInteger(1);
        this.namePrefix = namePrefix + "-thread-";
        this.daemon = daemon;
        this.priority = priority;
        this.stackSize = stackSize;
    }

    /**
     * @param namePrefix
     * @return {@link ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix) {
        return new CustomizedThreadFactory(namePrefix);
    }

    /**
     * @param namePrefix
     * @param daemon
     * @return {@link ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix, boolean daemon) {
        return new CustomizedThreadFactory(namePrefix, daemon);
    }

    /**
     * @param namePrefix
     * @param daemon
     * @param priority
     * @return {@link ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix, boolean daemon, int priority) {
        return new CustomizedThreadFactory(namePrefix, daemon, priority);
    }

    /**
     * @param namePrefix
     * @param daemon
     * @param priority
     * @param stackSize
     * @return {@link ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix, boolean daemon, int priority, long stackSize) {
        return new CustomizedThreadFactory(namePrefix, daemon, priority, stackSize);
    }

    /**
     * @param r
     * @return {@link Thread}
     */
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), stackSize);
        t.setDaemon(daemon);
        t.setPriority(priority);
        return t;
    }
}
