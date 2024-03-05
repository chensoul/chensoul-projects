package com.chensoul.spring.boot.common.properties.logging;

import lombok.Data;

@Data
public class LogstashProperties {
    private boolean enabled = false;
    private String host = "localhost";
    private int port = 5000;
    private int ringBufferSize = 512;
}
