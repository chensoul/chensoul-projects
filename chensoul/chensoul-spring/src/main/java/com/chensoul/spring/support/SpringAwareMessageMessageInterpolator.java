package com.chensoul.spring.support;

import java.util.Locale;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Configures the {@link javax.validation.Validator} to check the Spring Messages.
 *
 * @author Scott Battaglia
 * @since 3.4
 */
@Setter
public class SpringAwareMessageMessageInterpolator implements MessageInterpolator, MessageSourceAware {

    private final MessageInterpolator defaultMessageInterpolator = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();

    private MessageSource messageSource;

    @Override
    public String interpolate(final String s, final Context context) {
        return interpolate(s, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(final String s, final Context context, final Locale locale) {
        try {
            return messageSource.getMessage(s, context.getConstraintDescriptor()
                .getAttributes().values().toArray(ArrayUtils.EMPTY_OBJECT_ARRAY), locale);
        } catch (final NoSuchMessageException e) {
            return defaultMessageInterpolator.interpolate(s, context, locale);
        }
    }
}
