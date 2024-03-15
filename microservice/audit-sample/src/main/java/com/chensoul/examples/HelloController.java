package com.chensoul.examples;

import com.chensoul.audit.Audit;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/hello")
    @Audit("你好")
    public static String hello(Authentication authentication) {
        return "hello: " + authentication.getName();
    }
}
