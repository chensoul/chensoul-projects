package com.chensoul.audit.support;

import com.chensoul.audit.AuditActionContext;
import com.chensoul.audit.AuditManager;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.DisposableBean;

/**
 * JdbcAuditManager is responsible for recording AuditActionContext instances to a database.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class JdbcAuditManager extends AbstractAsyncAuditManager implements AuditManager, DisposableBean {
    private static final String INSERT_SQL_TEMPLATE = "INSERT INTO %s (" +
                                                      Arrays.stream(AuditableFields.values())
                                                          .map(AuditableFields::getName)
                                                          .collect(Collectors.joining(",")) + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_SQL_TEMPLATE = "DELETE FROM %s %s";

    private final String tableName = "sys_audit";
    private final String selectByDateSqlTemplate = "SELECT * FROM %s WHERE %s ORDER BY " + AuditableFields.OPERATE_TIME.getName() + " DESC";
    private final String dateFormatterPattern = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void saveAuditRecord(final AuditActionContext audit) {

    }

    @Override
    public Set<? extends AuditActionContext> getAuditRecords(final Map<WhereClauseFields, Object> whereClause) {
        return null;
    }

    @Override
    public void removeAll() {
    }
}
