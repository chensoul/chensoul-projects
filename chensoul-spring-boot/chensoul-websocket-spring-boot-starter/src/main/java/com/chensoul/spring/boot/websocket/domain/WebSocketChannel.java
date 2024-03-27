package com.chensoul.spring.boot.websocket.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public enum WebSocketChannel {

    /**
     * 个人通知
     */
    NOTICE("/notice", "个人通知"), JOIN("/join", "加入"), LEAVE("/leave", "离开");

    private static final Map<String, WebSocketChannel> INDEX_MAP = new HashMap<>();

    private static final List<Map<String, Object>> JSON_STRUCT = new ArrayList<>();

    static {
        for (WebSocketChannel webSocketChannel : WebSocketChannel.values()) {
            INDEX_MAP.put(webSocketChannel.name(), webSocketChannel);
            Map<String, Object> map = new HashMap<>();
            map.put("value", webSocketChannel.ordinal());
            map.put("key", webSocketChannel.name());
            map.put("text", webSocketChannel.getDescription());

            JSON_STRUCT.add(webSocketChannel.ordinal(), map);
        }
    }

    private final String destination;

    private final String description;

    WebSocketChannel(String destination, String description) {
        this.destination = destination;
        this.description = description;
    }

    /**
     * @param code
     * @return
     */
    public static WebSocketChannel getWebSocketChannel(String code) {
        return INDEX_MAP.get(code);
    }

    /**
     * @return
     */
    public static List<Map<String, Object>> getToJsonStruct() {
        return JSON_STRUCT;
    }

    /**
     * @return
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

}
