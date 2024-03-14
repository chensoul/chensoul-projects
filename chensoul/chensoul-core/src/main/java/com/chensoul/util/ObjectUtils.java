package com.chensoul.util;

/**
 * ObjectUtils provides utility methods and decorators for {@code Object} objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 * @version $Id: $Id
 */
public abstract class ObjectUtils {
    /**
     * <p>defaultIfNull.</p>
     *
     * @param object a T object
     * @param defaultValue a T object
     * @param <T> a T class
     * @return a T object
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }
}
