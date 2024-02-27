package com.chensoul.auth.infrastructure.oauth2;

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
    private PasswordEncryptProperties passwordEncrypt = new PasswordEncryptProperties();
    public CaptchaProperties captcha = new CaptchaProperties();

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
