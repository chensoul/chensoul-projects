package com.chensoul.spring.boot.websocket.properties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
@Data
@ConfigurationProperties(prefix = "rose.websocket")
public class WebSocketProperties {

    private static final String SLASH = "/";

    private Boolean isCluster = false;

    /**
     * 客户端尝试连接端点
     */
    private String endpoint = "/ws";

    /**
     * WebSocket 广播消息代理地址
     */
    private String broadcast = "/topic";

    /**
     * WebSocket 点对点消息代理地址
     */
    private String peerToPeer = "/queue";

    /**
     * 全局使用的消息前缀
     */
    private List<String> applicationDestinationPrefixes = Arrays.asList("/app");

    /**
     * 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user
     */
    private String userDestinationPrefix = "/user";

    /**
     * 集群模式下，信息同步消息队列Topic
     */
    private String topic = "ws";

    /**
     * 请求中传递的用户身份标识属性名
     */
    private String principalAttribute = "token";

    private String format(String endpoint) {
        if (!StringUtils.isEmpty(endpoint) && !StringUtils.startsWithIgnoreCase(endpoint, SLASH)) {
            return SLASH + endpoint;
        } else {
            return endpoint;
        }
    }

    /**
     * @return
     */
    public String[] getApplicationPrefixes() {
        List<String> prefixes = this.getApplicationDestinationPrefixes();
        if (!CollectionUtils.isEmpty(prefixes)) {
            List<String> wellFormed = prefixes.stream().map(this::format).collect(Collectors.toList());
            String[] result = new String[wellFormed.size()];
            return wellFormed.toArray(result);
        } else {
            return new String[]{};
        }
    }

}
