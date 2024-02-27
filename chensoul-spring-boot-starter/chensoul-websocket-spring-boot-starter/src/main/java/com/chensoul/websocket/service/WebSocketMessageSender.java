package com.chensoul.websocket.service;

import com.chensoul.websocket.domain.WebSocketMessage;
import java.security.Principal;
import java.util.Map;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
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
