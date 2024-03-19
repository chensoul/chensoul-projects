package com.chensoul.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@RefreshScope
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    private final String username;
    private final String password;

    @Autowired
    public SecurityConfiguration(
        @Value("${app.eureka-username}") String username,
        @Value("${app.eureka-password}") String password, ConfigurableApplicationContext ctx) {
        this.username = username;
        this.password = password;

        log.info("{}", ctx.getEnvironment().getProperty("app.eureka-username"));
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username(username)
            .password(password)
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth ->
            auth.antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated());

        // disable CSRF to allow /eureka/** endpoins
        http.csrf(csrf -> csrf.disable()).httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
