package com.chensoul.auth.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Web security config
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                authorizeRequests -> authorizeRequests
                    .antMatchers("/actuator/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                    .regexMatchers("^/actuator(/.*)?$").permitAll()
//                    .regexMatchers("^/(swagger-ui|v3/api-docs)(/.*)?$").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(withDefaults());

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    /**
     * @see UserDetailsService 用于检索用户进行身份验证的实例。
     */
    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        UserDetails one = User.withDefaultPasswordEncoder().username("user").password("pw").roles("user").build();
        UserDetails two = User.withDefaultPasswordEncoder().username("admin").password("pw").roles("admin", "user").build();

        return new InMemoryUserDetailsManager(one, two);
    }
}