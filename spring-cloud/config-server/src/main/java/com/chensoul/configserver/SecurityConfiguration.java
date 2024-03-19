package com.chensoul.configserver;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> auth.antMatchers("/actuator/**").permitAll().anyRequest().authenticated());

        // Disable CSRF to allow POST to /encrypt and /decrypt endpoins
        http.csrf(csrf -> csrf.disable()).httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
