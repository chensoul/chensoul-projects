package com.chensoul.util;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * The utilities class for Java {@link java.util.Map}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class Maps {

    /**
     * The min load factor for {@link HashMap} or {@link Hashtable}
     */
    public static final float MIN_LOAD_FACTOR = Float.MIN_NORMAL;

    /**
     * <p>isEmpty.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * <p>isNotEmpty.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * <p>of.</p>
     *
     * @param key   a K object
     * @param value a V object
     * @param <K>   a K class
     * @param <V>   a V class
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        return singletonMap(key, value);
    }

    /**
     * <p>of.</p>
     *
     * @param key1   a K object
     * @param value1 a V object
     * @param key2   a K object
     * @param value2 a V object
     * @param <K>    a K class
     * @param <V>    a V class
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2) {
        return ofMap(key1, value1, key2, value2);
    }

    /**
     * <p>of.</p>
     *
     * @param key1   a K object
     * @param value1 a V object
     * @param key2   a K object
     * @param value2 a V object
     * @param key3   a K object
     * @param value3 a V object
     * @param <K>    a K class
     * @param <V>    a V class
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        return ofMap(key1, value1, key2, value2, key3, value3);
    }

    /**
     * <p>of.</p>
     *
     * @param key1   a K object
     * @param value1 a V object
     * @param key2   a K object
     * @param value2 a V object
     * @param key3   a K object
     * @param value3 a V object
     * @param key4   a K object
     * @param value4 a V object
     * @param <K>    a K class
     * @param <V>    a V class
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    /**
     * <p>of.</p>
     *
     * @param key1   a K object
     * @param value1 a V object
     * @param key2   a K object
     * @param value2 a V object
     * @param key3   a K object
     * @param value3 a V object
     * @param key4   a K object
     * @param value4 a V object
     * @param key5   a K object
     * @param value5 a V object
     * @param <K>    a K class
     * @param <V>    a V class
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4, key5, value5);
    }

    /**
     * <p>of.</p>
     *
     * @param values a {@link java.lang.Object} object
     * @return {@link java.util.Map}
     */
    public static Map of(Object... values) {
        return ofMap(values);
    }

    /**
     * <p>ofMap.</p>
     *
     * @param keyValuePairs a {@link java.lang.Object} object
     * @return {@link java.util.Map}
     */
    public static Map ofMap(Object... keyValuePairs) {
        int length = keyValuePairs.length;
        Map map = new HashMap(length / 2, MIN_LOAD_FACTOR);
        for (int i = 0; i < length; ) {
            map.put(keyValuePairs[i++], keyValuePairs[i++]);
        }
        return unmodifiableMap(map);
    }
}
