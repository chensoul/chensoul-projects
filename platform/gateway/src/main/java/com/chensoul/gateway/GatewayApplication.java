package com.chensoul.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

    public static void main(final String[] args) {
        //https://github.com/spring-projects/spring-boot/issues/26218
        System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
        SpringApplication.run(GatewayApplication.class, args);
    }

}
