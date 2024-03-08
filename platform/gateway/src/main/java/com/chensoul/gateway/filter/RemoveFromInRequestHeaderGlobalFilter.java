package com.chensoul.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RemoveFromInRequestHeaderGlobalFilter implements GlobalFilter {
    private static final String HEADER_NAME = "from";

    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
            .headers(httpHeaders -> {
                if (httpHeaders.containsKey(HEADER_NAME)) {
                    log.info("Remove the header '{}' from the request headers", HEADER_NAME);
                    httpHeaders.remove(HEADER_NAME);
                }
            }).build();
        return chain.filter(exchange.mutate().request(request.mutate().build()).build());
    }
}
