package com.chensoul.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Replace the url with the gateway loadbalancer url in swagger v3 api docs
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Component
@Order
public class ReplaceUrlInApiDocsGlobalFilter implements GlobalFilter {
    /**
     * Regex pattern for /xx/v3/api-docs
     */
    private static final Pattern REGEX_PATTERN = Pattern.compile("^/([^/]+)/v3/api-docs$");

    /**
     * Jackson ObjectMapper
     */
    private ObjectMapper objectMapper;

    public ReplaceUrlInApiDocsGlobalFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param exchange
     * @param chain
     * @return {@link Mono}<{@link Void}>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        URI uri = request.getURI();
        String path = uri.getPath();

        Matcher matcher = REGEX_PATTERN.matcher(path);
        if (!matcher.find()) {
            return chain.filter(exchange);
        }

        String service = matcher.group(1);
        String serviceUrl = UriComponentsBuilder.newInstance()
            .scheme(uri.getScheme())
            .host(uri.getHost())
            .port(uri.getPort())
            .path(service)
            .toUriString();

        ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                Flux<? extends DataBuffer> fluxDataBuffer = (Flux<? extends DataBuffer>) body;

                return response.writeWith(fluxDataBuffer.buffer().map(dataBuffer -> {
                    DataBuffer join = response.bufferFactory().join(dataBuffer);
                    byte[] bytes = new byte[join.readableByteCount()];
                    join.read(bytes);
                    DataBufferUtils.release(join);

                    String originalText = new String(bytes);
                    String result;
                    try {
                        Map<String, Object> map = objectMapper.readValue(originalText, Map.class);
                        List<Map<String, Object>> servers = (List<Map<String, Object>>) map.get("servers");
                        for (Map<String, Object> server : servers) {
                            Object originUrl = server.get("url");
                            if (server.replace("url", serviceUrl) != null) {
                                log.info("Replace the url with the gateway url: {} -> {}", originUrl, serviceUrl);
                            }
                        }
                        result = objectMapper.writeValueAsString(map);
                    } catch (JsonProcessingException e) {
                        result = originalText;
                    }

                    response.getHeaders().setContentLength(result.getBytes().length);
                    return response.bufferFactory().wrap(result.getBytes());
                }));
            }
        };

        return chain.filter(exchange.mutate().response(decorator).build());
    }
}
