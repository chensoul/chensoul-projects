package com.chensoul.audit.spi;

import org.aspectj.lang.JoinPoint;
public interface PrincipalResolver {

    /**
     * Default String that can be used when the user is anonymous.
     */
    final String ANONYMOUS_USER = "anonymous";

    /**
     * Default String that can be used when the user cannot be determined.
     */
    final String UNKNOWN_USER = "unknown";

    /**
     * Resolve the principal performing an audit-able action.
     * <p>
     * Note, this method should NEVER throw an exception *unless* the expectation is that a failed resolution causes
     * the entire transaction to fail.  Otherwise use {@link PrincipalResolver#UNKNOWN_USER}.
     *
     * @param auditTarget the join point where we're auditing.
     * @param returnValue the returned value
     * @return The principal as a String. CANNOT be NULL.
     */
    String resolveFrom(JoinPoint auditTarget, Object returnValue);

    /**
     * Called when there is no other way to resolve the principal (i.e. an error was captured, auditing was not
     * called, etc.)
     * <p>
     * Note, this method should NEVER throw an exception *unless* the expectation is that a failed resolution causes
     * the entire transaction to fail.  Otherwise use {@link PrincipalResolver#UNKNOWN_USER}.
     *
     * @return the principal.  CANNOT be NULL.
     */
    default String resolve() {
        return UNKNOWN_USER;
    }
}
