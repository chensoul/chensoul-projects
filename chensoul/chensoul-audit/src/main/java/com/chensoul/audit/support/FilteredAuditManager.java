package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import com.chensoul.audit.AuditManager;
import com.chensoul.jackson.utils.JsonUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * FilteredAuditManager is responsible for filtering and recording AuditActionContext instances.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@RequiredArgsConstructor
@Slf4j
public class FilteredAuditManager implements AuditManager, ApplicationEventPublisherAware {

    private final Collection<AuditManager> auditManagers;

    private final List<String> supportedActions;

    private final List<String> excludedActions;

    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void record(final AuditActionContext ctx) {
        boolean matched = actionMatched(ctx);
        if (matched) {
            log.trace("Recording audit action context [{}]", ctx);
            auditManagers.forEach(mgr -> mgr.record(ctx));

            if (applicationEventPublisher != null) {
                //TODO filter headers
                AuditApplicationEvent auditEvent = new AuditApplicationEvent(ctx.getUsername(),
                        ctx.getApplication(),
                        ctx.getUsername(),
                        ctx.getAction(),
                        JsonUtils.toJson(ctx.getResource()),
                        ctx.getOperateTime().toString(),
                        ctx.getClientInfo().getClientIp(),
                        ctx.getClientInfo().getServerIp(),
                        ctx.getClientInfo().getUserAgent(),
                        ctx.getClientInfo().getLocale().getLanguage());
                applicationEventPublisher.publishEvent(auditEvent);
            }
        } else {
            log.trace("Skipping to record audit action context [{}] as it's not authorized as an audit action among [{}]",
                    ctx, supportedActions);
        }
    }

    private boolean actionMatched(AuditActionContext ctx) {
        boolean matched = supportedActions
                .stream()
                .anyMatch(action -> {
                    String actionPerformed = ctx.getAction();
                    return "*".equals(action) || Pattern.compile(action).matcher(actionPerformed).find();
                });

        if (matched) {
            matched = excludedActions
                    .stream()
                    .noneMatch(action -> {
                        String actionPerformed = ctx.getAction();
                        return "*".equals(action) || Pattern.compile(action).matcher(actionPerformed).find();
                    });
        }
        return matched;
    }

    @Override
    public Set<? extends AuditActionContext> getAuditRecords(final Map<WhereClauseFields, Object> params) {
        return auditManagers
                .stream()
                .map(mgr -> mgr.getAuditRecords(params))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeAll() {
        auditManagers.forEach(AuditManager::removeAll);
    }

}

