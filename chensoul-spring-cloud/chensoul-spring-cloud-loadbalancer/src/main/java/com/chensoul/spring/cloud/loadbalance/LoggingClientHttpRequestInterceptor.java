package com.chensoul.spring.cloud.loadbalance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {
        log.info("=========================== request begin ===========================");
        log.info("uri : {}", request.getURI());
        log.info("method : {}", request.getMethod());
        log.info("headers : {}", request.getHeaders());
        log.info("request body : {}", new String(body, StandardCharsets.UTF_8));
        log.info("============================ request end ============================");
    }

    private void traceResponse(ClientHttpResponse httpResponse) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        log.info("============================ response begin ============================");
        log.info("Status code  : {}", httpResponse.getStatusCode());
        log.info("Status text  : {}", httpResponse.getStatusText());
        log.info("Headers      : {}", httpResponse.getHeaders());
        log.info("Response body: {}", inputStringBuilder.toString());
        log.info("============================= response end =============================");
    }
}
