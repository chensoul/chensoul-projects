package com.chensoul.gateway;

import com.chensoul.constant.StringPool;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗 2. 重写  StripPrefix = 1,支持全局（去掉）
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class RequestGlobalFilter implements GlobalFilter, Ordered {
    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 清洗请求头中 from 参数
        ServerHttpRequest request = exchange.getRequest().mutate()
            .headers(httpHeaders -> httpHeaders.remove("from")).build();

        // 2. 重写 StripPrefix @see StripPrefixGatewayFilterFactory
        addOriginalRequestUrl(exchange, request.getURI());

        String oldUri = request.getURI().toString();

        String rawPath = request.getURI().getRawPath();
        String newPath = StringPool.SLASH + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, StringPool.SLASH, true, true))
            .skip(1L)
            .collect(Collectors.joining(StringPool.SLASH));
        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        if (log.isDebugEnabled()) {
            log.debug("[Gateway] {} -> {}", oldUri, newRequest.getURI());
        }

        return chain.filter(exchange.mutate()
            .request(newRequest.mutate().build()).build());
    }

    /**
     * @return
     */
    @Override
    public int getOrder() {
        return -1000;
    }

}
