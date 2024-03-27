package com.chensoul.mybatis.spring.boot.tenant;
public abstract class TenantContextHolder {
    private static final ThreadLocal<String> TENANT = new InheritableThreadLocal<>();

    /**
     *
     */
    private TenantContextHolder() {
    }

    /**
     * Get TenantId
     *
     * @return {@link String}
     */
    public static String getTenantId() {
        return TENANT.get();
    }

    /**
     * Set TenantId
     *
     * @param tenantId
     */
    public static void setTenantId(String tenantId) {
        TENANT.set(tenantId);
    }

    /**
     * Clear TenantId
     */
    public static void clear() {
        TENANT.remove();
    }
}
