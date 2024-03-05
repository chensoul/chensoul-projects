package com.chensoul.spring.boot.common.properties.audit;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This is {@link AuditSlf4jProperties}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditSlf4jProperties implements Serializable {

    private static final long serialVersionUID = 4227475246873515918L;

    private boolean useSingleLine;

    private String auditableFields = "application,username,action,resource,operate_time,success,exception,cost,client_ip,server_ip,user_agent,headers";

    private boolean enabled = true;
}
