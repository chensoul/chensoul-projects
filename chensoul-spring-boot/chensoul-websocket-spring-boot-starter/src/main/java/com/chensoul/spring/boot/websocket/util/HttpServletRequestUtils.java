package com.chensoul.spring.boot.websocket.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ObjectUtils;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class HttpServletRequestUtils {
    private HttpServletRequestUtils() {
    }

    /**
     * @param request
     * @return
     */
    public static HttpServletRequest getHttpServletRequest(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest();
        }
        return null;
    }

    /**
     * @param request
     * @return
     */
    public static HttpSession getSession(ServerHttpRequest request) {
        HttpServletRequest httpServletRequest = getHttpServletRequest(request);
        if (!ObjectUtils.isEmpty(httpServletRequest)) {
            return httpServletRequest.getSession();
        }
        return null;
    }

}
