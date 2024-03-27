package com.chensoul.mybatis.spring.boot.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.chensoul.mybatis.spring.boot.datascope.DataScopeInterceptor;
import com.chensoul.mybatis.spring.boot.datascope.IDataScopeProvider;
import com.chensoul.mybatis.spring.boot.handler.DefaultDataPermissionHandler;
import com.chensoul.mybatis.spring.boot.handler.DefaultMetaObjectHandler;
import com.chensoul.mybatis.spring.boot.interceptor.CustomPaginationInnerInterceptor;
import com.chensoul.mybatis.spring.boot.tenant.CustomTenantInterceptor;
import com.chensoul.mybatis.spring.boot.tenant.CustomTenantLineHandler;
import com.chensoul.mybatis.spring.boot.tenant.TenantFilter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties({TenantProperties.class})
public class MybatisPlusConfiguration implements WebMvcConfigurer {

    @Autowired(required = false)
    private IDataScopeProvider dataScopeProvider;

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(getLocalAddress());
    }

    private InetAddress getLocalAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();
                final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress.isSiteLocalAddress()
                        && !inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
                        return inetAddress;
                    }
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return null;
    }

    /**
     * @param tenantProperties
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantProperties tenantProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        if (tenantProperties != null && BooleanUtils.isTrue(tenantProperties.getEnabled())) {
            CustomTenantLineHandler tenantLineHandler = new CustomTenantLineHandler(tenantProperties.getIncludeTables(), tenantProperties.getExcludeTables());
            CustomTenantInterceptor tenantInterceptor = new CustomTenantInterceptor(tenantLineHandler, tenantProperties.getExcludeSqls());
            interceptor.addInnerInterceptor(tenantInterceptor);
        }
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(dataPermissionHandler()));
        interceptor.addInnerInterceptor(new CustomPaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "chensoul.tenant.enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean tenantFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new TenantFilter());
        registration.addUrlPatterns("/*");
        registration.setName("tenantFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new DefaultMetaObjectHandler();
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DataPermissionHandler dataPermissionHandler() {
        return new DefaultDataPermissionHandler();
    }


    /**
     * @return
     */
    @Order
    @Bean
    @ConditionalOnMissingBean
    public DataScopeInterceptor dataScopeInterceptor() {
        return null == this.dataScopeProvider ? null : new DataScopeInterceptor(this.dataScopeProvider);
    }

}
