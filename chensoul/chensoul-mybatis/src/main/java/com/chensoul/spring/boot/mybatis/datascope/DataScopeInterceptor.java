package com.chensoul.spring.boot.mybatis.datascope;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class DataScopeInterceptor implements Interceptor {

    private IDataScopeProvider dataScopeProvider;

    /**
     * @param dataScopeProvider
     */
    public DataScopeInterceptor(IDataScopeProvider dataScopeProvider) {
        if (null != dataScopeProvider) {
            this.dataScopeProvider = dataScopeProvider;
            DataScopeHelper.init();
        }
    }

    /**
     * @param invocation
     * @return
     * @throws Throwable
     */
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType != SqlCommandType.UNKNOWN && sqlCommandType != SqlCommandType.FLUSH) {
            if (null != dataScopeProvider) {
                dataScopeProvider.sqlRender(args, mappedStatement, sqlCommandType);
            }
        }

        return invocation.proceed();
    }

    /**
     * @param var1
     * @return
     */
    public Object plugin(Object var1) {
        return var1 instanceof Executor ? Plugin.wrap(var1, this) : var1;
    }

}
