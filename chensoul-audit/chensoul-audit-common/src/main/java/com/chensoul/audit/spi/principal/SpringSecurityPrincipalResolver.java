package com.chensoul.audit.spi.principal;

import com.chensoul.audit.spi.PrincipalResolver;
import org.aspectj.lang.JoinPoint;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Resolves the principal name to the one provided by Spring Security.
 */
public class SpringSecurityPrincipalResolver implements PrincipalResolver {

    @Override
    public String resolveFrom(final JoinPoint auditableTarget, final Object returnValue) {
        return getFromSecurityContext();
    }

    @Override
    public String resolve() {
        return getFromSecurityContext();
    }

    private String getFromSecurityContext() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext == null) {
            return ANONYMOUS_USER;
        }

        if (securityContext.getAuthentication() == null) {
            return ANONYMOUS_USER;
        }

        final String subject = securityContext.getAuthentication().getName();
        if (subject == null) {
            return UNKNOWN_USER;
        }
        return subject;
    }

}
