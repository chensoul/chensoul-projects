package com.chensoul.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

	public static void main(final String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}

}
