package com.chensoul.util;

import static com.chensoul.lang.Prioritized.COMPARATOR;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toSet;

import com.chensoul.reflect.ClassLoaderUtils;
import com.chensoul.reflect.FieldUtils;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Predicate;

/**
 * The utilities class for ShutdownHook
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @see java.lang.ApplicationShutdownHooks
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class ShutdownHookUtils {

    /**
     * The System property name of the capacity of ShutdownHook callbacks
     */
    public static final String SHUTDOWN_HOOK_CALLBACKS_CAPACITY_PROPERTY_NAME = "chensoul.shutdown-hook.callbacks-capacity";

    /**
     * The System property value of the capacity of ShutdownHook callbacks, the default value is 512
     */
    public static final int SHUTDOWN_HOOK_CALLBACKS_CAPACITY = Integer.getInteger(SHUTDOWN_HOOK_CALLBACKS_CAPACITY_PROPERTY_NAME, 512);

    private static final PriorityBlockingQueue<Runnable> shutdownHookCallbacks = new PriorityBlockingQueue<>(SHUTDOWN_HOOK_CALLBACKS_CAPACITY, COMPARATOR);

    private static final String TARGET_CLASS_NAME = "java.lang.ApplicationShutdownHooks";

    private static final Class<?> TARGET_CLASS = ClassLoaderUtils.resolveClass(TARGET_CLASS_NAME, getSystemClassLoader());

    private static final Field HOOKS_FIELD = FieldUtils.findField(TARGET_CLASS, "hooks");

    private static final IdentityHashMap<Thread, Thread> hooksRef = findHooks();

    static {
        Runtime.getRuntime().addShutdownHook(new ShutdownHookCallbacksThread());
    }

    private static IdentityHashMap<Thread, Thread> findHooks() {
        return FieldUtils.getStaticFieldValue(HOOKS_FIELD);
    }

    /**
     * Get the shutdown hooks' threads that was added
     *
     * @return non-null
     */
    public static Set<Thread> getShutdownHookThreads() {
        return filterShutdownHookThreads(t -> true);
    }

    /**
     * <p>filterShutdownHookThreads.</p>
     *
     * @param hookThreadFilter a {@link java.util.function.Predicate} object
     * @return a {@link java.util.Set} object
     */
    public static Set<Thread> filterShutdownHookThreads(Predicate<Thread> hookThreadFilter) {
        return filterShutdownHookThreads(hookThreadFilter, false);
    }

    /**
     * <p>filterShutdownHookThreads.</p>
     *
     * @param hookThreadFilter a {@link java.util.function.Predicate} object
     * @param removed a boolean
     * @return a {@link java.util.Set} object
     */
    public static Set<Thread> filterShutdownHookThreads(Predicate<Thread> hookThreadFilter, boolean removed) {
        if (hooksRef == null || hooksRef.isEmpty()) {
            return emptySet();
        }

        Set<Thread> hookThreads = hooksRef.keySet().stream().filter(hookThreadFilter).collect(toSet());
        if (removed) {
            hookThreads.forEach(hooksRef::remove);
        }
        return hookThreads;
    }

    /**
     * Add the Shutdown Hook Callback
     *
     * @param callback the {@link java.lang.Runnable} callback
     * @return <code>true</code> if the specified Shutdown Hook Callback added, otherwise <code>false</code>
     */
    public static boolean addShutdownHookCallback(Runnable callback) {
        boolean added = false;
        if (callback != null) {
            added = shutdownHookCallbacks.add(callback);
        }
        return added;
    }

    /**
     * Remove the Shutdown Hook Callback
     *
     * @param callback the {@link java.lang.Runnable} callback
     * @return <code>true</code> if the specified Shutdown Hook Callback removed, otherwise <code>false</code>
     */
    public static boolean removeShutdownHookCallback(Runnable callback) {
        boolean removed = false;
        if (callback != null) {
            removed = shutdownHookCallbacks.remove(callback);
        }
        return removed;
    }

    /**
     * Get all Shutdown Hook Callbacks
     *
     * @return non-null
     */
    public static Collection<Runnable> getShutdownHookCallbacks() {
        return unmodifiableCollection(shutdownHookCallbacks);
    }

    private static class ShutdownHookCallbacksThread extends Thread {

        public ShutdownHookCallbacksThread() {
            setName("ShutdownHookCallbacksThread");
        }

        @Override
        public void run() {
            executeShutdownHookCallbacks();
            clearShutdownHookCallbacks();
        }

        private void executeShutdownHookCallbacks() {
            for (Runnable callback : shutdownHookCallbacks) {
                callback.run();
            }
        }

        private void clearShutdownHookCallbacks() {
            shutdownHookCallbacks.clear();
        }
    }

}
