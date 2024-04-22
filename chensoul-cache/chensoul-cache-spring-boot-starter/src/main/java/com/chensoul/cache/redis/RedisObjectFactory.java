package com.chensoul.cache.redis;

import com.chensoul.util.JacksonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Slf4j
@UtilityClass
public class RedisObjectFactory {
    public static Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        final ObjectMapper mapper = JacksonUtils.getObjectMapperWithJavaTimeModule();
        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        return jackson2JsonRedisSerializer;
    }
}
