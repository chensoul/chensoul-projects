package com.chensoul.audit.spi.resource;

import java.util.Collection;
import java.util.Iterator;
import org.aspectj.lang.JoinPoint;
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
