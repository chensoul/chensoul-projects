package com.chensoul.util;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Enumerable interface.</p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public interface Enumerable<T extends Enum<T> & Enumerable<T>> {
    /** Constant <code>ENUM_CACHES</code> */
    Map<Class<?>, Enumerable[]> ENUM_CACHES = new ConcurrentHashMap<>();

    /**
     * <p>getCode.</p>
     *
     * @return a int
     */
    int getCode();

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object
     */
    String getName();

    /**
     * <p>getByCode.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @param code a int
     * @param defaultValue a {@link com.chensoul.util.Enumerable} object
     * @param <T> a T class
     * @return a T object
     */
    static <T extends Enum<T> & Enumerable<T>> T getByCode(final Class<T> clazz, final int code, final Enumerable<T> defaultValue) {
        return (T) Arrays.stream(ENUM_CACHES.computeIfAbsent(clazz, t -> clazz.getEnumConstants()))
            .filter(e -> e.getCode() == code).findFirst().orElse(defaultValue);
    }

    /**
     * <p>getByName.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @param name a {@link java.lang.String} object
     * @param defaultValue a {@link com.chensoul.util.Enumerable} object
     * @param <T> a T class
     * @return a T object
     */
    static <T extends Enum<T> & Enumerable<T>> T getByName(final Class<T> clazz, final String name, final Enumerable<T> defaultValue) {
        return (T) Arrays.stream(ENUM_CACHES.computeIfAbsent(clazz, t -> clazz.getEnumConstants()))
            .filter(e -> name.equals(e.getName()))
            .findFirst().orElse(defaultValue);
    }

    /**
     * <p>getByCode.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @param code a int
     * @param <T> a T class
     * @return a T object
     */
    static <T extends Enum<T> & Enumerable<T>> T getByCode(final Class<T> clazz, final int code) {
        return getByCode(clazz, code, null);
    }

    /**
     * <p>getByName.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @param name a {@link java.lang.String} object
     * @param <T> a T class
     * @return a T object
     */
    static <T extends Enum<T> & Enumerable<T>> T getByName(final Class<T> clazz, final String name) {
        return getByName(clazz, name, null);
    }
}
