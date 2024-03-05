package com.chensoul.mybatis.spring.boot.configuration;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@ConfigurationProperties(prefix = "chensoul.tenant")
public class TenantProperties {

    /**
     * 是否开启租户模式
     */
    private Boolean enabled = true;

    private List<String> includeTables = new ArrayList<>();

    private List<String> tables = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的表名
     */
    private List<String> excludeTables = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的sql 需要配置mapper的全路径如：com.central.user.mapper.SysUserMapper.findList
     */
    private List<String> excludeSqls = new ArrayList<>();

    /**
     * @return
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param tables
     */
    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    /**
     * @return
     */
    public List<String> getIncludeTables() {
        if (CollectionUtils.isNotEmpty(tables)) {
            return tables;
        }
        return includeTables;
    }

    /**
     * @param includeTables
     */
    public void setIncludeTables(List<String> includeTables) {
        this.includeTables = includeTables;
    }

    /**
     * @return
     */
    public List<String> getExcludeTables() {
        return excludeTables;
    }

    /**
     * @param excludeTables
     */
    public void setExcludeTables(List<String> excludeTables) {
        this.excludeTables = excludeTables;
    }

    /**
     * @return
     */
    public List<String> getExcludeSqls() {
        return excludeSqls;
    }

    /**
     * @param excludeSqls
     */
    public void setExcludeSqls(List<String> excludeSqls) {
        this.excludeSqls = excludeSqls;
    }

}
