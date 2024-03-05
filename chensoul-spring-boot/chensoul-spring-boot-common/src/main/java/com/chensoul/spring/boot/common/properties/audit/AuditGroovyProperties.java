package com.chensoul.spring.boot.common.properties.audit;

import com.chensoul.spring.boot.common.properties.SpringResourceProperties;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link AuditGroovyProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditGroovyProperties implements Serializable {

    private static final long serialVersionUID = 4887475246873585918L;

    /**
     * Groovy template that constructs the audit payload.
     */
    @NestedConfigurationProperty
    private SpringResourceProperties template = new SpringResourceProperties();

    private boolean enabled = true;

}
