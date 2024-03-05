package com.chensoul.spring.boot.oauth2.configuration;

import com.chensoul.spring.boot.oauth2.feign.OAuth2FeignRequestInterceptor;
import com.chensoul.spring.boot.oauth2.support.CustomWebResponseExceptionTranslator;
import com.chensoul.spring.boot.oauth2.support.PermissionService;
import com.chensoul.spring.boot.oauth2.support.ResultMessageResolver;
import javax.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.event.LoggerListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 *
 */
@Configuration
@ConditionalOnClass(Servlet.class)
public class OAuthCommonConfiguration {

    /**
     * web响应异常转换器
     */
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    /**
     * OAuth2 AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(final WebResponseExceptionTranslator exceptionTranslator) {
        final OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
        accessDeniedHandler.setExceptionTranslator(exceptionTranslator);
        return accessDeniedHandler;
    }

    /**
     * OAuth2 AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(final WebResponseExceptionTranslator exceptionTranslator) {
        final OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionTranslator(exceptionTranslator);
        return authenticationEntryPoint;
    }

    /**
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * @return
     */
    @Bean
    public ResultMessageResolver ResultMessageResolver() {
        return new ResultMessageResolver();
    }

    /**
     * @return
     */
    @Bean("pms")
    public PermissionService permissionService() {
        return new PermissionService();
    }

    /**
     * @return
     */
    @Bean
    public LoggerListener loggerListener() {
        return new LoggerListener();
    }

    /**
     * @return
     */
    @Bean
    public org.springframework.security.authentication.event.LoggerListener oauthLoggerListener() {
        return new org.springframework.security.authentication.event.LoggerListener();
    }

    /**
     * @return
     */
    @Bean
    public OAuth2FeignRequestInterceptor feignOAuth2RequestInterceptor() {
        return new OAuth2FeignRequestInterceptor();
    }
}
