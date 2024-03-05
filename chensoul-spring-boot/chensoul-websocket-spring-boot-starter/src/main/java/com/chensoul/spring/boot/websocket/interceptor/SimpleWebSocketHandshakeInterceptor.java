package com.chensoul.spring.boot.websocket.interceptor;

import static com.chensoul.constant.StringPool.NULL;
import com.chensoul.spring.util.HttpRequestUtils;
import com.chensoul.util.StringUtils;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@AllArgsConstructor
public class SimpleWebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    public static final String WS_USER_KEY = "ws.user";
    public static final String WS_TENANT_ID = "ws.tenant_id";
    public static final String WS_MODULE_CODE = "ws.module_code";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        String tenantId = HttpRequestUtils.getValueFromRequest(((ServletServerHttpRequest) request).getServletRequest(), "TENANT-ID");

//		if (StringUtils.isBlank(tenantId) || StringUtils.equals(tenantId, StringPool.NULL)) {
//			log.warn("websocket 握手失败, tenantId 为空");
//			return false;
//		}

        if (log.isDebugEnabled()) {
            log.debug("websocket 握手开始, {}, {}", tenantId, request.getPrincipal().getName());
        }

        attributes.put(WS_TENANT_ID, StringUtils.defaultIfBlank(tenantId, NULL));
//		attributes.put(WS_MODULE_CODE, StringUtils.defaultIfBlank(moduleCode, NULL));
        attributes.put(WS_USER_KEY, request.getPrincipal());

        return super.beforeHandshake(request, response, webSocketHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        super.afterHandshake(request, response, wsHandler, exception);

        if (log.isDebugEnabled()) {
            log.debug("websocket 握手结束, 当前用户 {}", request.getPrincipal().getName());
        }
    }

}
