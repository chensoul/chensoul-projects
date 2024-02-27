package com.chensoul.auth.infrastructure.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@ConfigurationProperties(prefix = "auth.login")
public class LoginProperties {
    public final static String PASSWORD_LOGIN_URL = "/auth/passwordLogin";
    public final static String SMS_LOGIN_URL = "/auth/smsLogin";

    private String passwordLoginUrl = PASSWORD_LOGIN_URL;

    private String smsLoginUrl = SMS_LOGIN_URL;

}
