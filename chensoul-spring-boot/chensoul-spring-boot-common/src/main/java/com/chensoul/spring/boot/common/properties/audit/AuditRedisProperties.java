package com.chensoul.spring.boot.common.properties.audit;

import com.chensoul.spring.boot.common.properties.redis.BaseRedisProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Configuration properties for Redis.
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditRedisProperties extends BaseRedisProperties {
    private static final long serialVersionUID = -8112996050439638782L;

    /**
     * Execute the recording of audit records in async manner.
     * This setting must almost always be set to true.
     */
    private boolean asynchronous = true;
}
