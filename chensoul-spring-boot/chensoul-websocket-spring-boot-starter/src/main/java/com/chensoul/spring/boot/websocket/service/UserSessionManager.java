package com.chensoul.spring.boot.websocket.service;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.web.socket.WebSocketSession;
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
