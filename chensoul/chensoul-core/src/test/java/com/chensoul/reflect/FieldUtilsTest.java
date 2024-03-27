package com.chensoul.reflect;

import static com.chensoul.reflect.FieldUtils.findField;
import static com.chensoul.reflect.FieldUtils.setFieldValue;
import static com.chensoul.reflect.FieldUtils.setStaticFieldValue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

/**
 * {@link FieldUtils} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class FieldUtilsTest {

    private static String value = "1";

    @Test
    public void testFindField() {
        Class<?> klass = String.class;
        assertEquals(char[].class, findField(klass, "value").getType());

        klass = StringBuilder.class;
        assertEquals(char[].class, findField(klass, "value").getType());
        assertEquals(int.class, findField(klass, "count").getType());
    }

    @Test
    public void testGetFieldValue() {
        String value = "Hello,World";
        assertArrayEquals(value.toCharArray(), FieldUtils.getFieldValue(value, "value", char[].class));
    }

    @Test
    public void testGetStaticFieldValue() {
        assertSame(System.in, FieldUtils.getStaticFieldValue(System.class, "in"));
        assertSame(System.out, FieldUtils.getStaticFieldValue(System.class, "out"));
    }

    @Test
    public void testSetFieldValue() {
        Integer value = 999;
        setFieldValue(value, "value", 2);
        assertEquals(value.intValue(), 2);
    }

    @Test
    public void testStaticFieldValue() {
        setStaticFieldValue(getClass(), "value", "abc");
        assertEquals("abc", value);
    }
}
