package com.chensoul.audit;

import com.chensoul.audit.spi.AuditActionResolver;
import com.chensoul.audit.spi.AuditResourceResolver;
import com.chensoul.audit.spi.ClientInfoResolver;
import com.chensoul.audit.spi.PrincipalResolver;
import com.chensoul.audit.spi.action.DefaultAuditActionResolver;
import com.chensoul.audit.spi.resource.RequestAsStringResourceResolver;
import com.chensoul.audit.spi.clientinfo.DefaultClientInfoResolver;
import com.chensoul.audit.spi.principal.SpringSecurityPrincipalResolver;
import com.chensoul.spring.client.ClientInfo;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Slf4j
public class AuditAspect {
    private final PrincipalResolver defaultPrincipalResolver = new SpringSecurityPrincipalResolver();
    private final AuditActionResolver defaultAuditActionResolver = new DefaultAuditActionResolver();
    private final AuditResourceResolver defaultAuditResourceResolver = new RequestAsStringResourceResolver();

    private final Map<String, AuditActionResolver> auditActionResolvers;

    private final Map<String, AuditResourceResolver> auditResourceResolvers;

    private final Map<String, PrincipalResolver> auditPrincipalResolvers;

    private final List<AuditManager> AuditManagers;

    private final String applicationCode;

    @Setter
    private ClientInfoResolver clientInfoResolver = new DefaultClientInfoResolver();

    @Setter
    private boolean failOnFailure = true;

    @Setter
    private boolean enabled = true;

    public AuditAspect(final String applicationCode,
                       final List<AuditManager> AuditManagers,
                       final Map<String, AuditActionResolver> auditActionResolverMap,
                       final Map<String, AuditResourceResolver> auditResourceResolverMap) {
        this(applicationCode,
            AuditManagers, auditActionResolverMap, auditResourceResolverMap,
            new HashMap<>());
    }

    public AuditAspect(final String applicationCode,
                       final List<AuditManager> AuditManagers,
                       final Map<String, AuditActionResolver> auditActionResolverMap,
                       final Map<String, AuditResourceResolver> auditResourceResolverMap,
                       final Map<String, PrincipalResolver> auditPrincipalResolvers) {
        this.auditPrincipalResolvers = auditPrincipalResolvers;
        this.AuditManagers = AuditManagers;
        this.applicationCode = applicationCode;
        auditActionResolvers = auditActionResolverMap;
        auditResourceResolvers = auditResourceResolverMap;
    }

    @Around(value = "@annotation(audit)", argNames = "audit")
    public Object handleAudit(final ProceedingJoinPoint joinPoint, final Audit audit) throws Throwable {
        final Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        if (!enabled) {
            return joinPoint.proceed();
        }
        final Long startTime = System.currentTimeMillis();

        final AuditActionResolver auditActionResolver = auditActionResolvers.getOrDefault(audit.actionResolverName(), defaultAuditActionResolver);
        final AuditResourceResolver auditResourceResolver = auditResourceResolvers.getOrDefault(audit.resourceResolverName(), defaultAuditResourceResolver);

        String currentPrincipal = null, action = null, exception = null;
        String[] auditResource = new String[]{null};
        Object retVal = null;
        boolean success = true;
        try {
            retVal = joinPoint.proceed();

            currentPrincipal = getCurrentPrincipal(joinPoint, audit, retVal);
            auditResource = auditResourceResolver.resolveFrom(joinPoint, retVal);
            action = auditActionResolver.resolveFrom(joinPoint, audit);

            if (log.isDebugEnabled()) {
                log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), retVal);
            }
            return retVal;
        } catch (final Throwable t) {
            success = false;
            exception = StringUtils.abbreviate(ExceptionUtils.getRootCauseMessage(t), 100);

            currentPrincipal = getCurrentPrincipal(joinPoint, audit, t);
            auditResource = auditResourceResolver.resolveFrom(joinPoint, t);
            action = auditActionResolver.resolveFrom(joinPoint, audit);

            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw t;
        } finally {
            final long cost = System.currentTimeMillis() - startTime;
            executeAuditCode(joinPoint, currentPrincipal, action, auditResource, retVal, success, exception, cost);
        }
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private static Logger logger(final JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    private String getCurrentPrincipal(final ProceedingJoinPoint joinPoint, final Audit audit, final Object retVal) {
        PrincipalResolver resolver = defaultPrincipalResolver;
        if (StringUtils.isNotBlank(audit.principalResolverName())) {
            resolver = auditPrincipalResolvers.getOrDefault(audit.principalResolverName(), defaultPrincipalResolver);
        }
        return resolver.resolveFrom(joinPoint, retVal);
    }

    private void executeAuditCode(final ProceedingJoinPoint joinPoint, final String currentPrincipal, final String action, final String[] auditableResources,
                                  final Object retVal, final boolean success, final String exception, final Long cost) {
        final String applicationCode = this.applicationCode;
        final ClientInfo clientInfo = clientInfoResolver.resolveFrom(joinPoint, retVal);
        final LocalDateTime operateTime = LocalDateTime.now(Clock.systemUTC());

        assertNotNull(currentPrincipal, "'username' cannot be null.\n" + getDiagnosticInfo(joinPoint));
        assertNotNull(action, "'action' cannot be null.\n" + getDiagnosticInfo(joinPoint));
        assertNotNull(applicationCode, "'applicationCode' cannot be null.\n" + getDiagnosticInfo(joinPoint));
        assertNotNull(operateTime, "'operateTime' cannot be null.\n" + getDiagnosticInfo(joinPoint));

        final AuditActionContext auditContext = new AuditActionContext(applicationCode, currentPrincipal, action, auditableResources,
            operateTime, success, exception, cost, clientInfo);

        try {
            for (final AuditManager manager : AuditManagers) {
                manager.record(auditContext);
            }
        } catch (final Throwable e) {
            if (failOnFailure) {
                throw e;
            }
            log.error("Failed to record audit context for " + auditContext.getAction() + " and principal " + auditContext.getUsername(), e);
        }
    }

    private static String getDiagnosticInfo(final ProceedingJoinPoint joinPoint) {
        return "Check the correctness of @Audit annotation at the following audit point: " + joinPoint.toLongString();
    }

    private static void assertNotNull(final Object o, final String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
