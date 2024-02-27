package com.chensoul.audit;

import com.chensoul.spring.client.ClientInfo;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Audit action context
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class AuditActionContext implements Serializable {
    private static final long serialVersionUID = -3530737409883959089L;

    private String application;

    private String username;

    private String action;

    private String[] resource;

    private LocalDateTime operateTime;

    private boolean success;

    private String exception;

    private Long cost;

    private ClientInfo clientInfo;
}
