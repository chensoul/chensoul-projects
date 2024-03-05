package com.chensoul.spring.boot.audit.spi.resource;

import java.util.Collection;
import java.util.Iterator;
import org.aspectj.lang.JoinPoint;

/**
 * ReturnValueAsStringResourceResolver is responsible for resolving the resource from the method return value.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class ReturnValueAsStringResourceResolver extends AbstractAuditResourceResolver {
    @Override
    protected String[] createResource(JoinPoint joinPoint, Object retVal) {
        if (retVal instanceof Collection) {
            final Collection c = (Collection) retVal;
            final String[] retvals = new String[c.size()];

            int i = 0;
            for (final Iterator iter = c.iterator(); iter.hasNext() && i < c.size(); i++) {
                final Object o = iter.next();

                if (o != null) {
                    retvals[i] = toResourceString(o);
                }
            }

            return retvals;
        }

        if (retVal instanceof Object[]) {
            final Object[] vals = (Object[]) retVal;
            final String[] retvals = new String[vals.length];
            for (int i = 0; i < vals.length; i++) {
                retvals[i] = toResourceString(vals[i]);
            }

            return retvals;
        }

        return new String[]{toResourceString(retVal)};
    }
}
