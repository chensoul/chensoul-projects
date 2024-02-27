package com.chensoul.audit.spi.resource;

import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;

/**
 * ArgumentAsStringResourceResolver is responsible for resolving the resource from the method arguments.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class ArgumentAsStringResourceResolver extends AbstractAuditResourceResolver {
    @Override
    protected String[] createResource(JoinPoint joinPoint, Object returnValue) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return new String[0];
        }

        final List<String> stringArgs = new ArrayList<>();
        for (final Object arg : args) {
            if (arg == null) {
                continue;
            }
            stringArgs.add(toResourceString(arg));
        }
        return stringArgs.toArray(new String[stringArgs.size()]);
    }

}
