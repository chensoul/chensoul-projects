package com.chensoul.usersample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.cloud.config.enabled=false", "eureka.client.enabled=false"})
class UserSampleApplicationTests {

    @Test
    void contextLoads() {
    }

}
