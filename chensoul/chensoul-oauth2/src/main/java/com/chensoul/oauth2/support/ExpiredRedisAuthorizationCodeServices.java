package com.chensoul.oauth2.support;

import java.lang.reflect.Method;
import java.util.List;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RedisAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 *
 */
public class ExpiredRedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {
    private static final boolean springDataRedis_2_0 = ClassUtils.isPresent(
        "org.springframework.data.redis.connection.RedisStandaloneConfiguration",
        RedisAuthorizationCodeServices.class.getClassLoader());

    private static final String AUTH_CODE = "oauth2:code:";

    private final RedisConnectionFactory connectionFactory;

    private String prefix = "";

    private long expiredSeconds = 300L;

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    private Method redisConnectionSet_2_0;

    /**
     * Default constructor.
     *
     * @param connectionFactory the connection factory which should be used to obtain a connection to Redis
     */
    public ExpiredRedisAuthorizationCodeServices(final RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        if (springDataRedis_2_0) {
            loadRedisConnectionMethods_2_0();
        }
    }

    @Override
    protected void store(final String code, final OAuth2Authentication authentication) {
        final byte[] key = serializeKey(AUTH_CODE + code);
        final byte[] auth = serialize(authentication);

        final RedisConnection conn = getConnection();
        try {
            if (springDataRedis_2_0) {
                try {
                    redisConnectionSet_2_0.invoke(conn, key, expiredSeconds, auth);
                } catch (final Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                conn.setEx(key, expiredSeconds, auth);
            }
        } finally {
            conn.close();
        }
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        final byte[] key = serializeKey(AUTH_CODE + code);

        List<Object> results = null;
        final RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(key);
            results = conn.closePipeline();
        } finally {
            conn.close();
        }

        if (results == null) {
            return null;
        }
        final byte[] bytes = (byte[]) results.get(0);
        return deserializeAuthentication(bytes);
    }

    private void loadRedisConnectionMethods_2_0() {
        redisConnectionSet_2_0 = ReflectionUtils.findMethod(
            RedisConnection.class, "setEx", byte[].class, long.class, byte[].class);
    }

    private byte[] serializeKey(final String object) {
        return serialize(prefix + object);
    }

    private byte[] serialize(final Object object) {
        return serializationStrategy.serialize(object);
    }

    private byte[] serialize(final String string) {
        return serializationStrategy.serialize(string);
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private OAuth2Authentication deserializeAuthentication(final byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    public void setSerializationStrategy(final RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public void setExpiredSeconds(final long expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }
}
