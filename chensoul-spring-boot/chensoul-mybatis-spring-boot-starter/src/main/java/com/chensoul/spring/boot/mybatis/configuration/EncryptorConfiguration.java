package com.chensoul.spring.boot.mybatis.configuration;

import com.chensoul.spring.boot.mybatis.encrypt.DefaultEncryptor;
import com.chensoul.spring.boot.mybatis.encrypt.Encryptor;
import com.chensoul.spring.boot.mybatis.encrypt.FieldBinder;
import com.chensoul.spring.boot.mybatis.interceptor.FieldDecryptInterceptor;
import com.chensoul.spring.boot.mybatis.interceptor.FieldEncryptInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "chensoul.encryptor", name = "password")
@EnableConfigurationProperties({EncryptorProperties.class})
public class EncryptorConfiguration {

    @Autowired
    private EncryptorProperties encryptorProperties;

    /**
     * @param encryptor
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public FieldEncryptInterceptor fieldEncryptInterceptor(Encryptor encryptor) {
        return new FieldEncryptInterceptor(encryptor, this.encryptorProperties);
    }

    /**
     * @param encryptor
     * @param fieldBinder
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public FieldDecryptInterceptor fieldDecryptInterceptor(@Autowired(required = false) Encryptor encryptor,
                                                           @Autowired(required = false) FieldBinder fieldBinder) {
        return new FieldDecryptInterceptor(encryptor, fieldBinder, this.encryptorProperties);
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Encryptor encryptor() {
        return new DefaultEncryptor();
    }

}
