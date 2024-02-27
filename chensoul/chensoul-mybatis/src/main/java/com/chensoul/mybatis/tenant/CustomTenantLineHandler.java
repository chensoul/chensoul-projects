package com.chensoul.mybatis.tenant;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class CustomTenantLineHandler implements TenantLineHandler {

    private List<String> includeTables;

    private List<String> excludeTables;

    @Override
    public Expression getTenantId() {
        String tenant = TenantContextHolder.getTenantId();
        if (StringUtils.isNotEmpty(tenant)) {
            return new StringValue(TenantContextHolder.getTenantId());
        }
        return new NullValue();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        //优先判断是否排除
        if (CollectionUtils.isNotEmpty(excludeTables)) {
            return excludeTables.stream().anyMatch(t -> t.equalsIgnoreCase(tableName));
        }

        //其次，判断是否包含
        if (CollectionUtils.isNotEmpty(includeTables)) {
            return includeTables.stream().noneMatch(t -> t.equalsIgnoreCase(tableName));
        }
        //没有配置，则不忽略任何表
        return Boolean.FALSE;
    }

}
