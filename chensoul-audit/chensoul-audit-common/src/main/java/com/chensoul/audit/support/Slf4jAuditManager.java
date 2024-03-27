package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
