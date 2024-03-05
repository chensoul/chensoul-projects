package org.springframework.cloud.openfeign;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 *
 */
public class HystrixFeignConfiguration {
    /**
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean(Targeter.class)
    @ConditionalOnProperty("loadbalance.hystrix.enabled")
    public Targeter hystrixTargeter() {
        return new HystrixTargeter();
    }
}
