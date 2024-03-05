package com.chensoul.spring.boot.audit.spi.clientinfo;

import com.chensoul.spring.boot.audit.spi.ClientInfoResolver;
import com.chensoul.spring.util.HttpRequestUtils;
import com.chensoul.spring.client.ClientInfo;
import com.chensoul.spring.client.ClientInfoHolder;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DefaultClientInfoResolver is responsible for resolving the {@link ClientInfo} from the {@link ClientInfoHolder}.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DefaultClientInfoResolver implements ClientInfoResolver {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public ClientInfo resolveFrom(final JoinPoint joinPoint, final Object retVal) {
        final ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
        if (clientInfo != null) {
            return resolveClientInfo(clientInfo);
        }
        log.warn("No ClientInfo form thread local could be found. Returning ClientInfo from request.");

        return ClientInfo.from(HttpRequestUtils.getHttpServletRequest().get(), null);
    }

    protected ClientInfo resolveClientInfo(final ClientInfo clientInfo) {
        return clientInfo;
    }
}
