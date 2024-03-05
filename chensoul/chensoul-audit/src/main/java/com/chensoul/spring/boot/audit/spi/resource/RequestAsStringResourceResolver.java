package com.chensoul.spring.boot.audit.spi.resource;

import com.chensoul.spring.util.HttpRequestUtils;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;

/**
 * RequestAsStringResourceResolver is responsible for resolving the resource from the {@link HttpServletRequest}.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class RequestAsStringResourceResolver extends AbstractAuditResourceResolver {
    @Override
    protected String[] createResource(JoinPoint joinPoint, Object retVal) {
        HttpServletRequest httpServletRequest = HttpRequestUtils.getHttpServletRequest().get();
        if (httpServletRequest != null) {
            String messagePayload = HttpRequestUtils.getMessagePayload(httpServletRequest);
            String messageParam = HttpRequestUtils.createMessage(httpServletRequest, true, false, false, false, null);
            return new String[]{messageParam, messagePayload};
        }

        return new String[]{};

    }
}
