package com.chensoul.util;

/**
 * ObjectUtils provides utility methods and decorators for {@code Object} objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class ObjectUtils {
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }
}
