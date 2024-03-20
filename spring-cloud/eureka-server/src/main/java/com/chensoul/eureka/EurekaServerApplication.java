package com.chensoul.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Eureka Application
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(final String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EurekaServerApplication.class, args);

        String serviceUrl = ctx.getEnvironment().getProperty("eureka.client.serviceUrl.defaultZone");
        String username = ctx.getEnvironment().getProperty("spring.security.user.name");

        log.info("Serving configurations for secured user: {} from serviceUrl: {}", username, serviceUrl);
    }

}
