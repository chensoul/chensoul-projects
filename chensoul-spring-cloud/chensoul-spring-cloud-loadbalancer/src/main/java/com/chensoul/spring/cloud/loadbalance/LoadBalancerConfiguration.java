package com.chensoul.spring.cloud.loadbalance;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
    @Primary
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingClientHttpRequestInterceptor());
        restTemplate.setInterceptors(interceptors);

        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        return factory;
    }

}


