package com.chensoul.spring.boot.audit.spi;

import com.chensoul.spring.boot.audit.Audit;
import org.aspectj.lang.JoinPoint;


/**
 * An spi interface for resolving the audit action from the {@link Audit} annotation.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface AuditActionResolver {
    String resolveFrom(JoinPoint joinPoint, Audit audit);

}
