package com.chensoul.authorization.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT,
    properties = {"spring.cloud.config.enabled=false", "eureka.client.enabled=false"})
class ClientCredentialsTests {

    @LocalServerPort
    private int serverPort;

    @Test
    void start() throws JsonProcessingException {
        String clientId = "messaging-client";
        String clientSecret = "secret";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(clientId, clientSecret);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.put("grant_type", Collections.singletonList("client_credentials"));
        requestBody.put("scope", Collections.singletonList("openid profile message.read message.write"));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        Map map = restTemplate.postForObject(String.format("http://localhost:%d/oauth2/token", serverPort), httpEntity,
            Map.class);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

        log.info("token:\n{}", objectWriter.writeValueAsString(map));
    }

}
