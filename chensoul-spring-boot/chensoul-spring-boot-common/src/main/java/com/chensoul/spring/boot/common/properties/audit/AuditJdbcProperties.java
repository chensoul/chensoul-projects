package com.chensoul.spring.boot.common.properties.audit;

import com.chensoul.spring.boot.common.properties.jpa.AbstractJpaProperties;
import com.chensoul.spring.boot.common.properties.quartz.SchedulingProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link AuditJdbcProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class AuditJdbcProperties extends AbstractJpaProperties {

    private static final long serialVersionUID = 4227475246873515918L;

    private boolean enabled = true;

    /**
     * Execute the recording of audit records in async manner.
     * This setting must almost always be set to true.
     */
    private boolean asynchronous = true;

    /**
     * Indicates how long audit records should be kept in the database.
     * This is used by the clean-up criteria to clean up after stale audit records.
     */
    private int maxAgeDays = 180;

    /**
     * Allows one to trim the audit data by the specified length.
     * A negative value disables the trimming process where the audit
     * functionality no longer substrings the audit record.
     */
    private int columnLength = 512;

    /**
     * SQL query that provides a template to fetch audit records.
     * Accepts two parameters using {@code %s} for table name and
     * audit date.
     */
    private String selectSqlQueryTemplate;

    /**
     * Indicate the date formatter pattern used to fetch
     * audit records from the database based on the record date.
     * Default value is {@code yyyy-MM-dd 00:00:00.000000}.
     */
    private String dateFormatterPattern;

    /**
     * A formatter function that receives the formatted date value
     * and the date pattern as the first and second argument.
     * Example: {@code TO_DATE('%s', '%s')}.
     */
    private String dateFormatterFunction;

    /**
     * Scheduler settings to indicate how often the cleaner is reloaded.
     */
    @NestedConfigurationProperty
    private SchedulingProperties schedule = new SchedulingProperties();
}
