package com.chensoul.spring.boot.websocket.handler;

import com.chensoul.spring.boot.websocket.properties.WebSocketProperties;
import com.chensoul.spring.boot.websocket.util.HttpServletRequestUtils;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Data
@Setter
public class StompWebSocketHandshakeHandler extends DefaultHandshakeHandler {

    private WebSocketProperties webSocketProperties;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();
        if (!ObjectUtils.isEmpty(principal)) {
            log.debug("Get user principal from request, value is  [{}].", principal.getName());
            return principal;
        }

        Object user = null;
        HttpServletRequest httpServletRequest = HttpServletRequestUtils.getHttpServletRequest(request);
        if (!ObjectUtils.isEmpty(httpServletRequest)) {
            user = httpServletRequest.getAttribute(webSocketProperties.getPrincipalAttribute());
            if (ObjectUtils.isEmpty(user)) {
                user = httpServletRequest.getParameter(webSocketProperties.getPrincipalAttribute());
                if (ObjectUtils.isEmpty(user)) {
                    user = httpServletRequest.getHeader("X-Herodotus-Session");
                } else {
                    log.debug("Get user principal [{}] from request parameter, use parameter  [{}]..", user,
                            webSocketProperties.getPrincipalAttribute());
                }
            } else {
                log.debug("Get user principal [{}] from request attribute, use attribute  [{}]..", user,
                        webSocketProperties.getPrincipalAttribute());
            }
        }

        if (ObjectUtils.isEmpty(user)) {
            HttpSession httpSession = HttpServletRequestUtils.getSession(request);
            if (!ObjectUtils.isEmpty(httpSession)) {
                user = httpSession.getAttribute(webSocketProperties.getPrincipalAttribute());
                if (ObjectUtils.isEmpty(user)) {
                    user = httpSession.getId();
                } else {
                    log.debug("Get user principal [{}] from httpsession, use attribute  [{}].", user,
                            webSocketProperties.getPrincipalAttribute());
                }
            } else {
                log.error("Cannot find session from websocket request.");
                return null;
            }
        } else {
            log.debug("Get user principal [{}] from request header X_HERODOTUS_SESSION.", user);
        }

        // TODO
        return null;
    }

}
