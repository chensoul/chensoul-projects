package com.chensoul.audit.spi.action;

import com.chensoul.audit.Audit;
import com.chensoul.audit.spi.AuditActionResolver;
import org.aspectj.lang.JoinPoint;

/**
 * DefaultAuditActionResolver is responsible for resolving the audit action from the {@link Audit} annotation.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DefaultAuditActionResolver implements AuditActionResolver {
    @Override
    public String resolveFrom(final JoinPoint auditableTarget, final Audit audit) {
        return audit.value();
    }
}
