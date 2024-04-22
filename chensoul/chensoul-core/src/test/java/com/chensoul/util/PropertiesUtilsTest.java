package com.chensoul.util;

import static com.chensoul.util.PropertiesUtils.flatProperties;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
public class PropertiesUtilsTest {

    @Test
    public void testFlatProperties() {
        Map<String, Object> level3Properties = Maps.of("f", "F");
        Map<String, Object> level2Properties = Maps.of("c", "C", "d", level3Properties);
        Map<String, Object> properties = Maps.of("a", "A", "b", level2Properties);
        Map<String, Object> flattenProperties = flatProperties(properties);
        assertEquals("A", flattenProperties.get("a"));
        assertEquals("C", flattenProperties.get("b.c"));
        assertEquals("F", flattenProperties.get("b.d.f"));
    }
}
