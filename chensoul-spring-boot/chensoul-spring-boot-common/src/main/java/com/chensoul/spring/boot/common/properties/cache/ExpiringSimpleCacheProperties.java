package com.chensoul.spring.boot.common.properties.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link ExpiringSimpleCacheProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExpiringSimpleCacheProperties extends SimpleCacheProperties {
    private static final long serialVersionUID = -268826011744304210L;

    /**
     * Cache duration specifies the fixed duration for an
     * entry to be automatically removed from the cache after its creation.
     */
    private String duration = "PT15M";
}
