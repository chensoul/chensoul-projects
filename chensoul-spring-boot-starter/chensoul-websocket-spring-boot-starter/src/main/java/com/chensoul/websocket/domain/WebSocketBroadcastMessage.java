package com.chensoul.websocket.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@Accessors(chain = true)
public class WebSocketBroadcastMessage implements Serializable {

    private String payload;

}
