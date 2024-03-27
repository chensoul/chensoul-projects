package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import com.chensoul.audit.AuditManager;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
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
