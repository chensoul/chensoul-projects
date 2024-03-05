package com.chensoul.auth.infrastructure.security.configuration;

import com.chensoul.auth.infrastructure.security.authentication.password.PasswordLoginConfigure;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(LoginProperties.class)
public class WebSecurityConfiguration {
    LoginProperties loginProperties;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.antMatcher("/**");
        http.apply(new PasswordLoginConfigure());

        http.authorizeRequests(authorize -> {
                authorize.antMatchers(this.loginProperties.getPasswordLoginUrl(), this.loginProperties.getSmsLoginUrl()).permitAll();
                authorize.anyRequest().authenticated();
            })
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        return http.build();
    }
}
