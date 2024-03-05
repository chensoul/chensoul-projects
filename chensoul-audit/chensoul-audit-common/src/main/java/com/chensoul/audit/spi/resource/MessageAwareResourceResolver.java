package com.chensoul.audit.spi.resource;

import com.chensoul.audit.spi.AuditResourceResolver;
import com.chensoul.util.StringUtils;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import org.aspectj.lang.JoinPoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * MessageAwareResourceResolver is responsible for resolving the resource from the message bundle.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class MessageAwareResourceResolver implements AuditResourceResolver {
    private final ApplicationContext context;
    private final AuditResourceResolver delegate;

    public MessageAwareResourceResolver(ApplicationContext context, AuditResourceResolver delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    /**
     * @param joinPoint   the join point that contains the arguments.
     * @param returnValue The returned value
     * @return
     */
    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Object returnValue) {
        return delegate.resolveFrom(joinPoint, returnValue);
    }

    /**
     * @param joinPoint the join point that contains the arguments.
     * @param exception The exception incurred when the join point proceeds.
     * @return
     */
    @Override
    public String[] resolveFrom(JoinPoint joinPoint, Throwable exception) {
        return resolveMessagesFromBundleOrDefault(delegate.resolveFrom(joinPoint, exception), exception);
    }

    private String[] resolveMessagesFromBundleOrDefault(final String[] resolved, final Throwable e) {
        Locale locale = LocaleContextHolder.getLocale();
        String defaultKey = String.join("_",
                StringUtils.splitByCharacterTypeCamelCase(e.getClass().getSimpleName())).toUpperCase();
        return Stream.of(resolved)
                .map(key -> toResourceString(context.getMessage(key, null, defaultKey, locale)))
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }
}
