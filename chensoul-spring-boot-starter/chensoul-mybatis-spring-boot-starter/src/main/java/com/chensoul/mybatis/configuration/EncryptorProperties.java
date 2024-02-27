package com.chensoul.mybatis.configuration;

import com.chensoul.mybatis.encrypt.Algorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Data
@ConfigurationProperties(prefix = "chensoul.encryptor")
public class EncryptorProperties {

    private String password;

    private Algorithm algorithm;
}
