package com.chensoul.oauth2.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * @param accessDeniedHandler
     */
    public WebSecurityConfiguration(final AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        //前后端分离项目，不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and().authorizeRequests().anyRequest().authenticated()
            .and().formLogin().disable()
            .exceptionHandling()
            .accessDeniedHandler(this.accessDeniedHandler);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui/*", "/v3/api-docs", "/favicon.ico");
    }
}
