package com.chensoul.spring.boot.websocket.service.impl;

import com.chensoul.spring.boot.websocket.domain.WebSocketMessage;
import com.chensoul.spring.boot.websocket.service.UserSessionManager;
import com.chensoul.spring.boot.websocket.service.WebSocketMessageSender;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@AllArgsConstructor
public class SimpleWebSocketMessageSender implements WebSocketMessageSender {

    private UserSessionManager userSessionManager;

    @Override
    public void addUser(Principal principal, WebSocketSession session) {
        userSessionManager.add(principal, session);
    }

    @Override
    public void removeUser(WebSocketSession session) {
        userSessionManager.remove(session);
    }

    @Override
    public Map<WebSocketSession, Principal> getAllUsers() {
        return userSessionManager.getSessionPrincipalMap();
    }

    @Override
    public void toUser(WebSocketMessage<String> webSocketMessage) {
        Principal principal = userSessionManager.getPrincipalByName(webSocketMessage.getTo());
        if (principal == null) {
            return;
        }
        CopyOnWriteArraySet<WebSocketSession> webSocketSessions = userSessionManager.getSessionsByPrincipal(principal);
        if (CollectionUtils.isEmpty(webSocketSessions)) {
            return;
        }
        webSocketSessions.forEach(webSocketSession -> {
            sendMessage(principal, webSocketSession, webSocketMessage.getPayload());
        });
    }

    @Override
    public void toAll(String payload) {
        userSessionManager.getSessionPrincipalMap().forEach((webSocketSession, webSocketPrincipal) -> {
            sendMessage(webSocketPrincipal, webSocketSession, payload);
        });
    }

    protected <T> void sendMessage(Principal principal, WebSocketSession webSocketSession, String payload) {
        if (webSocketSession.isOpen()) {
            TextMessage textMessage = new TextMessage(payload);
            try {
                webSocketSession.sendMessage(textMessage);
                log.info("Success to send message to {}:{}", principal.getName(),
                        webSocketSession.getRemoteAddress().getPort());
            } catch (IOException e) {
                log.error("Fail to send message to {}:{}", principal.getName(),
                        webSocketSession.getRemoteAddress().getPort(), e);
            }
        }
    }

}
