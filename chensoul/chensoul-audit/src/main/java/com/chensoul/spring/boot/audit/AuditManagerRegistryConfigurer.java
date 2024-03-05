package com.chensoul.spring.boot.audit;

/**
 * AuditManagerRegistryConfigurer is responsible for configuring AuditManagerRegistry.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface AuditManagerRegistryConfigurer {

    /**
     * @param auditManagerRegistry the audit manager registry
     */
    void configure(AuditManagerRegistry auditManagerRegistry);

    /**
     * Gets name.
     *
     * @return the name
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
