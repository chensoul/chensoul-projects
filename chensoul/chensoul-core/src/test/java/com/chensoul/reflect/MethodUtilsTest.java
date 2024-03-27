package com.chensoul.reflect;

import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
public class MethodUtilsTest {

    /**
     * Test {@link MethodUtils#getSignature(Method)}
     */
    @Test
    public void testGetSignature() {
        Method method = null;

        // Test non-argument Method
        method = MethodUtils.findMethod(this.getClass(), "testGetSignature");
        assertEquals("com.chensoul.reflect.MethodUtilsTest#testGetSignature()", MethodUtils.getSignature(method));

        // Test one-argument Method
        method = MethodUtils.findMethod(Object.class, "equals", Object.class);
        assertEquals("java.lang.Object#equals(java.lang.Object)", MethodUtils.getSignature(method));

        // Test two-argument Method
        method = MethodUtils.findMethod(MethodUtils.class, "findMethod", Class.class, String.class, Class[].class);
        assertEquals("com.chensoul.reflect.MethodUtils#findMethod(java.lang.Class,java.lang.String,java.lang.Class[])", MethodUtils.getSignature(method));

    }
}
