package com.chensoul.gateway;

import java.util.Objects;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayApplication {

    public static void main(final String[] args) {
        //https://github.com/spring-projects/spring-boot/issues/26218
        System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * @link {https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory}
     */
    @Bean
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

}
