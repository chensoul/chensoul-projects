package com.chensoul.spring.boot.websocket.service.impl;

import com.chensoul.spring.boot.websocket.service.UserSessionManager;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;
@Slf4j
public class InMemoryUserSessionManager implements UserSessionManager {

    private static Map<Principal, CopyOnWriteArraySet<WebSocketSession>> principalSessionListMap = new ConcurrentHashMap<>();

    private static Map<WebSocketSession, Principal> sessionPrincipalMap = new ConcurrentHashMap<>();

    private static Map<String, Principal> namePrincipalMap = new ConcurrentHashMap<>();

    /**
     * @param principal
     * @param session
     */
    public void add(Principal principal, WebSocketSession session) {
        if (!principalSessionListMap.containsKey(principal)) {
            principalSessionListMap.put(principal, new CopyOnWriteArraySet<>());
        }
        if (!sessionPrincipalMap.containsKey(session)) {
            sessionPrincipalMap.put(session, principal);
        }
        if (!namePrincipalMap.containsKey(principal.getName())) {
            namePrincipalMap.put(principal.getName(), principal);
        }

        principalSessionListMap.get(principal).add(session);

        log.debug("User {}:{} login", principal.getName(), session.getRemoteAddress().getPort());
    }

    /**
     * @param name
     * @return
     */
    public CopyOnWriteArraySet<WebSocketSession> getSessionsByName(String name) {
        if (!namePrincipalMap.containsKey(name)) {
            return null;
        }
        return principalSessionListMap.get(namePrincipalMap.get(name));
    }

    /**
     * @param principal
     * @return
     */
    public CopyOnWriteArraySet<WebSocketSession> getSessionsByPrincipal(Principal principal) {
        return principalSessionListMap.getOrDefault(principal, new CopyOnWriteArraySet<>());
    }

    /**
     * @param name
     * @return
     */
    public Principal getPrincipalByName(String name) {
        return namePrincipalMap.get(name);
    }

    /**
     * @param session
     */
    public void remove(WebSocketSession session) {
        Principal principal = sessionPrincipalMap.remove(session);
        if (principal != null) {
            CopyOnWriteArraySet<WebSocketSession> webSocketSessions = principalSessionListMap.get(principal);
            if (webSocketSessions != null) {
                webSocketSessions.remove(session);
                if (webSocketSessions.isEmpty()) {
                    principalSessionListMap.remove(principal);
                    namePrincipalMap.remove(principal.getName());
                }
            }
            log.debug("User {}:{} logout", principal.getName(), session.getRemoteAddress().getPort());
        }
    }

    /**
     * @return
     */
    public Map<WebSocketSession, Principal> getSessionPrincipalMap() {
        return sessionPrincipalMap;
    }

}
