package com.chensoul.reflect;

import static com.chensoul.reflect.ProxyUtils.isProxyable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
public class ProxyUtilsTest {

    @Test
    public void testIsProxyable() {
        assertTrue(isProxyable(getClass()));
        assertFalse(isProxyable(int.class));
        assertFalse(isProxyable(int[].class));
        assertFalse(isProxyable(String.class));
        assertFalse(isProxyable(A.class));
        assertFalse(isProxyable(B.class));
    }

    static class A {
        A(Object... args) {
        }
    }

    static class B {
        public final String toString() {
            return "B";
        }
    }
}
