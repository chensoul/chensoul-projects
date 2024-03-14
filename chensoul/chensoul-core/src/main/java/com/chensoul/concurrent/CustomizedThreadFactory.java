package com.chensoul.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Customized {@link java.util.concurrent.ThreadFactory}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
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
     * <p>Constructor for CustomizedThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     */
    protected CustomizedThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    /**
     * <p>Constructor for CustomizedThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @param daemon a boolean
     */
    protected CustomizedThreadFactory(String namePrefix, boolean daemon) {
        this(namePrefix, daemon, Thread.NORM_PRIORITY);
    }

    /**
     * <p>Constructor for CustomizedThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @param daemon a boolean
     * @param priority a int
     */
    protected CustomizedThreadFactory(String namePrefix, boolean daemon, int priority) {
        this(namePrefix, daemon, priority, 0);
    }

    /**
     * <p>Constructor for CustomizedThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @param daemon a boolean
     * @param priority a int
     * @param stackSize a long
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
     * <p>newThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @return {@link java.util.concurrent.ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix) {
        return new CustomizedThreadFactory(namePrefix);
    }

    /**
     * <p>newThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @param daemon a boolean
     * @return {@link java.util.concurrent.ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix, boolean daemon) {
        return new CustomizedThreadFactory(namePrefix, daemon);
    }

    /**
     * <p>newThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @param daemon a boolean
     * @param priority a int
     * @return {@link java.util.concurrent.ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix, boolean daemon, int priority) {
        return new CustomizedThreadFactory(namePrefix, daemon, priority);
    }

    /**
     * <p>newThreadFactory.</p>
     *
     * @param namePrefix a {@link java.lang.String} object
     * @param daemon a boolean
     * @param priority a int
     * @param stackSize a long
     * @return {@link java.util.concurrent.ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String namePrefix, boolean daemon, int priority, long stackSize) {
        return new CustomizedThreadFactory(namePrefix, daemon, priority, stackSize);
    }

    /** {@inheritDoc} */
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), stackSize);
        t.setDaemon(daemon);
        t.setPriority(priority);
        return t;
    }
}
