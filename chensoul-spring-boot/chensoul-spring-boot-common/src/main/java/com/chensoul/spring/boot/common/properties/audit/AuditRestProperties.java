package com.chensoul.spring.boot.common.properties.audit;

import com.chensoul.spring.boot.common.properties.rest.RestEndpointProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link AuditRestProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditRestProperties extends RestEndpointProperties {

    private static final long serialVersionUID = 3893437775090452831L;

    /**
     * Make storage requests asynchronously.
     */
    private boolean asynchronous = true;
}
