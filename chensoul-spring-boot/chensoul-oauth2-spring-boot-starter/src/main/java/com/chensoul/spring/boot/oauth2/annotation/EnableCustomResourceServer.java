package com.chensoul.spring.boot.oauth2.annotation;

import com.chensoul.spring.boot.oauth2.configuration.JwtAccessTokenConverterConfiguration;
import com.chensoul.spring.boot.oauth2.configuration.OAuthCommonConfiguration;
import com.chensoul.spring.boot.oauth2.configuration.OAuthResourceServerConfiguration;
import com.chensoul.spring.boot.oauth2.configuration.WebSecurityConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 资源服务注解
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableResourceServer
@Import({
    OAuthCommonConfiguration.class,
    OAuthResourceServerConfiguration.class,
    WebSecurityConfiguration.class,
    JwtAccessTokenConverterConfiguration.class
})
public @interface EnableCustomResourceServer {

}
