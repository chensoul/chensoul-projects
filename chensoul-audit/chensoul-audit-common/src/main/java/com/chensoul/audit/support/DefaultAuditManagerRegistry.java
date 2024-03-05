package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import com.chensoul.audit.AuditManager;
import com.chensoul.audit.AuditManagerRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * DefaultAuditManagerRegistry is responsible for registering and retrieving AuditManager instances.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
public class DefaultAuditManagerRegistry implements AuditManagerRegistry {
    private final List<AuditManager> auditManagers = new ArrayList<>(0);

    @Override
    public void registerAuditManager(final AuditManager manager) {
        auditManagers.add(manager);
    }

    @Override
    public void record(final AuditActionContext audit) {
        auditManagers.forEach(manager -> manager.record(audit));
    }

    @Override
    public Set<AuditActionContext> getAuditRecords(final Map<AuditManager.WhereClauseFields, Object> criteria) {
        return auditManagers
            .stream()
            .map(manager -> manager.getAuditRecords(criteria))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }
}
