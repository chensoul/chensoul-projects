
package com.chensoul.spring.cloud.openfeign.configuration;

import com.chensoul.spring.cloud.openfeign.retry.FeignRetryAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * 重试配置
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RetryTemplate.class)
@ConditionalOnProperty(name = "feign.retry.enabled", havingValue = "true")
public class RetryFeignConfiguration {

    /**
     * @return
     */
    @Bean
    public FeignRetryAspect feignRetryAspect() {
        return new FeignRetryAspect();
    }

}
