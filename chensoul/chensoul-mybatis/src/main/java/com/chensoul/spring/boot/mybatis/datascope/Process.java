package com.chensoul.spring.boot.mybatis.datascope;

import net.sf.jsqlparser.statement.Statement;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@FunctionalInterface
public interface Process {

    /**
     * @param statement
     * @param index
     */
    void process(Statement statement, int index);

}
