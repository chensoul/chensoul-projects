package com.chensoul.authserver;

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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Slf4j
@AutoConfigureMockMvc
@Import(TestApplicationConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
    properties = {"spring.cloud.config.enabled=false", "eureka.client.enabled=false"})
class AuthServerApplicationTest {

    @LocalServerPort
    private int serverPort;

    static final String CLIENT_ID = "messaging-client";

    static final String CLIENT_SECRET = "secret";

    @Test
    void contextLoads() {
    }

    public static String clientCredentialsAccessToken(RestTemplate restTemplate) throws JsonProcessingException {
        return clientCredentialsMap(restTemplate).get("access_token").toString();
    }

    public static Map clientCredentialsMap(RestTemplate restTemplate) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.put("grant_type", Collections.singletonList("client_credentials"));
        requestBody.put("scope", Collections.singletonList("openid profile message.read message.write"));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        @SuppressWarnings("all")
        Map map = restTemplate.postForObject("http://auth-server/oauth2/token", httpEntity, Map.class);

        log.info("token:\n{}", objectWriter.writeValueAsString(map));

        return map;
    }

}
