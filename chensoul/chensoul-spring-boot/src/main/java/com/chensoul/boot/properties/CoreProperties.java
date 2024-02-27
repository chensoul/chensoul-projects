package com.chensoul.boot.properties;

import com.chensoul.boot.properties.audit.AuditProperties;
import com.chensoul.boot.properties.logging.LoggingProperties;
import com.chensoul.boot.properties.web.ServerProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@ConfigurationProperties("chensoul")
@Getter
@Setter
@Accessors(chain = true)
@Validated
public class CoreProperties {
    @NestedConfigurationProperty
    private AuditProperties audit = new AuditProperties();

    @NestedConfigurationProperty
    private ServerProperties server = new ServerProperties();

    @NestedConfigurationProperty
    private LoggingProperties logging = new LoggingProperties();
}
