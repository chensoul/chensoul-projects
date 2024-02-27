package com.chensoul.examples;

import com.chensoul.oauth2.annotation.EnableCustomResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomResourceServer
@SpringBootApplication
public class ExampleAuditApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ExampleAuditApplication.class, args);
    }
}
