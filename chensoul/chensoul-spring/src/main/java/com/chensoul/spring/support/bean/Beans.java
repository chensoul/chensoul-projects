package com.chensoul.spring.support.bean;

import com.chensoul.lang.function.CheckedConsumer;
import com.chensoul.lang.function.CheckedSupplier;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import java.io.File;
import java.time.Duration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;


/**
 * A re-usable collection of utility methods for object instantiations and configurations used cross various
 * {@code @Bean} creation methods throughout CAS server.
 */
@Slf4j
public class Beans {

    /**
     * New duration. If the provided length is duration,
     * it will be parsed accordingly, or if it's a numeric value
     * it will be pared as a duration assuming it's provided as seconds.
     *
     * @param value the length in seconds.
     * @return the duration
     */
    public static Duration newDuration(final String value) {
        if (isNeverDurable(value)) {
            return Duration.ZERO;
        }
        if (isInfinitelyDurable(value)) {
            return Duration.ofDays(Integer.MAX_VALUE);
        }
//        if (NumberUtils.isCreatable(value)) {
//            return Duration.ofSeconds(Long.parseLong(value));
//        }
        return Duration.parse(value);
    }

    /**
     * Is infinitely durable?
     *
     * @param value the value
     * @return true/false
     */
    public static boolean isInfinitelyDurable(final String value) {
        return "-1".equalsIgnoreCase(value) || StringUtils.isEmpty(value) || "INFINITE".equalsIgnoreCase(value);
    }

    /**
     * Is never durable?
     *
     * @param value the value
     * @return true/false
     */
    public static boolean isNeverDurable(final String value) {
        return "0".equalsIgnoreCase(value) || "NEVER".equalsIgnoreCase(value) || StringUtils.isEmpty(value);
    }

    /**
     * Gets temp file path.
     *
     * @param prefix the prefix
     * @param suffix the suffix
     * @return the temp file path
     */
    public static String getTempFilePath(final String prefix, final String suffix) {
        return CheckedSupplier.unchecked(() -> File.createTempFile(prefix, suffix).getCanonicalPath()).get();
    }

    public static <T, V> Cache<T, V> newCache(final int initialCapacity, int cacheSize, final String duration, final Expiry<T, V> expiryAfter) {
        return newCacheBuilder(initialCapacity, cacheSize, duration).expireAfter(expiryAfter).build();
    }

    public static Caffeine newCacheBuilder(final int initialCapacity, int cacheSize, final String duration) {
        val builder = Caffeine.newBuilder()
            .initialCapacity(initialCapacity)
            .maximumSize(cacheSize);
        if (StringUtils.isNotBlank(duration)) {
            builder.expireAfterWrite(newDuration(duration));
        }
        builder.removalListener((key, value, cause) -> {
            log.trace("Removing cached value [{}] linked to cache key [{}]; removal cause is [{}]", value, key, cause);
            CheckedConsumer.unchecked(__ -> {
                if (value instanceof AutoCloseable) {
                    AutoCloseable closeable = (AutoCloseable) value;
                    Objects.requireNonNull(closeable).close();
                }
            }).accept(value);
        });
        return builder;
    }
}
