package com.chensoul.websocket.service.impl;

import com.chensoul.websocket.domain.WebSocketMessage;
import com.chensoul.websocket.exception.IllegalChannelException;
import com.chensoul.websocket.exception.PrincipalNotFoundException;
import com.chensoul.websocket.properties.WebSocketProperties;
import com.chensoul.websocket.service.WebSocketMessageSender;
import java.security.Principal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class StompWebSocketClusterMessageSender implements InitializingBean, WebSocketMessageSender {

    private RedissonClient redissonClient;

    private WebSocketProperties webSocketProperties;

    private StompWebSocketMessageSender stompWebSocketMessageSender;

    /**
     * @param redissonClient
     */
    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * @param webSocketProperties
     */
    public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    /**
     * @param stompWebSocketMessageSender
     */
    public void setWebSocketMessageSender(StompWebSocketMessageSender stompWebSocketMessageSender) {
        this.stompWebSocketMessageSender = stompWebSocketMessageSender;
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

    @Override
    public void toUser(WebSocketMessage<String> webSocketMessage) {
        try {
            stompWebSocketMessageSender.toUser(webSocketMessage);
        } catch (PrincipalNotFoundException e) {
            RTopic rTopic = redissonClient.getTopic(webSocketProperties.getTopic(), new JsonJacksonCodec());
            rTopic.publish(webSocketMessage);
            log.debug("Current instance can not found user [{}], publish messages.", webSocketMessage.getTo());
        } catch (IllegalChannelException e) {
            log.error("Web socket channel is incorrect.");
        }
    }

    @Override
    public void toAll(String payload) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RTopic topic = redissonClient.getTopic(webSocketProperties.getTopic());
        topic.addListener(WebSocketMessage.class,
                (MessageListener<WebSocketMessage<String>>) (charSequence, webSocketMessage) -> {
                    log.debug("Redisson received spring socket sync messages [{}]", webSocketMessage);
                    stompWebSocketMessageSender.toUser(webSocketMessage);
                });
    }

}
