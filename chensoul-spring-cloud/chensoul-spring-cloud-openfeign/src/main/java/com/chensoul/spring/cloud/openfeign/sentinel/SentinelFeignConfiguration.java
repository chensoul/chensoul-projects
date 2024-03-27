
package com.chensoul.spring.cloud.openfeign.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.chensoul.spring.cloud.openfeign.sentinel.ext.AllowHeaderRequestOriginParser;
import com.chensoul.spring.cloud.openfeign.sentinel.handle.JsonBlockExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * sentinel 配置
 */
@AutoConfiguration
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@ConditionalOnProperty(name = "feign.sentinel.enabled", havingValue = "true")
public class SentinelFeignConfiguration {

    /**
     * @return
     */
//    @Bean
//    @Scope("prototype")
//    @ConditionalOnMissingBean
//    public Feign.Builder autoFallbackSentinelFeignBuilder() {
//        return AutoFallbackSentinelFeign.builder();
//    }

    /**
     * @param objectMapper
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler(ObjectMapper objectMapper) {
        return new JsonBlockExceptionHandler(objectMapper);
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestOriginParser requestOriginParser() {
        return new AllowHeaderRequestOriginParser();
    }

}
