package com.chensoul.mybatis.spring.boot.tenant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
class TenantContextHolderTest {
    private String TEST = "TEST";

    @Test
    void setTenantId() {
        TenantContextHolder.setTenantId(TEST);
        assertEquals(TEST, TenantContextHolder.getTenantId());
        TenantContextHolder.clear();
        assertEquals(null, TenantContextHolder.getTenantId());
    }

    @Test
    void testSubThread() throws InterruptedException {
        TenantContextHolder.setTenantId(TEST);
        assertEquals(TEST, TenantContextHolder.getTenantId());

        Thread childThread = new Thread(() -> {
            assertEquals(TEST, TenantContextHolder.getTenantId());

            TenantContextHolder.setTenantId("TEST2");
            assertEquals("TEST2", TenantContextHolder.getTenantId());

            TenantContextHolder.clear();
            assertEquals(null, TenantContextHolder.getTenantId());
        });

        childThread.start();
        childThread.join();

        assertEquals(TEST, TenantContextHolder.getTenantId());
    }
}
