package com.chensoul.spring.client;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Creates a {@link ClientInfo} object from the http request
 * and passes it to the {@link ClientInfoHolder} .
 */
@RequiredArgsConstructor
public class ClientInfoThreadLocalFilter implements Filter {
    private final ClientInfoOptions options;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest) {
                final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                ClientInfo clientInfo = ClientInfo.from(httpServletRequest, options);
                ClientInfoHolder.setClientInfo(clientInfo);
            }
            filterChain.doFilter(request, response);
        } finally {
            ClientInfoHolder.clear();
        }
    }
}
