package com.chensoul.mybatis.datascope;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class MappedStatementHelper {
    private MappedStatementHelper() {
    }

    /**
     * @param invocation
     * @param function
     */
    public static void convertSql(Invocation invocation, Function<String, String> function) {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        args[0] = build(mappedStatement, new BoundSqlSource(new BoundSql(mappedStatement.getConfiguration(),
                function.apply(boundSql.getSql()), boundSql.getParameterMappings(), boundSql.getParameterObject())));
    }

    /**
     * @param mappedStatement
     * @param sqlSource
     * @return
     */
    public static MappedStatement build(MappedStatement mappedStatement, SqlSource sqlSource) {
        Builder builder = new Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource,
                mappedStatement.getSqlCommandType());
        builder.resource(mappedStatement.getResource());
        builder.parameterMap(mappedStatement.getParameterMap());
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.timeout(mappedStatement.getTimeout());
        builder.statementType(mappedStatement.getStatementType());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());
        builder.resultOrdered(mappedStatement.isResultOrdered());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        if (null != mappedStatement.getKeyProperties() && mappedStatement.getKeyProperties().length > 0) {
            builder.keyProperty(
                    Arrays.stream(mappedStatement.getKeyProperties()).collect(Collectors.joining(",")));
        }

        if (null != mappedStatement.getKeyColumns() && mappedStatement.getKeyColumns().length > 0) {
            builder.keyColumn(Arrays.stream(mappedStatement.getKeyColumns()).collect(Collectors.joining(",")));
        }

        builder.databaseId(mappedStatement.getDatabaseId());
        builder.lang(mappedStatement.getLang());
        if (null != mappedStatement.getResultSets() && mappedStatement.getResultSets().length > 0) {
            builder.resultSets(Arrays.stream(mappedStatement.getResultSets()).collect(Collectors.joining(",")));
        }

        return builder.build();
    }

    /**
     * @param mappedStatement
     * @param boundSql
     * @return
     */
    public static MappedStatement build(MappedStatement mappedStatement, BoundSql boundSql) {
        return build(mappedStatement, new BoundSqlSource(boundSql));
    }

}
