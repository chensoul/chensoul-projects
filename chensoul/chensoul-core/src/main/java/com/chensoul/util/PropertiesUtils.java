package com.chensoul.util;

import static com.chensoul.constant.SymbolConstants.DOT;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public abstract class PropertiesUtils {
    public static Map<String, Object> flatProperties(Map<String, Object> properties) {
        if (properties == null || properties.isEmpty()) {
            return Maps.newHashMap();
        }
        Map<String, Object> flattenProperties = new LinkedHashMap<>();
        flatProperties(properties, null, flattenProperties);
        return Collections.unmodifiableMap(flattenProperties);
    }

    static void flatProperties(Map<String, Object> properties, String propertyNamePrefix,
        Map<String, Object> flattenProperties) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String propertyName = normalizePropertyName(propertyNamePrefix, entry.getKey());
            Object propertyValue = entry.getValue();
            if (propertyValue instanceof String) {
                flattenProperties.put(propertyName, propertyValue);
            } else if (propertyValue instanceof Map) {
                Map subProperties = (Map) propertyValue;
                flatProperties(subProperties, propertyName, flattenProperties);
            }
        }
    }

    private static String normalizePropertyName(String propertyNamePrefix, String propertyName) {
        return propertyNamePrefix == null ? propertyName : propertyNamePrefix + DOT + propertyName;
    }
}
