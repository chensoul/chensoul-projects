package com.chensoul.mybatis.spring.boot.tenant;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.NULL;
import static com.chensoul.mybatis.spring.boot.MybatisConstants.HEADER_TENANT_ID;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 */
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    public static String extractTenantId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        if (!StringUtils.hasText(tenantId)) {
            tenantId = request.getParameter(HEADER_TENANT_ID);
        }
        return tenantId;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = extractTenantId(httpServletRequest);

        if (!StringUtils.hasText(tenantId)) {
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

}
