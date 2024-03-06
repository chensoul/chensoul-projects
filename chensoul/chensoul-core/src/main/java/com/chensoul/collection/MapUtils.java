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
 * The utilities class for Java {@link Map}
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class MapUtils {

    /**
     * The min load factor for {@link HashMap} or {@link Hashtable}
     */
    public static final float MIN_LOAD_FACTOR = Float.MIN_NORMAL;

    /**
     * @param map
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * @param map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * @param key
     * @param value
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        return singletonMap(key, value);
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2) {
        return ofMap(key1, value1, key2, value2);
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        return ofMap(key1, value1, key2, value2, key3, value3);
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @param key4
     * @param value4
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @param key4
     * @param value4
     * @param key5
     * @param value5
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return ofMap(key1, value1, key2, value2, key3, value3, key4, value4, key5, value5);
    }

    /**
     * @param values
     * @return {@link Map}
     */
    public static Map of(Object... values) {
        return ofMap(values);
    }

    /**
     * @param keyValuePairs
     * @return {@link Map}
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
     * @return {@link HashMap}<{@link K}, {@link V}>
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * @param initialCapacity
     * @return {@link HashMap}<{@link K}, {@link V}>
     */
    public static <K, V> HashMap<K, V> newHashMap(int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }

    /**
     * @param initialCapacity
     * @param loadFactor
     * @return {@link HashMap}<{@link K}, {@link V}>
     */
    public static <K, V> HashMap<K, V> newHashMap(int initialCapacity, float loadFactor) {
        return new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * @param map
     * @return {@link HashMap}<{@link K}, {@link V}>
     */
    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<>(map);
    }

    /**
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    /**
     * @param initialCapacity
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int initialCapacity) {
        return new LinkedHashMap<>(initialCapacity);
    }

    /**
     * @param initialCapacity
     * @param loadFactor
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int initialCapacity,
        float loadFactor) {
        return new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * @param initialCapacity
     * @param loadFactor
     * @param accessOrder
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int initialCapacity,
        float loadFactor,
        boolean accessOrder) {
        return new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * @param map
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return newLinkedHashMap(map);
    }

    /**
     * @return {@link ConcurrentHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * @param initialCapacity
     * @return {@link ConcurrentHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int initialCapacity) {
        return new ConcurrentHashMap<>(initialCapacity);
    }

    /**
     * @param initialCapacity
     * @param loadFactor
     * @return {@link ConcurrentHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int initialCapacity,
        float loadFactor) {
        return new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * @param map
     * @return {@link ConcurrentHashMap}<{@link K}, {@link V}>
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map) {
        return new ConcurrentHashMap<>(map);
    }

    /**
     * @return {@link TreeMap}<{@link K}, {@link V}>
     */
    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<>();
    }

    /**
     * @param comparator
     * @return {@link TreeMap}<{@link K}, {@link V}>
     */
    public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
        return new TreeMap<>(comparator);
    }

    /**
     * @param map
     * @return {@link TreeMap}<{@link K}, {@link V}>
     */
    public static <K, V> TreeMap<K, V> newTreeMap(Map<? extends K, ? extends V> map) {
        return new TreeMap<>(map);
    }

    /**
     * @param map
     * @return {@link TreeMap}<{@link K}, {@link V}>
     */
    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
        return new TreeMap<>(map);
    }

    /**
     * Shallow Clone {@link Map}
     *
     * @param source the source of {@link Map}
     * @return non-null
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
     * @param values
     * @param entryMapper
     * @return {@link Map}<{@link K}, {@link V}>
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
     * @param key
     * @param value
     * @return {@link Map.Entry}<{@link K}, {@link V}>
     */
    public static <K, V> Map.Entry<K, V> ofEntry(K key, V value) {
        return immutableEntry(key, value);
    }

    /**
     * @param key
     * @param value
     * @return {@link Map.Entry}<{@link K}, {@link V}>
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
