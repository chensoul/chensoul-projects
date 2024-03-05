package com.chensoul.spring.boot.audit.support;

import com.chensoul.spring.boot.audit.AuditActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slf4jAuditManager is responsible for recording AuditActionContext instances to a slf4j logger.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class Slf4jAuditManager extends AbstractStringAuditManager {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void record(final AuditActionContext auditActionContext) {
        log.info(toString(auditActionContext));
    }

    @Override
    public void removeAll() {
    }
}
