package com.chensoul.spring.boot.mybatis.datascope;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface IDataScopeProvider {

    /**
     * @param args
     * @param mappedStatement
     * @param sqlCommandType
     * @throws Exception
     */
    void sqlRender(Object[] args, MappedStatement mappedStatement, SqlCommandType sqlCommandType) throws Exception;

}
