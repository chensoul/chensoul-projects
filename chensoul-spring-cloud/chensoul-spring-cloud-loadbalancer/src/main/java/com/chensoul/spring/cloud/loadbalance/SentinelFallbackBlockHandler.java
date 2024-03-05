package com.chensoul.spring.cloud.loadbalance;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

public class SentinelFallbackBlockHandler {
    public static ClientHttpResponse handleBlock(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException exception) {
        return new SentinelClientHttpResponse();
    }

    public static ClientHttpResponse handleFallback(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
        return new SentinelClientHttpResponse();
    }
}
