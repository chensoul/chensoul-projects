package com.chensoul.audit.spi.resource;

import com.chensoul.audit.spi.AuditResourceResolver;
import com.chensoul.util.StringUtils;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;

/**
 * Abstract AuditResourceResolver.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class AbstractAuditResourceResolver implements AuditResourceResolver {

    protected Function<String, String> resourcePostProcessor = input -> input;

    public void setResourcePostProcessor(final Function<String, String> resourcePostProcessor) {
        if (resourcePostProcessor == null) {
            throw new IllegalArgumentException("Resource post processor cannot be null");
        }
        this.resourcePostProcessor = resourcePostProcessor;
    }

    @Override
    public final String[] resolveFrom(final JoinPoint joinPoint, final Object returnValue) {
        String[] resource = createResource(joinPoint, returnValue);
        return Arrays.stream(resource).map(this.resourcePostProcessor::apply).filter(StringUtils::isNotBlank).collect(Collectors.toList()).toArray(new String[]{});
    }

    @Override
    public final String[] resolveFrom(final JoinPoint joinPoint, final Throwable e) {
        String[] resource = createResource(joinPoint, e);
        return Arrays.stream(resource).map(this.resourcePostProcessor::apply).filter(StringUtils::isNotBlank).collect(Collectors.toList()).toArray(new String[]{});
    }

    protected abstract String[] createResource(final JoinPoint joinPoint, final Object retVal);
}
