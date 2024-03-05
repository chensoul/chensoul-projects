package com.chensoul.adminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@EnableAdminServer
@SpringBootApplication
public class AdminserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminserverApplication.class, args);
    }

}
