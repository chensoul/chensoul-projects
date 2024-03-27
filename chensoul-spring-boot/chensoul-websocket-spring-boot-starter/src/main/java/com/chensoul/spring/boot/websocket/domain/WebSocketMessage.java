package com.chensoul.spring.boot.websocket.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class WebSocketMessage<T> implements Serializable {

    private String from;

    private String to;

    private String channel;

    private T payload;

}
