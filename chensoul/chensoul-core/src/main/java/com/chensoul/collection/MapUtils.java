package com.chensoul.collection;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

/**
 * The utilities class for Java {@link java.util.Map}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class MapUtils {

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
     * @param key a K object
     * @param value a V object
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        return singletonMap(key, value);
    }

    /**
     * <p>of.</p>
     *
     * @param key1 a K object
     * @param value1 a V object
     * @param key2 a K object
     * @param value2 a V object
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2) {
        return ofMap(key1, value1, key2, value2);
    }

    /**
     * <p>of.</p>
     *
     * @param key1 a K object
     * @param value1 a V object
     * @param key2 a K object
     * @param value2 a V object
     * @param key3 a K object
     * @param value3 a V object
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        return ofMap(key1, value1, key2, value2, key3, value3);
    }

    /**
     * <p>of.</p>
     *
     * @param key1 a K object
     * @param value1 a V object
     * @param key2 a K object
     * @param value2 a V object
     * @param key3 a K object
     * @param value3 a V object
     * @param key4 a K object
     * @param value4 a V object
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    /**
     * <p>of.</p>
     *
     * @param key1 a K object
     * @param value1 a V object
     * @param key2 a K object
     * @param value2 a V object
     * @param key3 a K object
     * @param value3 a V object
     * @param key4 a K object
     * @param value4 a V object
     * @param key5 a K object
     * @param value5 a V object
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
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
        for (int i = 0; i < length;) {
            map.put(keyValuePairs[i++], keyValuePairs[i++]);
        }
        return unmodifiableMap(map);
    }

    /**
     * <p>newHashMap.</p>
     *
     * @return {@link java.util.HashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * <p>newHashMap.</p>
     *
     * @param initialCapacity a int
     * @return {@link java.util.HashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> HashMap<K, V> newHashMap(int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }

    /**
     * <p>newHashMap.</p>
     *
     * @param initialCapacity a int
     * @param loadFactor a float
     * @return {@link java.util.HashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> HashMap<K, V> newHashMap(int initialCapacity, float loadFactor) {
        return new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * <p>newHashMap.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return {@link java.util.HashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<>(map);
    }

    /**
     * <p>newLinkedHashMap.</p>
     *
     * @return {@link java.util.LinkedHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    /**
     * <p>newLinkedHashMap.</p>
     *
     * @param initialCapacity a int
     * @return {@link java.util.LinkedHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int initialCapacity) {
        return new LinkedHashMap<>(initialCapacity);
    }

    /**
     * <p>newLinkedHashMap.</p>
     *
     * @param initialCapacity a int
     * @param loadFactor a float
     * @return {@link java.util.LinkedHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int initialCapacity,
        float loadFactor) {
        return new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * <p>newLinkedHashMap.</p>
     *
     * @param initialCapacity a int
     * @param loadFactor a float
     * @param accessOrder a boolean
     * @return {@link java.util.LinkedHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int initialCapacity,
        float loadFactor,
        boolean accessOrder) {
        return new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * <p>newLinkedHashMap.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return {@link java.util.LinkedHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return newLinkedHashMap(map);
    }

    /**
     * <p>newConcurrentHashMap.</p>
     *
     * @return {@link java.util.concurrent.ConcurrentHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * <p>newConcurrentHashMap.</p>
     *
     * @param initialCapacity a int
     * @return {@link java.util.concurrent.ConcurrentHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int initialCapacity) {
        return new ConcurrentHashMap<>(initialCapacity);
    }

    /**
     * <p>newConcurrentHashMap.</p>
     *
     * @param initialCapacity a int
     * @param loadFactor a float
     * @return {@link java.util.concurrent.ConcurrentHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int initialCapacity,
        float loadFactor) {
        return new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * <p>newConcurrentHashMap.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return {@link java.util.concurrent.ConcurrentHashMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map) {
        return new ConcurrentHashMap<>(map);
    }

    /**
     * <p>newTreeMap.</p>
     *
     * @return {@link java.util.TreeMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<>();
    }

    /**
     * <p>newTreeMap.</p>
     *
     * @param comparator a {@link java.util.Comparator} object
     * @return {@link java.util.TreeMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
        return new TreeMap<>(comparator);
    }

    /**
     * <p>newTreeMap.</p>
     *
     * @param map a {@link java.util.Map} object
     * @return {@link java.util.TreeMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> TreeMap<K, V> newTreeMap(Map<? extends K, ? extends V> map) {
        return new TreeMap<>(map);
    }

    /**
     * <p>newTreeMap.</p>
     *
     * @param map a {@link java.util.SortedMap} object
     * @return {@link java.util.TreeMap}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
        return new TreeMap<>(map);
    }

    /**
     * Shallow Clone {@link java.util.Map}
     *
     * @param source the source of {@link java.util.Map}
     * @return non-null
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map<K, V> shallowCloneMap(Map<K, V> source) {
        if (source instanceof SortedMap) {
            return new TreeMap(source);
        } else if (source instanceof LinkedHashMap) {
            return new LinkedHashMap(source);
        } else if (source instanceof IdentityHashMap) {
            return new IdentityHashMap(source);
        } else if (source instanceof ConcurrentNavigableMap) {
            return new ConcurrentSkipListMap(source);
        } else if (source instanceof ConcurrentMap) {
            return new ConcurrentHashMap<>(source);
        } else {
            return new HashMap(source);
        }
    }

    /**
     * <p>toFixedMap.</p>
     *
     * @param values a {@link java.util.Collection} object
     * @param entryMapper a {@link java.util.function.Function} object
     * @return {@link java.util.Map}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     * @param <E> a E class
     */
    public static <K, V, E> Map<K, V> toFixedMap(Collection<E> values,
        Function<E, Map.Entry<K, V>> entryMapper) {
        int size = CollectionUtils.size(values);
        if (size < 1) {
            return emptyMap();
        }

        Map<K, V> fixedMap = newHashMap(size, MIN_LOAD_FACTOR);

        for (E value : values) {
            Map.Entry<K, V> entry = entryMapper.apply(value);
            fixedMap.put(entry.getKey(), entry.getValue());
        }
        return unmodifiableMap(fixedMap);
    }

    /**
     * <p>ofEntry.</p>
     *
     * @param key a K object
     * @param value a V object
     * @return {@link java.util.Map.Entry}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map.Entry<K, V> ofEntry(K key, V value) {
        return immutableEntry(key, value);
    }

    /**
     * <p>immutableEntry.</p>
     *
     * @param key a K object
     * @param value a V object
     * @return {@link java.util.Map.Entry}<{@link K}, {@link V}>
     * @param <K> a K class
     * @param <V> a V class
     */
    public static <K, V> Map.Entry<K, V> immutableEntry(K key, V value) {
        return new ImmutableEntry(key, value);
    }

    /**
     * TODO
     *
     * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
     * @since 0.0.1
     */
    public static class ImmutableEntry<K, V> implements Map.Entry<K, V> {

        /**
         *
         */
        private final K key;

        /**
         *
         */
        private final V value;

        /**
         * @param key
         * @param value
         */
        public ImmutableEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return {@link K}
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * @return {@link V}
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * @param value
         * @return {@link V}
         */
        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("ReadOnly Entry can't be modified");
        }
    }
}
