package com.chensoul.websocket.interceptor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Setter
public class StompWebSocketChannelInterceptor implements ChannelInterceptor {

    /**
     * 在消息发送之前调用，方法中可以对消息进行修改，如果此方法返回值为空，则不会发生实际的消息发送调用
     *
     * @param message {@link Message}
     * @param channel {@link MessageChannel}
     * @return {@link Message}
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
//        }
        return message;
    }

    /**
     * 在消息发送后立刻调用
     *
     * @param message {@link Message}
     * @param channel {@link MessageChannel}
     * @param sent    boolean值参数表示该调用的返回值
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        /*
         * 拿到消息头对象后，我们可以做一系列业务操作 1. 通过getSessionAttributes()方法获取到websocketSession，
         * 就可以取到我们在WebSocketHandshakeInterceptor拦截器中存在session中的信息 2.
         * 我们也可以获取到当前连接的状态，做一些统计，例如统计在线人数，或者缓存在线人数对应的令牌，方便后续业务调用
         */
//        HttpSession httpSession = (HttpSession) accessor.getSessionAttributes().get("HTTP_SESSION");

    }

    /**
     * 1. 在消息发送完成后调用，而不管消息发送是否产生异常，在此方法中，我们可以做一些资源释放清理的工作 2.
     * 此方法的触发必须是preSend方法执行成功，且返回值不为null,发生了实际的消息推送，才会触发
     *
     * @param message {@link Message}
     * @param channel {@link MessageChannel}
     * @param sent    boolean值参数表示该调用的返回值
     * @param ex      失败时抛出的 Exception
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

    }

    /**
     * 在消息被实际检索之前调用, 只适用于(PollableChannels, 轮询场景)，在websocket的场景中用不到
     *
     * @param channel channel {@link MessageChannel}
     * @return 如果返回false, 则不会对检索任何消息
     */
    @Override
    public boolean preReceive(MessageChannel channel) {
        return Boolean.FALSE;
    }

    /**
     * 在检索到消息之后，返回调用方之前调用，可以进行信息修改。适用于PollableChannels，轮询场景
     *
     * @param message {@link Message}
     * @param channel {@link MessageChannel}
     * @return 如果返回null, 就不会进行下一步操作
     */
    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return message;
    }

    /**
     * 1. 在消息接收完成之后调用，不管发生什么异常，可以用于消息发送后的资源清理 2. 只有当preReceive 执行成功，并返回true才会调用此方法 3.
     * 适用于PollableChannels，轮询场景
     *
     * @param message {@link Message}
     * @param channel {@link MessageChannel}
     * @param ex      失败时抛出的 Exception
     */
    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {

    }

}
