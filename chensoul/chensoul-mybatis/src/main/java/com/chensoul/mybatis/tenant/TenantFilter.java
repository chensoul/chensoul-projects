package com.chensoul.mybatis.tenant;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.NULL;
import static com.chensoul.mybatis.MybatisConstants.HEADER_TENANT_ID;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 */
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = extractTenantId(httpServletRequest);

        if (StringUtils.isBlank(tenantId)) {
            tenantId = NULL;
        }

        String uri = httpServletRequest.getRequestURI();
        log.info("{}, {}", uri, tenantId);


        TenantContextHolder.setTenantId(tenantId);
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            TenantContextHolder.clear();
        }
    }

    public static String extractTenantId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        if (StringUtils.isBlank(tenantId)) {
            tenantId = request.getParameter(HEADER_TENANT_ID);
        }
        return tenantId;
    }

}
