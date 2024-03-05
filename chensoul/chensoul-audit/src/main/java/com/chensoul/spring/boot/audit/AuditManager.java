package com.chensoul.spring.boot.audit;

import java.util.Map;
import java.util.Set;

/**
 * AuditManager is responsible for recording audit records.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface AuditManager {
    public enum AuditableFields {
        APPLICATION("APPLICATION"),
        USERNAME("USERNAME"),
        ACTION("ACTION"),
        RESOURCE("RESOURCE"),
        OPERATE_TIME("OPERATE_TIME"),
        SUCCESS("SUCCESS"),
        EXCEPTION("EXCEPTION"),
        COST("COST"),
        CLIENT_IP("CLIENT_IP"),
        SERVER_IP("SERVER_IP"),
        USER_AGENT("USER_AGENT"),
        HEADERS("HEADERS");

        private final String name;

        AuditableFields(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum WhereClauseFields {
        DATE,
        USERNAME
    }

    /**
     * Make an audit trail record. Implementations could use any type of back end medium to serialize audit trail
     * data i.e. RDBMS, log file, IO stream, SMTP, JMS queue or what ever else imaginable.
     * <p>
     * This concept is somewhat similar to log4j Appender.
     *
     * @param auditActionContext the audit action context
     */
    void record(AuditActionContext auditActionContext);

    /**
     * Gets audit records since.
     *
     * @param whereClause the where clause
     * @return the audit records since
     */
    Set<? extends AuditActionContext> getAuditRecords(Map<WhereClauseFields, Object> whereClause);

    /**
     * Remove all.
     */
    default void removeAll() {
    }
}
