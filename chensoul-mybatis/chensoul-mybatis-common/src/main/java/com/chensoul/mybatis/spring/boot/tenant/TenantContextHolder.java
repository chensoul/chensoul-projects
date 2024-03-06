package com.chensoul.mybatis.spring.boot.tenant;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.NULL;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 *
 */
public class TenantContextHolder {
    private static final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

    /**
     *
     */
    private TenantContextHolder() {
    }

    /**
     * @return
     */
    public static String getTenantId() {
        String tenantId = THREAD_LOCAL_TENANT.get();

        if (StringUtils.isBlank(tenantId)) {
            tenantId = NULL;
        }
        return tenantId;
    }

    /**
     * @param tenantId
     */
    public static void setTenantId(String tenantId) {
        THREAD_LOCAL_TENANT.set(tenantId);
    }

    /**
     *
     */
    public static void clear() {
        THREAD_LOCAL_TENANT.remove();
    }

    /**
     * @param tenantId
     * @return
     */
    public static boolean isValid(String tenantId) {
        return StringUtils.isNotBlank(tenantId) && !StringUtils.equals(tenantId, NULL);
    }

}
