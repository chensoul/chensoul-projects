package com.chensoul.auth.infrastructure.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizationServerSettingsTests {

    @LocalServerPort
    private int serverPort;

    RestTemplate restTemplate = new RestTemplate();

    @Test
    void local() throws JsonProcessingException {
        ResponseEntity<Map> entity = restTemplate.getForEntity(
            String.format("http://127.0.0.1:%d/.well-known/oauth-authorization-server", serverPort), Map.class);

        Assert.isTrue(entity.getStatusCodeValue() == 200, "HTTP 状态码不正常");

        Map body = entity.getBody();

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        log.info("AuthorizationServerSettings:\n{}", objectWriter.writeValueAsString(body));
    }
}
