package com.chensoul.websocket.configuration;

import com.chensoul.websocket.properties.WebSocketProperties;
import com.chensoul.websocket.service.UserSessionManager;
import com.chensoul.websocket.service.impl.InMemoryUserSessionManager;
import com.chensoul.websocket.service.impl.SimpleWebSocketClusterMessageSender;
import com.chensoul.websocket.service.impl.SimpleWebSocketMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({WebSocketProperties.class})
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class SimpleWebSocketSenderConfiguration {

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(UserSessionManager.class)
    public UserSessionManager userSessionManager() {
        return new InMemoryUserSessionManager();
    }

    /**
     * @param userSessionManager
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "chensoul.websocket.isCluster", havingValue = "false", matchIfMissing = true)
    public SimpleWebSocketMessageSender simpleWebSocketMessageSender(UserSessionManager userSessionManager) {
        SimpleWebSocketMessageSender simpleWebSocketMessageSender = new SimpleWebSocketMessageSender(
                userSessionManager);
        log.debug("Simple WebSocket Message Sender Auto Configure.");
        return simpleWebSocketMessageSender;
    }

    /**
     * @param userSessionManager
     * @param redissonClient
     * @param webSocketProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "chensoul.websocket.isCluster", havingValue = "true")
    public SimpleWebSocketMessageSender simpleWebSocketClusterMessageSender(UserSessionManager userSessionManager,
                                                                            Redisson redissonClient, WebSocketProperties webSocketProperties) {
        SimpleWebSocketClusterMessageSender simpleWebSocketClusterMessageSender = new SimpleWebSocketClusterMessageSender(
                userSessionManager, redissonClient, webSocketProperties);
        log.debug("Simple WebSocket Cluster Message Sender Auto Configure.");
        return simpleWebSocketClusterMessageSender;
    }

}
