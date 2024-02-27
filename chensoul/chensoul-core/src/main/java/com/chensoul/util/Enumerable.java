package com.chensoul.util;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface Enumerable<T extends Enum<T> & Enumerable<T>> {
    Map<Class<?>, Enumerable[]> ENUM_CACHES = new ConcurrentHashMap<>();

    /**
     * @return
     */
    int getCode();

    /**
     * @return
     */
    String getName();

    static <T extends Enum<T> & Enumerable<T>> T getByCode(final Class<T> clazz, final int code, final Enumerable<T> defaultValue) {
        return (T) Arrays.stream(ENUM_CACHES.computeIfAbsent(clazz, t -> clazz.getEnumConstants()))
            .filter(e -> e.getCode() == code).findFirst().orElse(defaultValue);
    }

    static <T extends Enum<T> & Enumerable<T>> T getByName(final Class<T> clazz, final String name, final Enumerable<T> defaultValue) {
        return (T) Arrays.stream(ENUM_CACHES.computeIfAbsent(clazz, t -> clazz.getEnumConstants()))
            .filter(e -> name.equals(e.getName()))
            .findFirst().orElse(defaultValue);
    }

    static <T extends Enum<T> & Enumerable<T>> T getByCode(final Class<T> clazz, final int code) {
        return getByCode(clazz, code, null);
    }

    static <T extends Enum<T> & Enumerable<T>> T getByName(final Class<T> clazz, final String name) {
        return getByName(clazz, name, null);
    }
}
