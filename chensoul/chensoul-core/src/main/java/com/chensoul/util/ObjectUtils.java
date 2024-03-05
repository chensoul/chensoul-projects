package com.chensoul.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * ObjectUtils provides utility methods and decorators for {@code Object} objects.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class ObjectUtils {
    public static boolean isArray(final Object object) {
        return object != null && object.getClass().isArray();
    }

    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static boolean anyNull(final Object... values) {
        return !allNotNull(values);
    }

    public static boolean allNotNull(final Object... values) {
        return values != null && Stream.of(values).noneMatch(Objects::isNull);
    }

    public static <T> T firstNonNull(final T... values) {
        return Arrays.stream(values).filter(Objects::nonNull).findFirst().orElse(null);
    }

}
