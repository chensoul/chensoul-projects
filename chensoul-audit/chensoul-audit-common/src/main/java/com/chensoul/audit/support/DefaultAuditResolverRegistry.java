package com.chensoul.audit.support;

import com.chensoul.audit.spi.AuditActionResolver;
import com.chensoul.audit.spi.AuditResourceResolver;
import com.chensoul.audit.AuditResolverRegistry;
import com.chensoul.audit.spi.PrincipalResolver;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

/**
 * DefaultAuditResolverRegistry is responsible for registering and retrieving AuditResourceResolver instances.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
public class DefaultAuditResolverRegistry implements AuditResolverRegistry {
    private final Map<String, AuditResourceResolver> auditResourceResolvers = new LinkedHashMap<>();
    private final Map<String, AuditActionResolver> auditActionResolvers = new LinkedHashMap<>();
    private final Map<String, PrincipalResolver> auditPrincipalResolvers = new LinkedHashMap<>();

    @Override
    public void registerAuditResourceResolver(final String key, final AuditResourceResolver resolver) {
        auditResourceResolvers.putIfAbsent(key, resolver);
    }

    @Override
    public void registerAuditPrincipalResolver(final String key, final PrincipalResolver resolver) {
        auditPrincipalResolvers.putIfAbsent(key, resolver);
    }

    @Override
    public void registerAuditActionResolver(final String key, final AuditActionResolver resolver) {
        auditActionResolvers.putIfAbsent(key, resolver);
    }

    @Override
    public void registerAuditActionResolvers(final Map<String, AuditActionResolver> resolvers) {
        auditActionResolvers.putAll(resolvers);
    }

    @Override
    public void registerAuditResourceResolvers(final Map<String, AuditResourceResolver> resolvers) {
        auditResourceResolvers.putAll(resolvers);
    }
}
