package com.chensoul.mybatis.spring.boot.configuration;

import static com.chensoul.mybatis.spring.boot.MybatisConstants.HEADER_TENANT_ID;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensoul.mybatis.spring.boot.tenant.TenantContextHolder;
import com.chensoul.mybatis.spring.boot.tenant.TenantFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 租户信息拦截
 */
@AutoConfiguration
public class FeignTenantConfiguration {

    /**
     * @return
     */
    @Bean
    public RequestInterceptor feignTenantInterceptor() {
        return new FeignTenantInterceptor();
    }

    /**
     *
     */
    @Slf4j
    public static class FeignTenantInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate requestTemplate) {
            String tenantId = TenantContextHolder.getTenantId();
            if (StringUtils.isBlank(tenantId)) {
                RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    tenantId = TenantFilter.extractTenantId(((ServletRequestAttributes) attributes).getRequest());
                }
            }

            if (StringUtils.isNotBlank(tenantId)) {
                //兼容 websocket
                requestTemplate.header(HEADER_TENANT_ID, tenantId);
            }
        }
    }
}
