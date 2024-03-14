package com.chensoul.spring.boot.audit;

import com.chensoul.audit.spring.boot.AuditConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

@ImportAutoConfiguration({
//        RefreshAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        AopAutoConfiguration.class
})
@SpringBootConfiguration
@Import({
        AuditConfiguration.class,
        AuditConfiguration.class,
})
public class SharedTestConfiguration {
}
