package com.chensoul.websocket.service;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface UserSessionManager {

    /**
     * @param principal
     * @param session
     */
    void add(Principal principal, WebSocketSession session);

    /**
     * @param name
     * @return
     */
    CopyOnWriteArraySet<WebSocketSession> getSessionsByName(String name);

    /**
     * @param principal
     * @return
     */
    CopyOnWriteArraySet<WebSocketSession> getSessionsByPrincipal(Principal principal);

    /**
     * @param name
     * @return
     */
    Principal getPrincipalByName(String name);

    /**
     * @param session
     */
    void remove(WebSocketSession session);

    /**
     * @return
     */
    Map<WebSocketSession, Principal> getSessionPrincipalMap();

}
