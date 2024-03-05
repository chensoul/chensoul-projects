package com.chensoul.spring.boot.redis.support;

import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * redis cache 扩展cache name 自动化配置，ttl 单位为秒 cachename = xx#ttl
 */
@Slf4j
public class CustomRedisCacheManager extends RedisCacheManager {
    private static final String SPLIT_FLAG = "#";
    private static final String COLON = ":";
    private static final int CACHE_LENGTH = 2;

    /**
     * @param cacheWriter
     * @param defaultCacheConfiguration
     * @param initialCacheConfigurations
     * @param allowInFlightCacheCreation
     */
    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                                   Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        if (StringUtils.isEmpty(name) || !name.contains(SPLIT_FLAG)) {
            return super.createRedisCache(name, cacheConfig);
        }

        String[] cacheArray = name.split(SPLIT_FLAG);
        if (cacheArray.length < CACHE_LENGTH) {
            return super.createRedisCache(name, cacheConfig);
        }

        if (cacheConfig != null) {
            String ttlStr = cacheArray[1].split(COLON)[0];
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(Long.parseLong(ttlStr)));
        }
        return super.createRedisCache(name, cacheConfig);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public Cache getCache(String name) {
        return super.getCache(name);
    }

}
