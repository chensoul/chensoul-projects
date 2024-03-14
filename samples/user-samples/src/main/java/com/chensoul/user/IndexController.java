package com.chensoul.user;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    @Value("${server.port}")
    private Long port;

    @GetMapping
    public String index(HttpServletRequest request) {
        return port + ":" + request.getSession(true).getId();
    }
}
