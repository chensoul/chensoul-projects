package com.chensoul.boot.properties.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link RestEndpointProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class RestEndpointProperties extends BaseRestEndpointProperties {
    private static final long serialVersionUID = 2687020856160473089L;

    /**
     * HTTP method to use when contacting the rest endpoint.
     * Examples include {@code GET, POST}, etc.
     */
    private String method = "GET";
}
