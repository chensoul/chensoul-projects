package com.chensoul.auth.infrastructure.captcha.service;

import com.chensoul.exception.BusinessException;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@AllArgsConstructor
public class CaptchaAttemptService {
    private final int CAPTCHA_MAX_ATTEMPT = 5;
    private final int CAPTCHA_FAIL_LOCK_MINUTES = 15;
    private final String ATTEMPT_PREFIX = "service:attempt:";

    private RedisTemplate<String, Object> redisTemplate;


    /**
     * @param key
     */
    public void captchaSucceeded(final String key) {
        redisTemplate.delete(getCacheKey(key));
    }

    /**
     * @param key
     * @return
     */
    public Long captchaFailed(final String key) {
        final String cacheKey = getCacheKey(key);

        //TODO 考虑并发问题
        final Long value = redisTemplate.opsForValue().increment(cacheKey, 1);
        redisTemplate.expire(cacheKey, Duration.ofMinutes(15));

        return value;
    }

    /**
     * @param key
     * @return
     */
    public boolean isBlocked(final String key) {
        final Integer increment = (Integer) redisTemplate.opsForValue().get(getCacheKey(key));
        final Boolean blocked = increment != null && increment >= CAPTCHA_MAX_ATTEMPT;

        if (blocked) {
            log.warn("用户【{}】获取验证码超过最大次数，请{}分钟后再试!", key, CAPTCHA_FAIL_LOCK_MINUTES);

            throw new BusinessException("获取验证码超过最大次数，请" + CAPTCHA_FAIL_LOCK_MINUTES + "分钟后再试!");
        }
        return false;
    }

    /**
     * @param key
     * @return
     */
    private String getCacheKey(final String key) {
        return ATTEMPT_PREFIX + key;
    }
}
