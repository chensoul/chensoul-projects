package com.chensoul.boot.properties.redis;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link RedisClusterNodeProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class RedisClusterNodeProperties implements Serializable {
    private static final long serialVersionUID = 2912983343579258662L;

    /**
     * Server's host address.
     */
    private String host;

    /**
     * Server's port number.
     */
    private int port;

    /**
     * Set the id of the master node.
     */
    private String replicaOf;

    /**
     * Identifier of this node.
     */
    private String id;

    /**
     * Name of this node.
     */
    private String name;

    /**
     * Indicate the type/role of this node.
     * Accepted values are: {@code MASTER, REPLICA}.
     */
    private String type;
}
