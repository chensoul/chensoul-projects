package com.chensoul.oauth2.support;

import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since
 */
@AllArgsConstructor
public class RedisLoginAttemptService {
    private RedisTemplate<String, Object> redisTemplate;

    private final int LOGIN_MAX_ATTEMPT = 5;
    private final int LOGIN_FAIL_LOCK_MINUTES = 10;
    private final String ATTEMPT_PREFIX = "login:attempt:";

    /**
     * @param key
     */
    public void loginSucceeded(final String key) {
        redisTemplate.delete(getCacheKey(key));
    }

    /**
     * @param key
     * @return
     */
    public Integer loginFailed(final String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        String cacheKey = getCacheKey(key);

        Integer longValue = (Integer) valueOps.get(cacheKey);
        longValue = longValue == null ? 1 : longValue + 1;
        valueOps.set(cacheKey, longValue, Duration.ofMinutes(LOGIN_FAIL_LOCK_MINUTES));

        return longValue;
    }

    /**
     * @param key
     * @return
     */
    public Boolean isBlocked(final String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("用户名不能为空");
        }

        Object increment = redisTemplate.opsForValue().get(getCacheKey(key));
        Boolean blocked = increment != null && (Integer) increment >= LOGIN_MAX_ATTEMPT;

        if (blocked) {
            throw new BadCredentialsException("连续" + LOGIN_MAX_ATTEMPT + "次输错密码，账号被锁定" + LOGIN_FAIL_LOCK_MINUTES + "分钟");
        }
        return false;
    }

    /**
     * @param key
     * @return
     */
    private String getCacheKey(String key) {
        return ATTEMPT_PREFIX + key;
    }
}
