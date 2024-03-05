package com.chensoul.spring.boot.oauth2.support;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

/**
 * Custom BearerTokenExtractor，resource request header token.
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class CustomBearerTokenExtractor extends BearerTokenExtractor {
    /**
     * @param request The request.
     * @return
     */
    @Override
    protected String extractHeaderToken(final HttpServletRequest request) {
        final Enumeration<String> headers = request.getHeaders(OAuth2AccessToken.ACCESS_TOKEN);
        while (headers.hasMoreElements()) {
            final String token = headers.nextElement();
            if (token != null) {
                return token;
            }
        }
        return super.extractHeaderToken(request);
    }
}
