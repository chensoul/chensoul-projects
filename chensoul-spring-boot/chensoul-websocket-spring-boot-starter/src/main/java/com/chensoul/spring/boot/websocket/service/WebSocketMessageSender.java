package com.chensoul.spring.boot.websocket.service;

import com.chensoul.spring.boot.websocket.domain.WebSocketMessage;
import java.security.Principal;
import java.util.Map;
import org.springframework.web.socket.WebSocketSession;
public interface WebSocketMessageSender {

    /**
     * @param principal
     * @param session
     */
    void addUser(Principal principal, WebSocketSession session);

    /**
     * @param session
     */
    void removeUser(WebSocketSession session);

    /**
     * @return
     */
    Map<WebSocketSession, Principal> getAllUsers();

    /**
     * @param webSocketMessage
     */
    void toUser(WebSocketMessage<String> webSocketMessage);

    /**
     * @param payload
     */
    void toAll(String payload);

}
