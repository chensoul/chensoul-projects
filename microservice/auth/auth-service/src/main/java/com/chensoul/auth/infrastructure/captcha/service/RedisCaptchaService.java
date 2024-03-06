package com.chensoul.auth.infrastructure.captcha.service;

import com.chensoul.auth.infrastructure.captcha.AuthProperties;
import com.chensoul.net.InetAddressUtils;
import com.chensoul.spring.util.HttpRequestUtils;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class RedisCaptchaService extends AbstractCaptchaService implements CaptchaService {
    private static final String CODE_PREFIX = "service:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthProperties.CaptchaProperties captchaProperties;

    private final CaptchaAttemptService captchaAttemptService;

    /**
     * @param redisTemplate
     */
    public RedisCaptchaService(final RedisTemplate<String, Object> redisTemplate, final AuthProperties.CaptchaProperties captchaProperties) {
        this.redisTemplate = redisTemplate;
        this.captchaProperties = captchaProperties;
        this.captchaAttemptService = new CaptchaAttemptService(redisTemplate);
    }

    @Override
    protected void onGenerateSuccess(final String randStr, final String code) {
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.opsForValue().set(this.getKey(randStr), code, this.captchaProperties.getExpiredSecond(), TimeUnit.SECONDS);
    }


    @Override
    public boolean validateCode(final String code, final String randStr) {
        if (this.captchaProperties.isDev()) {
            return true;
        }
        final String key = this.getKey(randStr);
        final Object codeValue = this.redisTemplate.opsForValue().get(key);

        log.info("{}, {}, {}", key, code, codeValue);

        return Objects.equals(code, codeValue);
    }


    @Override
    protected void beforeValidate(final HttpServletRequest request, final HttpServletResponse response) {
        final String clientIp = HttpRequestUtils.getClientIp(request);
        if (!InetAddressUtils.isInternalIp(clientIp)) {
            this.captchaAttemptService.isBlocked(clientIp);
        }
    }


    @Override
    protected void onValidateSuccess(final String code, final String randStr) {
        this.redisTemplate.delete(this.getKey(randStr));
        this.captchaAttemptService.captchaSucceeded(this.getKey(randStr));
    }

    @Override
    protected void onValidateFail(final HttpServletRequest request, final String code, final String randStr) {
        this.captchaAttemptService.captchaFailed(HttpRequestUtils.getClientIp(request));
    }

    public String getKey(final String randStr) {
        return CODE_PREFIX + this.captchaProperties.getPrefix() + randStr;
    }
}
