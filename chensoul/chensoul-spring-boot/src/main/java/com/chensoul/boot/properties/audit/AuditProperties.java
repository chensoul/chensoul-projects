package com.chensoul.boot.properties.audit;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link AuditProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditProperties implements Serializable {

    private static final long serialVersionUID = 3946106584608417663L;

    /**
     * Core auditing engine functionality and settings
     * are captured here, separate from audit storage services.
     */
    @NestedConfigurationProperty
    private AuditEngineProperties engine = new AuditEngineProperties();

    /**
     * Family of sub-properties pertaining to Jdbc-based audit destinations.
     */
    @NestedConfigurationProperty
    private AuditJdbcProperties jdbc = new AuditJdbcProperties();

    /**
     * Family of sub-properties pertaining to Redis-based audit destinations.
     */
    @NestedConfigurationProperty
    private AuditRedisProperties redis = new AuditRedisProperties();

    /**
     * Family of sub-properties pertaining to rest-based audit destinations.
     */
    @NestedConfigurationProperty
    private AuditRestProperties rest = new AuditRestProperties();

    /**
     * Family of sub-properties pertaining to file-based audit destinations.
     */
    @NestedConfigurationProperty
    private AuditSlf4jProperties slf4j = new AuditSlf4jProperties();


    /**
     * Family of sub-properties pertaining to groovy-based audit destinations.
     */
    @NestedConfigurationProperty
    private AuditGroovyProperties groovy = new AuditGroovyProperties();
}
