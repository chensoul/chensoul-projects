package com.chensoul.spring.boot.audit;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AuditManagerRegistry is responsible for registering and retrieving AuditManager instances.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface AuditManagerRegistry {
    /**
     * Register audit trail manager.
     *
     * @param manager the manager
     */
    void registerAuditManager(AuditManager manager);

    /**
     * Gets audit trail managers.
     *
     * @return the audit trail managers
     */
    List<AuditManager> getAuditManagers();

    /**
     * Record.
     *
     * @param audit the audit
     */
    void record(AuditActionContext audit);

    /**
     * Gets audit records for the specified query.
     *
     * @param criteria the criteria
     * @return the audit records since
     */
    Set<AuditActionContext> getAuditRecords(Map<AuditManager.WhereClauseFields, Object> criteria);
}
