package com.chensoul.auth.captcha;

import com.chensoul.auth.captcha.service.CaptchaService;
import com.chensoul.auth.captcha.service.InMemoryCaptchaService;
import com.chensoul.auth.captcha.service.RedisCaptchaService;
import javax.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Captcha Configuration
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration
@ConditionalOnProperty(name = "auth.service.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AuthProperties.class)
public class CaptchaConfiguration {

    @Bean
    public FilterRegistrationBean captchaFilter(final CaptchaService captchaService) {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new CaptchaFilter(captchaService));
        registration.addUrlPatterns("/oauth/token");
        registration.setName("captchaFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    @ConditionalOnProperty(name = "auth.service.enabled", havingValue = "true")
    public CaptchaService RedisCaptchaService(final RedisTemplate<String, Object> redisTemplate, final AuthProperties authProperties) {
        return new RedisCaptchaService(redisTemplate, authProperties.getLogin().getCaptcha());
    }

    @Bean
    @ConditionalOnMissingBean(CaptchaService.class)
    @ConditionalOnProperty(name = "auth.service.enabled", havingValue = "false", matchIfMissing = true)
    public CaptchaService inMemoryCaptchaService() {
        return new InMemoryCaptchaService();
    }
}
