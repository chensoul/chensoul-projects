package com.chensoul.spring.boot.audit.spi;

import com.chensoul.spring.client.ClientInfo;
import org.aspectj.lang.JoinPoint;

/**
 * A spi interface for resolving the {@link ClientInfo} from the {@link JoinPoint} argument and return value.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface ClientInfoResolver {

    /**
     * Resolve the ClientInfo from the provided arguments and return value.
     *
     * @param joinPoint the point where the join occurred.
     * @param retVal    the return value from the method call.
     * @return the constructed ClientInfo object.  Should never return null!
     */
    ClientInfo resolveFrom(JoinPoint joinPoint, Object retVal);
}
