package com.chensoul.spring.boot.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 包装 {@link OAuth2Exception}，使用 {@link CustomOAuth2ExceptionJackson2Serializer} 处理异常返回信息
 */
@JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

    /**
     * @param msg
     */
    CustomOAuth2Exception(String msg) {
        super(msg);
    }

    /**
     * @param origin
     * @return
     */
    public static CustomOAuth2Exception from(OAuth2Exception origin) {
        CustomOAuth2Exception ex = new CustomOAuth2Exception(origin.getMessage()) {
            @Override
            public String getOAuth2ErrorCode() {
                return origin.getOAuth2ErrorCode();
            }

            @Override
            public int getHttpErrorCode() {
                return origin.getHttpErrorCode();
            }

            @Override
            public Map<String, String> getAdditionalInformation() {
                return origin.getAdditionalInformation();
            }

            @Override
            public String toString() {
                return this.getSummary();
            }

            @Override
            public String getSummary() {
                return origin.getSummary();
            }
        };
        ex.initCause(origin);
        return ex;
    }
}
