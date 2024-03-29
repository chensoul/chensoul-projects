package com.chensoul.spring.support;

import com.chensoul.lang.function.CheckedSupplier;
import java.time.ZonedDateTime;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * This is {@link Converters}.
 */
@NoArgsConstructor
public class Converters {

    /**
     * The Zoned date time to string converter
     * turns a {@link ZonedDateTime} into a formatted string.
     */
    public static class ZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {

        @Override
        public String convert(final ZonedDateTime zonedDateTime) {
            return zonedDateTime.toString();
        }
    }

    /**
     * Converts a String to a resource instance.
     */
    public static class StringToResourceConverter implements Converter<String, Resource> {

        @Override
        public Resource convert(final String resource) {
            return CheckedSupplier.unchecked(() -> new DefaultResourceLoader().getResource(resource)).get();
        }
    }
}
