package com.chensoul.spring.boot.websocket.service.impl;

import com.chensoul.spring.boot.websocket.domain.WebSocketChannel;
import com.chensoul.spring.boot.websocket.domain.WebSocketMessage;
import com.chensoul.spring.boot.websocket.exception.IllegalChannelException;
import com.chensoul.spring.boot.websocket.exception.PrincipalNotFoundException;
import com.chensoul.spring.boot.websocket.properties.WebSocketProperties;
import com.chensoul.spring.boot.websocket.service.WebSocketMessageSender;
import java.security.Principal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class StompWebSocketMessageSender implements WebSocketMessageSender {

    private SimpMessagingTemplate simpMessagingTemplate;

    private SimpUserRegistry simpUserRegistry;

    private WebSocketProperties webSocketProperties;

    /**
     * @param simpMessagingTemplate
     */
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * @param simpUserRegistry
     */
    public void setSimpUserRegistry(SimpUserRegistry simpUserRegistry) {
        this.simpUserRegistry = simpUserRegistry;
    }

    /**
     * @param webSocketProperties
     */
    public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    public void addUser(Principal principal, WebSocketSession session) {

    }

    @Override
    public void removeUser(WebSocketSession session) {

    }

    @Override
    public Map<WebSocketSession, Principal> getAllUsers() {
        return null;
    }

    /**
     * 发送给指定用户信息。
     *
     * @param webSocketMessage 发送内容参数实体 {@link WebSocketMessage}
     */
    public void toUser(WebSocketMessage<String> webSocketMessage) {
        WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannel(webSocketMessage.getChannel());
        if (ObjectUtils.isEmpty(webSocketChannel)) {
            throw new IllegalChannelException("Web socket channel is incorrect!");
        }

        SimpUser simpUser = simpUserRegistry.getUser(webSocketMessage.getTo());
        if (ObjectUtils.isEmpty(simpUser)) {
            throw new PrincipalNotFoundException("Web socket user principal is not found!");
        }

        log.debug("Web socket send message to user [{}].", webSocketMessage.getTo());
        simpMessagingTemplate.convertAndSendToUser(webSocketMessage.getTo(), webSocketChannel.getDestination(),
                webSocketMessage.getPayload());
    }

    /**
     * 广播 WebSocket 信息
     *
     * @param payload 发送的内容
     */
    public void toAll(String payload) {
        simpMessagingTemplate.convertAndSend(webSocketProperties.getBroadcast(), payload);
    }

}
