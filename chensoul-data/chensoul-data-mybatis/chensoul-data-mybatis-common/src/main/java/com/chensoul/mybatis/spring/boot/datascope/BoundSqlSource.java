package com.chensoul.mybatis.spring.boot.datascope;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class BoundSqlSource implements SqlSource {

    private BoundSql boundSql;

    /**
     * @param boundSql
     */
    public BoundSqlSource(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    /**
     * @param var1
     * @return
     */
    public BoundSql getBoundSql(Object var1) {
        return this.boundSql;
    }

}
