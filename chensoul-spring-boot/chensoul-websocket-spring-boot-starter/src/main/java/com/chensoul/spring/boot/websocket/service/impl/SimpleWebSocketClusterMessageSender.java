package com.chensoul.spring.boot.websocket.service.impl;

import com.chensoul.spring.boot.websocket.domain.WebSocketBroadcastMessage;
import com.chensoul.spring.boot.websocket.domain.WebSocketMessage;
import com.chensoul.spring.boot.websocket.properties.WebSocketProperties;
import com.chensoul.spring.boot.websocket.service.UserSessionManager;
import com.chensoul.spring.boot.websocket.service.WebSocketMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class SimpleWebSocketClusterMessageSender extends SimpleWebSocketMessageSender
        implements InitializingBean, WebSocketMessageSender {

    private RedissonClient redissonClient;

    private WebSocketProperties webSocketProperties;

    /**
     * @param userSessionManager
     * @param redissonClient
     * @param webSocketProperties
     */
    public SimpleWebSocketClusterMessageSender(UserSessionManager userSessionManager, RedissonClient redissonClient,
                                               WebSocketProperties webSocketProperties) {
        super(userSessionManager);
        this.redissonClient = redissonClient;
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    public void toUser(WebSocketMessage<String> webSocketMessage) {
        RTopic rTopic = redissonClient.getTopic(webSocketProperties.getTopic());
        rTopic.publish(webSocketMessage);
    }

    @Override
    public void toAll(String payload) {
        RTopic rTopic = redissonClient.getTopic(webSocketProperties.getTopic());
        rTopic.publish(new WebSocketBroadcastMessage().setPayload(payload));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RTopic topic = redissonClient.getTopic(webSocketProperties.getTopic());
        topic.addListener(WebSocketMessage.class,
                (MessageListener<WebSocketMessage<String>>) (charSequence, webSocketMessage) -> {
                    log.debug("Redisson received spring socket sync messages [{}]", webSocketMessage);
                    super.toUser(webSocketMessage);
                });

        topic.addListener(WebSocketBroadcastMessage.class, (charSequence, webSocketBroadcastMessage) -> {
            log.debug("Redisson received spring socket sync message [{}]", webSocketBroadcastMessage);
            super.toAll(webSocketBroadcastMessage.getPayload());
        });
    }

}
