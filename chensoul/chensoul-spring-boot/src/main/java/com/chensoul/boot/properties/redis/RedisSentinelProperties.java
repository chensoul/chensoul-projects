package com.chensoul.boot.properties.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link RedisSentinelProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class RedisSentinelProperties implements Serializable {
    private static final long serialVersionUID = 5434823157764550831L;

    /**
     * Name of Redis server.
     */
    private String master;

    /**
     * Login password of the sentinel server.
     */
    private String password;

    /**
     * list of host:port pairs.
     */
    private List<String> node = new ArrayList<>(0);
}
