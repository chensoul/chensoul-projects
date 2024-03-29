package com.chensoul.spring.boot.websocket.configuration;

import com.chensoul.spring.boot.websocket.handler.SimpleTextWebSocketHandler;
import com.chensoul.spring.boot.websocket.interceptor.SimpleWebSocketHandshakeInterceptor;
import com.chensoul.spring.boot.websocket.properties.WebSocketProperties;
import com.chensoul.spring.boot.websocket.service.impl.SimpleWebSocketMessageSender;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
@EnableWebSocket
@Configuration
@AllArgsConstructor
@Import({SimpleWebSocketSenderConfiguration.class})
@EnableConfigurationProperties({WebSocketProperties.class})
public class SimpleWebSocketConfiguration implements WebSocketConfigurer {

    private SimpleWebSocketMessageSender simpleWebSocketMessageSender;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws").setAllowedOrigins("*").addInterceptors(handshakeInterceptor());
    }

    /**
     * @return
     */
    @Bean
    public HttpSessionHandshakeInterceptor handshakeInterceptor() {
        return new SimpleWebSocketHandshakeInterceptor();
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(WebSocketHandler.class)
    public WebSocketHandler webSocketHandler() {
        return new SimpleTextWebSocketHandler(simpleWebSocketMessageSender);
    }

    /**
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

}
