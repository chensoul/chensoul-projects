package com.chensoul.spring.boot.common.properties.web;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link ClientProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class ClientProperties implements Serializable {

    private static final long serialVersionUID = -32143821503580896L;

    private boolean enabled = true;

    private String allowedClientIpRegex = ".+";

    private String deniedClientIpRegex;

    @NestedConfigurationProperty
    private HttpProperties httpProperties = new HttpProperties();
}
