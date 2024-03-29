package com.chensoul.collection;

import static com.chensoul.collection.PropertiesUtils.flatProperties;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
public class PropertiesUtilsTest {

    @Test
    public void testFlatProperties() {
        Map<String, Object> level3Properties = MapUtils.of("f", "F");
        Map<String, Object> level2Properties = MapUtils.of("c", "C", "d", level3Properties);
        Map<String, Object> properties = MapUtils.of("a", "A", "b", level2Properties);
        Map<String, Object> flattenProperties = flatProperties(properties);
        assertEquals("A", flattenProperties.get("a"));
        assertEquals("C", flattenProperties.get("b.c"));
        assertEquals("F", flattenProperties.get("b.d.f"));
    }
}
