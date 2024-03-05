package com.chensoul.spring.boot.common.properties.jpa;

import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * This is {@link DatabaseProperties}.
 */
@Getter
@Setter
@Accessors(chain = true)
public class DatabaseProperties implements Serializable {

    private static final long serialVersionUID = 7740236971148591965L;

    /**
     * Whether SQL queries should be displayed in the console/logs.
     */
    private boolean showSql;

    /**
     * Whether to generate DDL after the EntityManagerFactory has been initialized creating/updating all relevant tables.
     */
    private boolean genDdl = true;

    /**
     * When choosing physical table names, determine whether names
     * should be considered case-insensitive.
     */
    private boolean caseInsensitive;

    /**
     * Indicate a physical table name
     * to be used by the hibernate naming strategy
     * in case table names need to be customized for the
     * specific type of database. The key here indicates
     * the CAS-provided table name and the value is the
     * translate physical name for the database. If a match
     * is not found for the CAS-provided table name, then that
     * name will be used by default.
     */
    private Map<String, String> physicalTableNames = new LinkedCaseInsensitiveMap<>();
}
