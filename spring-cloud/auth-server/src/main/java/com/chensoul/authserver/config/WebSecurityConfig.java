package com.chensoul.authserver.config;

import org.springframework.context.annotation.Bean;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
        UserDetails one = User.withDefaultPasswordEncoder().username("dev-usr").password("dev-pwd").roles("user").build();
        return new InMemoryUserDetailsManager(one);
    }
}
