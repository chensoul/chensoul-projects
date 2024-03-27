package com.chensoul.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
public class ModifierTest {

    @Test
    public void testGetValue() {
        for (Modifier modifier : Modifier.values()) {
            assertEquals(modifier.getValue(), findModifierValue(modifier.name()));
        }
    }

    @Test
    public void testMatches() {
        for (Modifier modifier : Modifier.values()) {
            assertTrue(modifier.matches(modifier.getValue()));
        }
    }

    private int findModifierValue(String name) {
        return FieldUtils.getStaticFieldValue(java.lang.reflect.Modifier.class, name);
    }
}
