package com.chensoul.auth.infrastructure.security.authentication.password;

import com.chensoul.auth.infrastructure.security.support.handler.CustomAuthenticationFailureHandler;
import com.chensoul.auth.infrastructure.security.support.handler.CustomAuthenticationSuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class PasswordLoginConfigure extends AbstractHttpConfigurer<PasswordLoginConfigure, HttpSecurity> {
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        final ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        http.authenticationProvider((AuthenticationProvider) context.getBean(PasswordLoginAuthenticationProvider.class));

        final CustomAuthenticationSuccessHandler successHandler = context.getBean(CustomAuthenticationSuccessHandler.class);
        final CustomAuthenticationFailureHandler failureHandler = context.getBean(CustomAuthenticationFailureHandler.class);

        final AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        final PasswordLoginProcessingFilter passwordLoginFilter = new PasswordLoginProcessingFilter(managerBuilder.getObject());
        passwordLoginFilter.setAuthenticationSuccessHandler(successHandler);
        passwordLoginFilter.setAuthenticationFailureHandler(failureHandler);

        http.addFilterAfter(passwordLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
