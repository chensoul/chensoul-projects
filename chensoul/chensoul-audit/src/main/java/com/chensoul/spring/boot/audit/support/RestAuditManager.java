package com.chensoul.spring.boot.audit.support;

import com.chensoul.spring.boot.audit.AuditActionContext;
import com.chensoul.spring.boot.audit.AuditManager;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * RestAuditManager is responsible for recording AuditActionContext instances to RESTful API.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class RestAuditManager extends AbstractAsyncAuditManager {
    public RestAuditManager(final boolean asynchronous) {
        super(asynchronous);
    }

    @Override
    public void saveAuditRecord(final AuditActionContext audit) {
    }

    @Override
    public Set<? extends AuditActionContext> getAuditRecords(final Map<AuditManager.WhereClauseFields, Object> whereClause) {
        return null;
    }

    @Override
    public void removeAll() {

    }
}
