package com.chensoul.eureka;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfiguration {
//    @Value("${app.eureka-username}")
//    private String username;
//
//    @Value("${app.eureka-password}")
//    private String password;
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//            .username(username)
//            .password(password)
//            .roles("USER")
//            .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf
                .ignoringAntMatchers("/eureka/**")
            )
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> {
                auth.antMatchers("/actuator/**").permitAll();
                auth.anyRequest().authenticated();
            })
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}
