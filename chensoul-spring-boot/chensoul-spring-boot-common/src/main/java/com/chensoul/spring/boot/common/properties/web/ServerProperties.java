package com.chensoul.spring.boot.common.properties.web;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link ServerProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class ServerProperties implements Serializable {

    private static final long serialVersionUID = 7876382696803430817L;

    @NestedConfigurationProperty
    private ClientProperties client = new ClientProperties();

    @NestedConfigurationProperty
    private ApiDocProperties apiDoc = new ApiDocProperties();

}
