package com.chensoul.audit;

import com.chensoul.spring.boot.common.properties.CoreProperties;
import com.chensoul.spring.client.ClientInfo;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is {@link GroovyAuditManagerTest}.
 */
@Tag("Audits")
@SpringBootTest(classes = {
    SharedTestConfiguration.class,
},
    properties = {
        "chensoul.audit.slf4j.enabled=false",
        "chensoul.audit.groovy.enabled=true",
        "chensoul.audit.groovy.template.location=classpath:/GroovyAudit.groovy"
    })
@EnableConfigurationProperties(CoreProperties.class)
class GroovyAuditManagerTest {
    @Autowired
    private AuditManager auditManager;

    @Test
    void verifyOperation() throws Throwable {
        ClientInfo clientInfo = new ClientInfo("1.2.3.4", "1.2.3.4", UUID.randomUUID().toString())
            .setExtraInfo(Maps.of("Hello", "World"))
            .setHeaders(Maps.of("H1", "V1"));
        AuditActionContext ctx = new AuditActionContext("CAS",
            "user", "TEST", new String[]{"CAS"}, LocalDateTime.now(Clock.systemUTC()), true, null, 0L,
            clientInfo);
        auditManager.record(ctx);
    }
}
