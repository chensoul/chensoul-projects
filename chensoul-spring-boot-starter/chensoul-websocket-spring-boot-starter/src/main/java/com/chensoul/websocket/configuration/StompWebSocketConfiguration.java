package com.chensoul.websocket.configuration;

import com.chensoul.websocket.handler.StompWebSocketHandshakeHandler;
import com.chensoul.websocket.interceptor.StompWebSocketChannelInterceptor;
import com.chensoul.websocket.properties.WebSocketProperties;
import com.chensoul.websocket.service.WebSocketMessageSender;
import com.chensoul.websocket.service.impl.StompWebSocketClusterMessageSender;
import com.chensoul.websocket.service.impl.StompWebSocketMessageSender;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Configuration
@ConditionalOnBean({RedissonClient.class})
@EnableConfigurationProperties({WebSocketProperties.class})
// @EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
// 开始支持@MessageMapping,就像是使用@requestMapping一样
@EnableWebSocketMessageBroker
public class StompWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketProperties webSocketProperties;

    /**
     * @return
     */
    @Bean
    public StompWebSocketChannelInterceptor webSocketChannelInterceptor() {
        StompWebSocketChannelInterceptor stompWebSocketChannelInterceptor = new StompWebSocketChannelInterceptor();
        return stompWebSocketChannelInterceptor;
    }

    /**
     * @return
     */
    @Bean
    public StompWebSocketHandshakeHandler webSocketHandshakeHandler() {
        StompWebSocketHandshakeHandler stompWebSocketHandshakeHandler = new StompWebSocketHandshakeHandler();
        stompWebSocketHandshakeHandler.setWebSocketProperties(webSocketProperties);
        return stompWebSocketHandshakeHandler;
    }

    /**
     * @param simpUserRegistry
     * @param simpMessagingTemplate
     * @return
     */
    @Bean
    public WebSocketMessageSender webSocketMessageSender(SimpUserRegistry simpUserRegistry,
                                                         SimpMessagingTemplate simpMessagingTemplate) {
        StompWebSocketMessageSender stompWebSocketMessageSender = new StompWebSocketMessageSender();
        stompWebSocketMessageSender.setSimpMessagingTemplate(simpMessagingTemplate);
        stompWebSocketMessageSender.setSimpUserRegistry(simpUserRegistry);
        stompWebSocketMessageSender.setWebSocketProperties(webSocketProperties);
        log.debug("Stomp WebSocket Message Sender Auto Configure.");
        return stompWebSocketMessageSender;
    }

    /**
     * @param stompWebSocketMessageSender
     * @param redissonClient
     * @return
     */
    @Bean
    @ConditionalOnBean({StompWebSocketMessageSender.class, RedissonClient.class})
    public StompWebSocketClusterMessageSender stompWebSocketClusterMessageSender(
            StompWebSocketMessageSender stompWebSocketMessageSender, RedissonClient redissonClient) {
        StompWebSocketClusterMessageSender stompWebSocketClusterMessageSender = new StompWebSocketClusterMessageSender();
        stompWebSocketClusterMessageSender.setWebSocketProperties(webSocketProperties);
        stompWebSocketClusterMessageSender.setWebSocketMessageSender(stompWebSocketMessageSender);
        stompWebSocketClusterMessageSender.setRedissonClient(redissonClient);
        log.debug("Stomp WebSocket Cluster Sender Auto Configure.");
        return stompWebSocketClusterMessageSender;
    }

    /**
     * 添加 Stomp Endpoint，创建配置客户端尝试连接地址，并对外暴露该接口，这样就可以通过websocket连接上服务
     *
     * @param registry {@link StompEndpointRegistry}
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个 Stomp 的节点(endpoint),并指定使用 SockJS 协议
        registry.addEndpoint(webSocketProperties.getEndpoint())
                .setAllowedOrigins("*")
                .setHandshakeHandler(webSocketHandshakeHandler())
                .withSockJS();
    }

    /**
     * 配置消息代理
     *
     * @param registry {@link MessageBrokerRegistry}
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*
         * enableStompBrokerRelay 配置外部的STOMP服务，需要安装额外的支持 比如rabbitmq或activemq 1.
         * 配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /topicTest 或者 /userTest 我们就可以在配置的域上向客户端推送消息 3.
         * 可以通过 setRelayHost 配置代理监听的host,默认为localhost 4. 可以通过 setRelayPort
         * 配置代理监听的端口，默认为61613 5. 可以通过 setClientLogin 和 setClientPasscode 配置账号和密码 6.
         * setxxx这种设置方法是可选的，根据业务需要自行配置，也可以使用默认配置
         */
        // registry.enableStompBrokerRelay("/topicTest", "/userTest")
        // .setRelayHost("rabbit.someotherserver")
        // .setRelayPort(62623);
        // .setClientLogin("userName")
        // .setClientPasscode("password");

        // 自定义调度器，用于控制心跳线程
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 线程池线程数，心跳连接开线程
        taskScheduler.setPoolSize(1);
        // 线程名前缀
        taskScheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        // 初始化
        taskScheduler.initialize();

        /*
         * spring 内置broker对象 广播式应配置一个/topic消息代理,点对点应配置一个/user消息代理， 1.
         * 配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /topic 或者 /private 我们就可以在配置的域上向客户端推送消息
         * 2，进行心跳设置，第一值表示server最小能保证发的心跳间隔毫秒数, 第二个值代码server希望client发的心跳间隔毫秒数 3.
         * 可以配置心跳线程调度器 setHeartbeatValue这个不能单独设置，不然不起作用，要配合setTaskScheduler才可以生效
         * 调度器我们可以自己写一个，也可以自己使用默认的调度器 new DefaultManagedTaskScheduler()
         */
        // registry.enableSimpleBroker(webSocketProperties.getBroadcast(),
        // webSocketProperties.getPeerToPeer())
        // .setHeartbeatValue(new long[]{10000, 10000})
        // .setTaskScheduler(taskScheduler);
        registry.enableSimpleBroker(webSocketProperties.getBroadcast(), webSocketProperties.getPeerToPeer());
        /*
         * 全局使用的消息前缀（客户端订阅路径上会体现出来） "/app" 为配置应用服务器的地址前缀，表示所有以/app 开头的客户端消息或请求
         * 都会路由到带有@MessageMapping 注解的方法中
         */
        String[] applicationDestinationPrefixes = webSocketProperties.getApplicationPrefixes();
        if (applicationDestinationPrefixes == null || applicationDestinationPrefixes.length == 0) {
            registry.setApplicationDestinationPrefixes(applicationDestinationPrefixes);
        }

        /*
         * 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/ 1. 配置一对一消息前缀， 客户端接收一对一消息需要配置的前缀
         * 如“'/user/'+userid + '/messages'”， 是客户端订阅一对一消息的地址 stompClient.subscribe js方法调用的地址
         * 2. 使用@SendToUser发送私信的规则不是这个参数设定，在框架内部是用UserDestinationMessageHandler处理， 而不是
         * AnnotationMethodMessageHandler 或 SimpleBrokerMessageHandler or
         * StompBrokerRelayMessageHandler，是在@SendToUser的URL前加“user+sessionId"组成
         */
        if (!StringUtils.isEmpty(webSocketProperties.getUserDestinationPrefix())) {
            registry.setUserDestinationPrefix(webSocketProperties.getUserDestinationPrefix());
        }

        /*
         * 自定义路径分割符 注释掉的这段代码添加的分割符为. 分割是类级别的@messageMapping和方法级别的@messageMapping的路径
         * 例如类注解路径为 “topic”,方法注解路径为“hello”，那么客户端JS stompClient.send
         * 方法调用的路径为“/app/topic.hello”
         * 注释掉此段代码后，类注解路径“/topic”,方法注解路径“/hello”,JS调用的路径为“/app/topic/hello”
         */
        // registry.setPathMatcher(new AntPathMatcher("."));
    }

    /**
     * 配置发送与接收的消息参数，可以指定消息字节大小，缓存大小，发送超时时间
     *
     * @param registration {@link WebSocketTransportRegistration}
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        /*
         * 1. setMessageSizeLimit 设置消息缓存的字节数大小 字节 2. setSendBufferSizeLimit
         * 设置websocket会话时，缓存的大小 字节 3. setSendTimeLimit 设置消息发送会话超时时间，毫秒
         */
        registration.setMessageSizeLimit(10240).setSendBufferSizeLimit(10240).setSendTimeLimit(10000);
    }

    /**
     * 采用自定义拦截器，获取connect时候传递的参数 设置输入消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     *
     * @param registration {@link ChannelRegistration}
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        /*
         * 配置消息线程池 1. corePoolSize 配置核心线程池，当线程数小于此配置时，不管线程中有无空闲的线程，都会产生新线程处理任务 2.
         * maxPoolSize 配置线程池最大数，当线程池数等于此配置时，不会产生新线程 3. keepAliveSeconds
         * 线程池维护线程所允许的空闲时间，单位秒
         */
        registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);

        /*
         * 添加stomp自定义拦截器，可以根据业务做一些处理 消息拦截器，实现ChannelInterceptor接口
         */
        registration.interceptors(webSocketChannelInterceptor());
    }

    /**
     * 设置输出消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     *
     * @param registration {@link ChannelRegistration}
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);
    }

    /**
     * 添加自定义的消息转换器，spring 提供多种默认的消息转换器
     *
     * @param messageConverters {@link MessageConverter}
     * @return 返回false, 不会添加消息转换器，返回true，会添加默认的消息转换器，当然也可以把自己写的消息转换器添加到转换链中
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }

}
