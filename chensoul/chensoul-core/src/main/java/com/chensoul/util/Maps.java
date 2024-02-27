package com.chensoul.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class Maps {

    /**
     *
     */
    private Maps() {
    }

    /**
     * @param entries
     * @param <K>
     * @param <V>
     * @return
     */
    @SafeVarargs
    public static <K, V> Map<K, V> of(final Map.Entry<? extends K, ? extends V>... entries) {
        final Map<K, V> map = new HashMap<>();
        for (final Map.Entry<? extends K, ? extends V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * @param entries
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> of(final Object... entries) {
        if (entries.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid number of arguments: " + entries.length);
        }
        final Map<K, V> map = new HashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            final K key = (K) entries[i];
            final V value = (V) entries[i + 1];
            map.put(key, value);
        }
        return Collections.unmodifiableMap(map);
    }
}
