package com.chensoul.mybatis.spring.boot.datascope;

import net.sf.jsqlparser.statement.Statement;
@FunctionalInterface
public interface Process {

    /**
     * @param statement
     * @param index
     */
    void process(Statement statement, int index);

}
