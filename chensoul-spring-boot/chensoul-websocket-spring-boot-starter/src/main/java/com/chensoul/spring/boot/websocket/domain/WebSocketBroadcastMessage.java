package com.chensoul.spring.boot.websocket.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class WebSocketBroadcastMessage implements Serializable {

    private String payload;

}
