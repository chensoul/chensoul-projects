package com.chensoul.audit.support;


import com.chensoul.audit.AuditActionContext;

/**
 * ConsoleAuditManager is responsible for printing audit records to console.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class ConsoleAuditManager extends AbstractStringAuditManager {

    @Override
    public void record(final AuditActionContext auditActionContext) {
        System.out.println(toString(auditActionContext));
    }

    @Override
    public void removeAll() {
    }
}
