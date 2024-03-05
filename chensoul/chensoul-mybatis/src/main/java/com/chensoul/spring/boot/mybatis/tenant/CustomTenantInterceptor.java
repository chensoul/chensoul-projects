package com.chensoul.spring.boot.mybatis.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class CustomTenantInterceptor extends TenantLineInnerInterceptor {

    private List<String> excludeSqls;

    /**
     * @param tenantLineHandler
     * @param excludeSqls
     */
    public CustomTenantInterceptor(TenantLineHandler tenantLineHandler, List<String> excludeSqls) {
        super(tenantLineHandler);
        this.excludeSqls = excludeSqls;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (isIgnoreMappedStatement(ms.getId())) {
            return;
        }
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    /**
     * @param msId
     * @return
     */
    private boolean isIgnoreMappedStatement(String msId) {
        return excludeSqls.stream().anyMatch((e) -> e.equalsIgnoreCase(msId));
    }

}
