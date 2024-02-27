package com.chensoul.redis.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.chensoul.jackson.support.JacksonObjectMapperFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * This is {@link RedisObjectFactory}.
 */
@Slf4j
@UtilityClass
public class RedisObjectFactory {
    public static Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        final ObjectMapper mapper = JacksonObjectMapperFactory.builder().build().toObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        return jackson2JsonRedisSerializer;
    }
}
