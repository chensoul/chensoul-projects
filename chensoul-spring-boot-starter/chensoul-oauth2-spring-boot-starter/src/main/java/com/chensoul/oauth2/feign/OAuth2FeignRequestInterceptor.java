package com.chensoul.oauth2.feign;

import com.chensoul.oauth2.constants.SecurityConstants;
import static com.chensoul.oauth2.constants.SecurityConstants.BEARER_TYPE;
import static com.chensoul.oauth2.constants.SecurityConstants.GATEWAY_TOKEN_VALUE;
import static com.chensoul.oauth2.constants.SecurityConstants.HEADER_CLIENT_ID;
import static com.chensoul.oauth2.constants.SecurityConstants.HEADER_GATEWAY_TOKEN;
import com.chensoul.oauth2.support.CustomBearerTokenExtractor;
import com.chensoul.oauth2.util.SecurityUtils;
import com.chensoul.spring.util.HttpRequestUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

/**
 * OAuth2 Feign Request Interceptor
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
    CustomBearerTokenExtractor bearerTokenExtractor = new CustomBearerTokenExtractor();

    @Override
    public void apply(RequestTemplate template) {
        log.info("Setting headers for feign request");

        template.header(HEADER_CLIENT_ID, SecurityUtils.getClientId());
        template.header(HEADER_GATEWAY_TOKEN, new String(Base64Utils.encode(GATEWAY_TOKEN_VALUE.getBytes())));

        // 内部请求不用设置 token
        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
        if (!CollectionUtils.isEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
            log.debug("The request is from inner, don't need to set access token");
            return;
        }

        if (HttpRequestUtils.getHttpServletRequest().isPresent()) {
            String authorizationToken = (String) bearerTokenExtractor.extract(HttpRequestUtils.getHttpServletRequest().get()).getPrincipal();
            template.header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " " + authorizationToken);
        }
    }
}
