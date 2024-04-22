package com.chensoul.cache;

import com.chensoul.cache.redis.CustomRedisCacheManager;
import com.chensoul.cache.redis.CustomRedisCacheWriter;
import com.chensoul.cache.redis.RedisObjectFactory;
import com.chensoul.util.JacksonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.CollectionUtils;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnMissingBean(CacheManager.class)
@EnableConfigurationProperties({CacheProperties.class, CacheSpecProperties.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class CustomRedisCacheConfiguration {

    @Bean
    RedisCacheManager cacheManager(CacheProperties cacheProperties,
                                   CacheSpecProperties cacheSpecProperties,
                                   CacheManagerCustomizers cacheManagerCustomizers,
                                   ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers,
                                   RedisConnectionFactory cf) {
        DefaultFormattingConversionService redisConversionService = new DefaultFormattingConversionService();
        RedisCacheConfiguration.registerDefaultConverters(redisConversionService);
        RedisCacheConfiguration configuration = createConfiguration(cacheProperties, redisConversionService);

        Map<String, RedisCacheConfiguration> initialCaches = new HashMap<>();

        //支持通过配置文件来自定义缓存过期时间
        if (cacheSpecProperties != null && !CollectionUtils.isEmpty(cacheSpecProperties.getSpecs())) {
            log.info("Initialized redis cache specs {}", cacheSpecProperties.getSpecs());

            cacheSpecProperties.getSpecs().forEach((cacheName, cacheSpecs) -> {
                initialCaches.put(cacheName, configuration.entryTtl(cacheSpecs.getTimeToLive()));
            });
        }

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(cf)
            .cacheDefaults(configuration).withInitialCacheConfigurations(initialCaches).transactionAware();
        if (cacheProperties.getRedis().isEnableStatistics()) {
            builder.enableStatistics();
        }

        //支持通过缓存名称来自定义缓存过期时间
        final CustomRedisCacheWriter redisCacheWriter = new CustomRedisCacheWriter(cf);
        final CustomRedisCacheManager cacheManager = new CustomRedisCacheManager(redisCacheWriter, configuration,
            initialCaches, true);

        redisCacheManagerBuilderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return cacheManagerCustomizers.customize(builder.build());
    }

    private RedisCacheConfiguration createConfiguration(
        CacheProperties cacheProperties, DefaultFormattingConversionService redisConversionService) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config.withConversionService(redisConversionService);

        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisObjectFactory.jackson2JsonRedisSerializer()));

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);

        //缓存对象支持 json 序列化
        ObjectMapper mapper = JacksonUtils.getObjectMapperWithJavaTimeModule();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("limit.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

}
