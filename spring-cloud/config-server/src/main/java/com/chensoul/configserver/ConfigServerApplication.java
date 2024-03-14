package com.chensoul.configserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Config Application
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServerApplication.class);

	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ConfigServerApplication.class, args);

		String repoLocation = ctx.getEnvironment().getProperty("spring.cloud.config.server.native.searchLocations");
		LOG.info("Serving configurations from folder: " + repoLocation);
	}

}
