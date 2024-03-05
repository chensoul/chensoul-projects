package com.chensoul.spring.boot.websocket.handler;

import com.chensoul.spring.boot.websocket.interceptor.SimpleWebSocketHandshakeInterceptor;
import com.chensoul.spring.boot.websocket.service.WebSocketMessageSender;
import com.chensoul.spring.boot.websocket.util.MicrometerHelper;
import java.io.IOException;
import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@AllArgsConstructor
@Slf4j
public class SimpleTextWebSocketHandler extends TextWebSocketHandler {

    private WebSocketMessageSender webSocketMessageSender;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(textMessage);
        }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        session.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) {
        log.warn("客户端异常：{}", session.getId(), throwable);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Principal principal = (Principal) session.getAttributes().get(SimpleWebSocketHandshakeInterceptor.WS_USER_KEY);
        if (principal != null) {
            MicrometerHelper.connectionEstablished();
            webSocketMessageSender.addUser(principal, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        MicrometerHelper.connectionClosed();
        webSocketMessageSender.removeUser(session);
    }

}
