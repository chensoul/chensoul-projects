package com.chensoul.spring.boot.audit.spi;

import com.chensoul.jackson.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;


/**
 * A spi interface for resolving the auditable resource.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface AuditResourceResolver {

    /**
     * Resolve the auditable resource.
     *
     * @param joinPoint   the join point that contains the arguments.
     * @param returnValue The returned value
     * @return The resource String.
     */
    String[] resolveFrom(JoinPoint joinPoint, Object returnValue);

    /**
     * Resolve the auditable resource for an audit-able action that has
     * incurred an exception.
     *
     * @param joinPoint the join point that contains the arguments.
     * @param throwable The exception incurred when the join point proceeds.
     * @return The resource String.
     */
    String[] resolveFrom(JoinPoint joinPoint, Throwable throwable);

    default String toResourceString(final Object arg) {
        return JsonUtils.toJson(arg);
    }

}
