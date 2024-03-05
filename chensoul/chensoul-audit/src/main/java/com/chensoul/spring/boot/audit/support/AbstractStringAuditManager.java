package com.chensoul.spring.boot.audit.support;

import com.chensoul.spring.boot.audit.AuditActionContext;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.chensoul.spring.boot.audit.AuditManager;
import com.chensoul.jackson.support.JacksonObjectMapperFactory;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.CollectionUtils;

/**
 * Abstract string audit manager that dumps auditable information to string.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class AbstractStringAuditManager implements AuditManager {
    private final ObjectMapper MAPPER = JacksonObjectMapperFactory.builder().build().toObjectMapper();

    private boolean useSingleLine = false;

    private List<AuditableFields> auditableFields;

    public void setUseSingleLine(final boolean useSingleLine) {
        this.useSingleLine = useSingleLine;
    }

    public void setAuditableFields(final List<AuditableFields> auditableFields) {
        this.auditableFields = auditableFields;
    }

    @Override
    public Set<? extends AuditActionContext> getAuditRecords(final Map<WhereClauseFields, Object> whereClause) {
        return new HashSet<>();
    }

    protected String toString(final AuditActionContext auditActionContext) {
        final StringBuilder builder = new StringBuilder();
        try {
            final ObjectWriter writer = useSingleLine
                ? MAPPER.writer(new MinimalPrettyPrinter())
                : MAPPER.writerWithDefaultPrettyPrinter();

            builder.append(writer.writeValueAsString(getMappedAuditActionContext(auditActionContext)));

            if (!useSingleLine) {
                builder.append("\n");
            }
        } catch (final Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return builder.toString();
    }

    /**
     * Gets mapped audit action context.
     * The returned map is only used when audit format is set to JSON.
     *
     * @param auditActionContext the audit action context
     * @return the mapped audit action context
     */
    protected Map<String, Object> getMappedAuditActionContext(final AuditActionContext auditActionContext) {
        final Map<String, Object> map = new LinkedHashMap();
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.USERNAME)) {
            map.put(AuditableFields.USERNAME.getName().toLowerCase(), auditActionContext.getUsername());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.ACTION)) {
            map.put(AuditableFields.ACTION.getName().toLowerCase(), auditActionContext.getAction());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.RESOURCE)) {
            map.put(AuditableFields.RESOURCE.getName().toLowerCase(), auditActionContext.getResource());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.APPLICATION)) {
            map.put(AuditableFields.APPLICATION.getName().toLowerCase(), auditActionContext.getApplication());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.OPERATE_TIME)) {
            map.put(AuditableFields.OPERATE_TIME.getName().toLowerCase(), auditActionContext.getOperateTime().toString());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.SUCCESS)) {
            map.put(AuditableFields.SUCCESS.getName().toLowerCase(), String.valueOf(auditActionContext.isSuccess()));
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.EXCEPTION)) {
            map.put(AuditableFields.EXCEPTION.getName().toLowerCase(), auditActionContext.getException());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.COST)) {
            map.put(AuditableFields.COST.getName().toLowerCase(), String.valueOf(auditActionContext.getCost()));
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.CLIENT_IP)) {
            map.put(AuditableFields.CLIENT_IP.getName().toLowerCase(), auditActionContext.getClientInfo().getClientIp());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.SERVER_IP)) {
            map.put(AuditableFields.SERVER_IP.getName().toLowerCase(), auditActionContext.getClientInfo().getServerIp());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.USER_AGENT)) {
            map.put(AuditableFields.USER_AGENT.getName().toLowerCase(), auditActionContext.getClientInfo().getUserAgent());
        }
        if (CollectionUtils.isEmpty(auditableFields) || auditableFields.contains(AuditableFields.HEADERS)) {
            map.put(AuditableFields.HEADERS.getName().toLowerCase(), auditActionContext.getClientInfo().getHeaders());
        }
        return map;
    }
}
