package com.chensoul.redis;

import com.chensoul.redis.support.CustomRedisCacheManager;
import com.chensoul.redis.support.CustomRedisCacheWriter;
import static com.chensoul.redis.support.RedisObjectFactory.jackson2JsonRedisSerializer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 扩展redis-cache支持注解cacheName添加超时时间
 */
@EnableCaching
@AutoConfiguration
@AutoConfigureAfter({RedisAutoConfiguration.class})
@ConditionalOnClass({RedisConnectionFactory.class})
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheManagerConfiguration {
    private final CacheProperties cacheProperties;
    private final CacheManagerCustomizers customizerInvoker;
    private final RedisCacheConfiguration redisCacheConfiguration;

    /**
     * @param cacheProperties
     * @param customizers
     * @param redisCacheConfiguration
     */
    RedisCacheManagerConfiguration(final CacheProperties cacheProperties, final ObjectProvider<List<CacheManagerCustomizer<?>>> customizers,
                                   final ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration) {
        this.cacheProperties = cacheProperties;
        customizerInvoker = new CacheManagerCustomizers(customizers.getIfAvailable());
        this.redisCacheConfiguration = redisCacheConfiguration.getIfAvailable();
    }

    /**
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(final RedisConnectionFactory connectionFactory) {
        final RedisCacheConfiguration cacheConfiguration = determineConfiguration();

        final List<String> cacheNames = cacheProperties.getCacheNames();
        final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        if (!cacheNames.isEmpty()) {
            cacheNames.forEach(it -> initialCaches.put(it, cacheConfiguration));
        }

        final CustomRedisCacheWriter redisCacheWriter = new CustomRedisCacheWriter(connectionFactory);
        final CustomRedisCacheManager cacheManager = new CustomRedisCacheManager(redisCacheWriter, cacheConfiguration,
            initialCaches, true);
        cacheManager.setTransactionAware(false);
        return customizerInvoker.customize(cacheManager);
    }

    /**
     * @return
     */
    private RedisCacheConfiguration determineConfiguration() {
        if (redisCacheConfiguration != null) {
            return redisCacheConfiguration;
        }

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()));

        final CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.computePrefixWith(CacheKeyPrefix.prefixed(redisProperties.getKeyPrefix()));
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
