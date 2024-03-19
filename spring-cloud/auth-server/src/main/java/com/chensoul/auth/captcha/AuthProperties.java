package com.chensoul.auth.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private LoginProperties login = new LoginProperties();

    @Data
    public class LoginProperties {
        public final static String PASSWORD_LOGIN_URL = "/auth/passwordLogin";
        public final static String SMS_LOGIN_URL = "/auth/smsLogin";

        private String passwordLoginUrl = PASSWORD_LOGIN_URL;

        private String smsLoginUrl = SMS_LOGIN_URL;

        private PasswordEncryptProperties passwordEncrypt = new PasswordEncryptProperties();
        private CaptchaProperties captcha = new CaptchaProperties();
    }

    @Data
    public class PasswordEncryptProperties {
        private boolean enabled;
        private String secret;
    }

    @Data
    public class CaptchaProperties {
        private boolean enabled = Boolean.FALSE;
        private String prefix = "";
        private long expiredSecond = 300L;
        private boolean dev;
    }
}
