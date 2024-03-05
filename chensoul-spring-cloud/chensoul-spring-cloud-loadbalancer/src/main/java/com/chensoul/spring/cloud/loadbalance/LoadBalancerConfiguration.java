package com.chensoul.spring.cloud.loadbalance;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoadBalancerConfiguration {

    /**
     * 支持负载均衡的 {@link RestTemplate}
     *
     * @return 返回 支持负载均衡的 {@link RestTemplate}
     */
    @Bean
    @LoadBalanced
    @SentinelRestTemplate(
        blockHandler = "handleBlock",
        fallback = "handleFallback",
        fallbackClass = SentinelFallbackBlockHandler.class,
        blockHandlerClass = SentinelFallbackBlockHandler.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
