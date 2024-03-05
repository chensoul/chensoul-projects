package com.chensoul.spring.boot.audit.spi.principal;

import com.chensoul.spring.boot.audit.spi.PrincipalResolver;
import org.aspectj.lang.JoinPoint;

/**
 * AspectJPrincipalResolver is responsible for resolving the principal from the {@link JoinPoint} argument.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class AspectJPrincipalResolver<T> implements PrincipalResolver {
    private int argumentPosition;

    private Class<T> argumentType;

    public AspectJPrincipalResolver(final int argumentPosition, final Class<T> argumentType) {
        this.argumentPosition = argumentPosition;
        this.argumentType = argumentType;
    }

    @Override
    public String resolveFrom(final JoinPoint auditTarget, final Object returnValue) {
        if (argumentPosition >= 0
            && argumentPosition <= auditTarget.getArgs().length - 1
            && argumentType.isAssignableFrom(auditTarget.getArgs()[argumentPosition].getClass())) {
            return resolveFrom((T) auditTarget.getArgs()[argumentPosition], auditTarget, returnValue);
        }
        return null;
    }


    protected abstract String resolveFrom(T argument, JoinPoint auditTarget, Object returnValue);

}
