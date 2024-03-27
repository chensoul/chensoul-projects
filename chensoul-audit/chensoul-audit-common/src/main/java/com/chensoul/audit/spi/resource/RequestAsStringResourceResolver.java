package com.chensoul.audit.spi.resource;

import com.chensoul.spring.util.HttpRequestUtils;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
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
