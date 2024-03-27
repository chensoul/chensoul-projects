package com.chensoul.reflect;

import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.util.ShutdownHookUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClassLoaderUtils {
    private static final ConcurrentMap<String, Class<?>> loadedClassesCache = initLoadedClassesCache();

    /**
     * Initialize the cache of loaded classes and add a shutdown hook to clear the cache
     *
     * @return {@link ConcurrentMap}<{@link String}, >
     */
    private static ConcurrentMap<String, Class<?>> initLoadedClassesCache() {
        ConcurrentMap<String, Class<?>> loadedClassesCache = new ConcurrentHashMap<>(256);
        ShutdownHookUtils.addShutdownHookCallback(loadedClassesCache::clear);
        return loadedClassesCache;
    }

    /**
     * Get the default {@link java.lang.ClassLoader}
     *
     * @return {@link java.lang.ClassLoader}
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }

        if (classLoader == null) {
            // If the ClassLoader is also not found, try to get the ClassLoader from the Caller class
            Class<?> callerClass = ReflectionUtils.getCallerClass(3);
            if (callerClass != null) {
                classLoader = callerClass.getClassLoader();
            }
        }

        if (classLoader == null) {
            // If the ClassLoader is also not found, try to get the ClassLoader from the current class
            classLoader = ClassLoaderUtils.class.getClassLoader();
        }

        if (classLoader == null) {
            // classLoader is null indicates the bootstrap ClassLoader
            try {
                classLoader = ClassLoader.getSystemClassLoader();
            } catch (Throwable ignored) {
            }
        }
        return classLoader;
    }

    /**
     * <p>getClassLoader.</p>
     *
     * @param loadedClass a {@link java.lang.Class} object
     * @return {@link java.lang.ClassLoader}
     */
    public static ClassLoader getClassLoader(Class<?> loadedClass) {
        ClassLoader classLoader = null;
        try {
            if (loadedClass == null) {
                classLoader = getCallerClassLoader(4);
            } else {
                classLoader = loadedClass.getClassLoader();
            }
        } catch (SecurityException ignored) {
        }
        return classLoader == null ? getDefaultClassLoader() : classLoader;
    }

    /**
     * <p>getCallerClassLoader.</p>
     *
     * @return {@link java.lang.ClassLoader}
     */
    public static ClassLoader getCallerClassLoader() {
        return getCallerClassLoader(4);
    }

    /**
     * Return the ClassLoader from the caller class
     *
     * @param invocationFrame
     * @return the ClassLoader (only {@code null} if the caller class was absent
     * @see ReflectionUtils#getCallerClass()
     */
    private static ClassLoader getCallerClassLoader(int invocationFrame) {
        ClassLoader classLoader = null;
        Class<?> callerClass = ReflectionUtils.getCallerClass(invocationFrame);
        if (callerClass != null) {
            classLoader = callerClass.getClassLoader();
        }
        return classLoader;
    }

    /**
     * <p>resolveClass.</p>
     *
     * @param className a {@link java.lang.String} object
     * @return a {@link java.lang.Class} object
     */
    public static Class<?> resolveClass(String className) {
        return resolveClass(className, getDefaultClassLoader());
    }

    /**
     * Resolve the {@link java.lang.Class} by the specified name and {@link java.lang.ClassLoader}
     *
     * @param className   the name of {@link java.lang.Class}
     * @param classLoader a {@link java.lang.ClassLoader} object
     * @return If can't be resolved , return <code>null</code>
     */
    public static Class<?> resolveClass(String className, ClassLoader classLoader) {
        return resolveClass(className, classLoader, false);
    }

    /**
     * Resolve the {@link java.lang.Class} by the specified name and {@link java.lang.ClassLoader}
     *
     * @param className   the name of {@link java.lang.Class}
     * @param cached      the resolved class is required to be cached or not
     * @param classLoader a {@link java.lang.ClassLoader} object
     * @return If can't be resolved , return <code>null</code>
     */
    public static Class<?> resolveClass(String className, ClassLoader classLoader, boolean cached) {
        if (className == null) {
            return null;
        }

        Class<?> targetClass = null;
        try {
            ClassLoader targetClassLoader = classLoader == null ? getDefaultClassLoader() : classLoader;
            targetClass = loadClass(className, targetClassLoader, cached);
        } catch (Throwable ignored) { // Ignored
        }
        return targetClass;
    }

    /**
     * <p>loadClass.</p>
     *
     * @param className a {@link java.lang.String} object
     * @param classLoader a {@link java.lang.ClassLoader} object
     * @return a {@link java.lang.Class} object
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        try {
            return classLoader.loadClass(className);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * Loaded specified class name under {@link java.lang.ClassLoader}
     *
     * @param className   the name of {@link java.lang.Class}
     * @param cached      the resolved class is required to be cached or not
     * @param classLoader a {@link java.lang.ClassLoader} object
     * @return a {@link java.lang.Class} object
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader, boolean cached) {
        Class loadedClass = null;
        if (cached) {
            String cacheKey = buildCacheKey(className, classLoader);
            loadedClass = loadedClassesCache.computeIfAbsent(cacheKey, k -> CheckedSupplier.unchecked(() -> loadClass(className, classLoader)).get());
        } else {
            loadedClass = loadClass(className, classLoader);
        }
        return loadedClass;
    }

    /**
     * @param className
     * @param classLoader
     * @return {@link String}
     */
    private static String buildCacheKey(String className, ClassLoader classLoader) {
        return className + classLoader.hashCode();
    }
}
