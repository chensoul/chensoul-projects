package com.chensoul.spring.boot.oauth2.support;

import com.chensoul.spring.boot.oauth2.exception.CustomOAuth2Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

/**
 * 包装 OAuth2Exception
 */
@Slf4j
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    /**
     * @param e
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
        OAuth2Exception originEx = responseEntity.getBody();
        HttpHeaders headers = responseEntity.getHeaders();

        CustomOAuth2Exception customOAuth2Exception = CustomOAuth2Exception.from(originEx);
        return new ResponseEntity<>(customOAuth2Exception, headers, responseEntity.getStatusCode());
    }
}
