package com.chensoul.auth;

import com.chensoul.openfeign.annotation.EnableCustomFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Auth Application
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@SpringBootApplication
@EnableCustomFeignClients
public class AuthApplication {
    /**
     * main method
     *
     * @param args parameter
     */
    public static void main(final String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
