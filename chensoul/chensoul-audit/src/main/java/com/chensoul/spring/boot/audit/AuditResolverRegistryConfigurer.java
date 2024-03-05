package com.chensoul.spring.boot.audit;

import org.springframework.core.Ordered;

/**
 * AuditResolverRegistryConfigurer is responsible for configuring AuditResolverRegistry.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface AuditResolverRegistryConfigurer extends Ordered {

    /**
     * Configure audit trail resolution plan.
     *
     * @param auditResolverRegistry
     */
    void configure(AuditResolverRegistry auditResolverRegistry);

    /**
     * Gets name.
     *
     * @return the name
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
