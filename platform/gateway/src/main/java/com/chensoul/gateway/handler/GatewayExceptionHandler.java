/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chensoul.gateway.handler;

import com.chensoul.jackson.utils.JsonUtils;
import com.chensoul.util.ExceptionUtils;
import com.chensoul.util.R;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * 网关异常通用处理器，只作用在 webflux 环境下 , 优先级低于 {@link ResponseStatusExceptionHandler} 执行
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    /**
     * @param exchange the current exchange
     * @param ex       the exception to handle
     * @return
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            Throwable rootCause = ExceptionUtils.getRootCause(ex);
            R restResult = R.error(rootCause != null ? rootCause.getMessage() : ex.getMessage());

            log.error("Gateway Error: {}, {}", exchange.getRequest().getPath(), restResult.getMessage(), ex);

            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JsonUtils.toJson(restResult).getBytes(StandardCharsets.UTF_8));
        }));
    }

}
